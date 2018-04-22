package tfg.android.fcg.modelo;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import tfg.android.fcg.AppMediador;

public class Modelo implements IModelo{

    private static Modelo singleton = null;
    private FirebaseAuth auth;

    private Modelo(){
        auth = FirebaseAuth.getInstance();
    }

    public static Modelo getInstance(){
        if(singleton == null)
            singleton = new Modelo();
        return singleton;
    }
    
    @Override
    public void comprobarLogin(Object[] informacion) {
        auth.signInWithEmailAndPassword((String)informacion[0],(String)informacion[1]).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Usuario logged.
                    AppMediador.getInstance().sendBroadcast(AppMediador.AVISO_USER_LOGGED_OK,null);
                }
                else{
                    //Fallo en login
                    AppMediador.getInstance().sendBroadcast(AppMediador.AVISO_USER_LOGGED_FAIL,null);
                }
            }
        });
    }

    @Override
    public void recuperarPassword(Object informacion) {

    }

    @Override
    public void cambiarPassword(Object informacion) {

    }

    //Usa la clase Usuario
    @Override
    public void registrarUsuario(Object informacion) {

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
