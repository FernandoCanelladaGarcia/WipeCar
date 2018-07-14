package tfg.android.fcg.modelo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
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
    public void comprobarLogin(Object[] informacion) {
    //TODO: ENCRIPTAR PASSWORD
        final String email = (String)informacion[0];
        final String password = (String)informacion[1];
        Log.i(TAG, email + " " + password);
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Bundle extras = new Bundle();
                if(task.isSuccessful()){
                    //Usuario logged.
                    Log.i(TAG,"Login comprobado");
                    setUsuarioActual(auth.getCurrentUser());

                    String idUser = auth.getCurrentUser().getUid();

                    SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String emailpref = sharedPreferences.getString("email",null);
                    if(emailpref != null){
                        if(!emailpref.equals(email)){
                            editor.putString("email",email);
                            editor.putString("password",password);
                            editor.apply();
                        }
                    }else{
                        Log.i(TAG,"Login comprobado");
                        editor.putString("email", email);
                        editor.putString("password",password);
                        editor.apply();
                    }
                    String id = sharedPreferences.getString("idUser", null);
                    if(id == null){
                        Log.i(TAG,"ACTUALIZANDO SHARED PREFERENCES");
                        adaptadorUsuario.obtenerSharedPreferences(idUser);
                    }
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

    //TODO: NUEVO METODO, REDACCION
    @Override
    public void deslogearUsuario(){
        auth.signOut();

        Log.i(TAG, "deslogeado usuario" );

        Bundle extras = new Bundle();
        extras.putBoolean(AppMediador.CLAVE_DESLOGIN, true);
        appMediador.sendBroadcast(AppMediador.AVISO_DESLOGIN,extras);
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

    //TODO: NO SE HA UTILIZADO, REDACCION
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
    public void registrarUsuario(Object informacion) {
       adaptadorUsuario.agregarUsuario((Object[]) informacion);
    }

    @Override
    public void obtenerUsuario(Object informacion) {
        adaptadorUsuario.obtenerUsuario((String)informacion);
    }

    @Override
    public void obtenerVehiculoUsuario(Object informacion) {
        adaptadorVehiculo.obtenerVehiculo((String) informacion);
    }

    //TODO: NO SE USA, REDACCION
    @Override
    public void obtenerMapa(Object informacion) {

    }

    //TODO: NUEVO, REDACCION
    @Override
    public void iniciarGps() {
        adaptadorPosicion.iniciarGps();
    }

    @Override
    public void cambiarRol(Object informacion){
        adaptadorUsuario.cambiarRol((boolean)informacion);
    }

    @Override
    public void obtenerPosicionUsuario(Object informacion){
        adaptadorPosicion.obtenerPosicion(informacion);
    }

    @Override
    public void guardarOrigenYDestino(Object[] informacion) {
        Object[] datos = new Object[13];
        datos[0] = 1;
        datos[5] = informacion[0];
        datos[6] = informacion[1];
        adaptadorUsuario.actualizarUsuario(datos);
    }

    @Override
    public void obtenerUsuariosPickUp(Object[] informacion) {
        //Pasajeros o Conductores;
        int tarea = (int) informacion[0];

        switch (tarea){
            case 0:
                //buscarPasajeros
                adaptadorUsuario.obtenerListaPasajeros((String)informacion[1]);
                break;
            case 1:
                //buscarConductores
                adaptadorUsuario.obtenerListaConductores((String)informacion[1]);
                break;
        }

    }

    @Override
    public void guardarUsuarioPickup(Object[] informacion) {
        adaptadorVinculo.agregarVinculo(informacion);
    }

    @Override
    public void eliminarUsuarioPickup(Object[] informacion) {
        adaptadorVinculo.eliminarVinculo(informacion);
    }

    @Override
    public void buscarVehiculos(Object informacion) {
        adaptadorUsuario.obtenerListaConductores((String)informacion);
    }

    @Override
    public void seleccionarVehiculoOnTheGo(Object informacion) {
        adaptadorVinculo.agregarVinculo((Object[])informacion);
    }

    @Override
    public void guardarPerfil(Object[] informacion) {
        adaptadorUsuario.actualizarUsuario(informacion);
    }

    @Override
    public void eliminarPerfil(Object[] informacion) {
        adaptadorUsuario.eliminarUsuario((String)informacion[0],(String)informacion[1],(String)informacion[2]);
        adaptadorVehiculo.eliminarVehiculo(informacion[0]);
        adaptadorPosicion.eliminarPosicion(informacion[0]);
    }

    @Override
    public void guardarVehiculo(Object[] informacion) {
        int tarea = (int)informacion[4];
        switch (tarea){
            case 0:
                Log.i(TAG,"Agregando vehiculo");
                adaptadorVehiculo.agregarVehiculo(informacion);
                break;
            case 1:
                Log.i(TAG,"Actualizando vehiculo");
                adaptadorVehiculo.actualizarVehiculo(informacion);
                break;
        }
    }

    @Override
    public void eliminarVehiculo(Object informacion) {
        adaptadorVehiculo.eliminarVehiculo(informacion);
    }

    @Override
    public void obtenerHistorial(Object informacion) {
        adaptadorHistorial.obtenerHistorial((String)informacion);
    }

    @Override
    public void asignarValoracion(Object[] informacion) {
        adaptadorUsuario.actualizarUsuario(informacion);
        //adaptadorHistorial.agregarValoracion((String[]) informacion);
    }

    @Override
    public void eliminarHistorial(Object informacion) {
        Object[] datos = (Object[])informacion;
        adaptadorHistorial.eliminarHistorial((String)datos[0],(String)datos[1]);
    }

    @Override
    public void guardarLocalizacion(Object[] informacion) {
        int tarea = (int) informacion[3];
        LatLng miLatlng = new LatLng((Double)informacion[1],(Double)informacion[2]);
        switch (tarea){
            case 0:
                Log.i(TAG,"agregando posicion en Modelo");
                String idUser = auth.getCurrentUser().getUid();
                informacion[0] = idUser;
                adaptadorPosicion.agregarPosicion(informacion);
                break;
            case 1:
                Log.i(TAG,"traduciendo posicion en Modelo");
                adaptadorPosicion.traducirLatlng(miLatlng);
                break;
        }

    }

    @Override
    public void actualizarLocalizacion(Object informacion){
        adaptadorPosicion.actualizarPosicion((Object[])informacion);
    }

    @Override
    public void obtenerPeticionesDePasajeros(Object informacion) {
        Object[] datos = (Object[])informacion;
        int tarea = (int)datos[0];
        switch(tarea){
            case 0:
                adaptadorVinculo.obtenerListaVinculos((String)datos[1]);
                break;
            case 1:
                adaptadorVinculo.obtenerListaVinculosConductores((String)datos[1]);
                break;
            case 2:
                adaptadorVinculo.obtenerListaVinculosOTG((String) datos[1]);
        }
    }

    @Override
    public void aceptarPasajero(Object informacion) {
        adaptadorVinculo.concretarVinculo((Object[])informacion);
    }

    @Override
    public void rechazarPasajero(Object informacion) {
        adaptadorVinculo.eliminarVinculo((Object[])informacion);
    }

    @Override
    public void pararRuta(Object informacion) {
        adaptadorPosicion.finalizarGps();
        Object[] datos = (Object[])informacion;
        int tarea = (int)datos[0];
        switch (tarea){
            case 0:
                Object[] info = new Object[]{2,"",datos[1]};
                adaptadorVinculo.eliminarVinculo(info);
                break;
            case 1:
                Vinculo v = (Vinculo)datos[1];
                Object[] vinculo = new Object[]{1,v.getIdPasajero(),v.getIdConductor()};
                adaptadorVinculo.eliminarVinculo(vinculo);
                Object[] vin = new Object[]{2,"",v.getIdConductor()};
                adaptadorVinculo.eliminarVinculo(vin);
                break;

        }
        //adaptadorHistorial.agregarHistorial(informacion);
    }

    @Override
    public void obtenerConductoresEnRuta(Object informacion){
        adaptadorVinculo.obtenerConductoresEnRuta((String)informacion);
    }

    @Override
    public void obtenerRespuestaConductor(Object informacion) {
        Object[] vinculo = (Object[])informacion;
        String idPasajero = (String)vinculo[0];
        String idConductor = (String)vinculo[1];
        adaptadorVinculo.obtenerRespuestaVinculos(idPasajero,idConductor);
    }

    @Override
    public void agregarHistorial(Object informacion){
        adaptadorHistorial.agregarHistorial(informacion);
    }
}
