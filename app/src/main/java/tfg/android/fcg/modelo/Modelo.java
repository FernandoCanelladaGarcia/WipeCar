package tfg.android.fcg.modelo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tfg.android.fcg.AppMediador;

public class Modelo implements IModelo{

    //Singleton
    private static Modelo singleton = null;

    private AppMediador appMediador;
    private BDAdaptadorHistorial adaptadorHistorial;
    private BDAdaptadorPosicion adaptadorPosicion;
    private BDAdaptadorUsuario adaptadorUsuario;
    private BDAdaptadorVehiculo adaptadorVehiculo;
    private BDAdaptadorVinculo adaptadorVinculo;

    //Variables de uso comun
    private FirebaseAuth auth;
    private FirebaseUser usuarioActual;
    private Login userActual;

    private final String TAG = "depurador";

    //Constructor
    private Modelo(){
        auth = FirebaseAuth.getInstance();
        appMediador = AppMediador.getInstance();

        adaptadorHistorial = new BDAdaptadorHistorial();
        adaptadorPosicion = new BDAdaptadorPosicion();
        adaptadorUsuario = new BDAdaptadorUsuario();
        adaptadorVehiculo = new BDAdaptadorVehiculo();
        adaptadorVinculo = new BDAdaptadorVinculo();
    }

    public static Modelo getInstance(){
        if(singleton == null)
            singleton = new Modelo();
        return singleton;
    }

    //**************** Getters y Setters necesarios para los test ***************** //


    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseUser getUsuarioActual(){
        return usuarioActual;
    }

    public void setUsuarioActual(FirebaseUser usuario){
        this.usuarioActual = usuario;
    }

    public Login getUserActual(){
        return userActual;
    }

    public void setUserActual(Login usuario){
        this.userActual = usuario;
    }

    @Override
    public void comprobarLogin(final Object[] informacion) {
        auth.signInWithEmailAndPassword((String)informacion[0],(String)informacion[1])
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Bundle extras = new Bundle();
                if(task.isSuccessful()){
                    //Usuario logged.
                    SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
                    String email = sharedPreferences.getString("email",null);
                    if(email != null){
                        if(!email.equals((String)informacion[0])){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email",(String)informacion[0]);
                            editor.apply();
                        }
                    }else{
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", (String)informacion[0]);
                        editor.apply();
                    }

                    usuarioActual = auth.getCurrentUser();

                    String idUser = usuarioActual.getUid();
                    String emailUser = usuarioActual.getEmail();

                    String displayName = usuarioActual.getDisplayName();
                    String[] partes = displayName.split("#");
                    String nombre = partes[0];
                    String telefono = partes [1];
                    Boolean rol = Boolean.valueOf(partes[2]);

                    userActual = new Login(idUser,nombre,emailUser,telefono,rol);
                    Log.i(TAG, "Usuario actual: " + userActual.getNombre());
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_LOGIN, true);
                    appMediador.sendBroadcast(AppMediador.AVISO_USER_LOGIN, extras);
                }
                else{
                    //Fallo en login
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_LOGIN,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_USER_LOGIN, extras);
                }
            }
        });
    }

    @Override
    public void recuperarPassword(Object informacion) {
        auth.sendPasswordResetEmail((String)informacion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Bundle extras = new Bundle();
                if(task.isSuccessful()){
                    Log.i(TAG,"Se ha enviado el correo correspondiente para restablecer contraseña");
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_RECUPERAR_PASSWORD,true);
                    appMediador.sendBroadcast(AppMediador.AVISO_CORREO_PASSWORD,extras);
                }else{
                    Log.i(TAG,"NO se ha enviado el correo para restablecer contraseña");
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_RECUPERAR_PASSWORD,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_CORREO_PASSWORD,extras);
                }
            }
        });
    }

    @Override
    public void cambiarPassword(final Object[] informacion) {

        String correo = userActual.getEmail();

        AuthCredential credential = EmailAuthProvider.getCredential(correo, (String) informacion[0]);
        usuarioActual.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Bundle extras = new Bundle();
                if(task.isSuccessful()){
                    usuarioActual.updatePassword((String)informacion[1]).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Bundle extras = new Bundle();
                            if(task.isSuccessful()){
                                Log.i(TAG,"Exito en actualizacion de contraseña");
                                extras.putBoolean(AppMediador.CLAVE_RESULTADO_CAMBIO_PASSWORD, true);

                                appMediador.sendBroadcast(AppMediador.AVISO_CAMBIO_PASSWORD,extras);
                            }else{
                                Log.i(TAG,"No hubo exito en actualizacion de contraseña");
                                extras.putBoolean(AppMediador.CLAVE_RESULTADO_CAMBIO_PASSWORD,false);
                                appMediador.sendBroadcast(AppMediador.AVISO_CAMBIO_PASSWORD,extras);
                            }
                        }
                    });
                }else{
                    Log.i(TAG,"No hubo exito en reautenticacion");
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_CAMBIO_PASSWORD,false);

                    appMediador.sendBroadcast(AppMediador.AVISO_CAMBIO_PASSWORD,extras);
                }
            }
        });
    }

    @Override
    public void registrarUsuario(Object informacion) {
       adaptadorUsuario.agregarUsuario(informacion);
    }

    @Override
    public void obtenerMapa(Object informacion) {

    }

    @Override
    public void obtenerPosicionUsuario(Object informacion) {

    }

    @Override
    public void guardarOrigenYDestino(Object informacion) {

    }

    @Override
    public void obtenerUsuariosPickUp(Object informacion) {

    }

    @Override
    public void guardarUsuarioPickup(Object informacion) {

    }

    @Override
    public void eliminarUsuarioPickup(Object informacion) {

    }

    @Override
    public void buscarVehiculos(Object informacion) {

    }

    @Override
    public void seleccionarVehiculoOnTheGo(Object informacion) {

    }

    @Override
    public void guardarPerfil(Object informacion) {

    }

    @Override
    public void eliminarPerfil(Object informacion) {

    }

    @Override
    public void guardarVehiculo(Object informacion) {

    }

    @Override
    public void eliminarVehiculo(Object informacion) {

    }

    @Override
    public void obtenerHistorial(Object informacion) {

    }

    @Override
    public void asignarValoracion(Object informacion) {

    }

    @Override
    public void guardarLocalizacion(Object informacion) {

    }

    @Override
    public void obtenerPeticionesDePasajeros(Object informacion) {

    }

    @Override
    public void aceptarPasajero(Object informacion) {

    }

    @Override
    public void rechazarPasajero(Object informacion) {

    }

    @Override
    public void pararRuta(Object informacion) {

    }

}
