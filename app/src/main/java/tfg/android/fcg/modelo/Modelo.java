package tfg.android.fcg.modelo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.EmailAuthProvider;

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
    private Login loginActual;

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

    public Login getLoginActual(){
        return loginActual;
    }

    public void setLoginActual(Login usuario){
        this.loginActual = usuario;
    }

    //*************** METODOS ****************//

    @Override
    public void comprobarLogin(final Object[] informacion) {
    //TODO: ENCRIPTAR PASSWORD
        auth.signInWithEmailAndPassword((String)informacion[0],(String)informacion[1])
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Bundle extras = new Bundle();
                if(task.isSuccessful()){
                    //Usuario logged.
                    Log.i(TAG,"Login comprobado");
                    setUsuarioActual(auth.getCurrentUser());
                    SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
                    String email = sharedPreferences.getString("email",null);
                    if(email != null){
                        if(!email.equals((String)informacion[0])){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email",(String)informacion[0]);
                            editor.putString("password",(String)informacion[1]);
                            editor.apply();
                        }
                    }else{
                        Log.i(TAG,"Login comprobado");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", (String)informacion[0]);
                        editor.putString("password",(String)informacion[1]);
                        editor.apply();
                    }

                    String idUser = auth.getCurrentUser().getUid();
                    String emailUser = auth.getCurrentUser().getEmail();
                    String displayName = auth.getCurrentUser().getDisplayName();
                    String userPhone = auth.getCurrentUser().getPhoneNumber();

                    String[] partes = displayName.split("#");
                    String nombre = partes[0];
                    Boolean rol = Boolean.valueOf(partes[1]);

                    loginActual = new Login(idUser,nombre,emailUser,userPhone,rol);
                    Log.i(TAG, "Usuario actual: " + loginActual.getNombre());
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
        //informacion 0 = vieja, informacion 1 = nueva, informacion 2 = correo
        String correo = (String)informacion[2];
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
    public void registrarUsuario(Object[] informacion) {
       adaptadorUsuario.agregarUsuario(informacion);
    }

    @Override
    public void obtenerMapa(Object informacion) {

    }

    @Override
    public void obtenerPosicionUsuario(Object informacion) {
        adaptadorPosicion.obtenerPosicion((String)informacion);
    }

    @Override
    public void guardarOrigenYDestino(Object[] informacion) {
        adaptadorUsuario.actualizarUsuario(informacion);
    }

    @Override
    public void obtenerUsuariosPickUp(Object informacion) {

    }

    @Override
    public void guardarUsuarioPickup(Object[] informacion) {
        adaptadorVinculo.agregarVinculo(informacion);
    }

    @Override
    public void eliminarUsuarioPickup(Object[] informacion) {
        adaptadorVinculo.eliminarVinculo((String)informacion[0],(String)informacion[1]);
    }

    @Override
    public void buscarVehiculos(Object informacion) {

    }

    @Override
    public void seleccionarVehiculoOnTheGo(Object informacion) {

    }

    @Override
    public void guardarPerfil(Object[] informacion) {
        adaptadorUsuario.actualizarUsuario(informacion);
    }

    @Override
    public void eliminarPerfil(Object informacion) {
        adaptadorUsuario.eliminarUsuario((String)informacion);
    }

    @Override
    public void guardarVehiculo(Object[] informacion) {
        adaptadorVehiculo.agregarVehiculo(informacion);
    }

    @Override
    public void eliminarVehiculo(Object informacion) {

    }

    @Override
    public void obtenerHistorial(Object informacion) {

    }

    @Override
    public void asignarValoracion(Object[] informacion) {
        adaptadorUsuario.actualizarUsuario(informacion);
    }

    @Override
    public void guardarLocalizacion(Object[] informacion) {
        adaptadorPosicion.actualizarPosicion(informacion);
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
