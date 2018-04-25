package tfg.android.fcg.modelo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorUsuario {

    private AppMediador appMediador;
    private FirebaseAuth auth;
    private FirebaseUser usuarioActual;
    private Usuario usuario;

    private final String TAG = "depurador";

    public BDAdaptadorUsuario(){
        appMediador = AppMediador.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    /**
     * Busca en la tabla Usuarios aquellos registros que coincidan con el parámetro.
     * @param destino contendra:
     */
    public void obtenerListaConductores(String destino){

    }

    /**
     * Busca en la tabla Usuarios aquellos registros que coincidan con el parámetro.
     * @param destino contendra:
     */
    public void obtenerListaPasajeros(String destino){

    }

    /**
     * Busca en la tabla Usuarios aquel registro donde el campo idUser coincida con el parámetro.
     * @param idUser contendra:
     */
    public void obtenerConductor(String idUser){

    }

    /**
     * Busca en la tabla Usuarios aquel registro donde el campo idUser coincide con el parámetro.
     * @param idUser contendra:
     */
    public void obtenerPasajero(String idUser){

    }

    /**
     * Añade un nuevo usuario a la tabla Usuarios.
     * @param informacion contendra:
     */
    public void agregarUsuario(final Object[] informacion){
        //INFORMACION LOGIN = 0 email, 1 contraseña, 2 nombre, 3 telefono, 4 direccion, modo por defecto pasajero = false
        //INFORMACION USUARIO = nombre, valoracion, origen, destino, fecha, hora, datovehiculo
        final String email = (String) informacion[0];
        String password = (String) informacion[1];
        final String nombre = (String) informacion [2];
        String telefono = (String) informacion [3];
        boolean rol = false;

        final String displayName = nombre+"#"+rol;

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Creacion de usuario exitosa
                    Log.i(TAG, "Exito en la creacion del usuario");
                    usuarioActual = auth.getCurrentUser();
                    UserProfileChangeRequest actualizacion = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
                    usuarioActual.updateProfile(actualizacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            //Actualizacion de Display Name de Usuario.
                            //Guardamos la sesion del usuario
                                SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.apply();

                                String nombreEncriptado = nombre;
                                String idUser = usuarioActual.getUid();
                                //Guardamos en la tabla el usuario
                                usuario = new Usuario(idUser,nombreEncriptado,"","","","","",null);
                                DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(idUser);
                                referenciaUsuario.setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            //Usuario correctamente registrado
                                            Bundle extras = new Bundle();
                                            extras.putBoolean(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO,true);
                                            appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO,extras);
                                        }
                                            //Fallo en registro
                                        Bundle extras = new Bundle();
                                        extras.putBoolean(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO,false);
                                        appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO,extras);
                                    }
                                });
                            }
                            //Fallo en actualizacion de DisplayName
                            Bundle extras = new Bundle();
                            extras.putBoolean(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO,false);
                            appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO,extras);
                        }
                    });
                }
                //Fallo en la creacion del usuario
                Bundle extras = new Bundle();
                extras.putBoolean(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO,false);
                appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO,extras);
            }
        });
    }

    /**
     * Modifica la información de un determinado usuario de la tabla Usuarios.
     * @param informacion contendra:
     */
    public void actualizarUsuario(Object informacion){

    }

    /**
     * Elimina de la tabla Usuarios el registro donde el campo coincide con el del parámetro.
     * @param idUser contendra:
     */
    public void eliminarUsuario(String idUser){

    }
}
