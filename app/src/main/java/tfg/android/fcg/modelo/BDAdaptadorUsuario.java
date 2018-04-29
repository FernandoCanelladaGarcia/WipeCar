package tfg.android.fcg.modelo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorUsuario {

    private AppMediador appMediador;
    private FirebaseAuth auth;
    private FirebaseUser usuarioActual;
    private Usuario usuario;
    private Login login;
    private String finalDisplayName = null;

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
        final String telefono = (String) informacion [3];
        final String origen = (String) informacion[4];
        boolean rol = false;
        final String displayName = nombre+"#"+telefono+"#"+rol;

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
                                //Creamos el Login del usuario
                                login = new Login(idUser,nombreEncriptado,email,telefono,false);
                                //Guardamos en la tabla el usuario
                                usuario = new Usuario(idUser,nombreEncriptado,"",origen,"","","",null);
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
    public void actualizarUsuario(final Object[] informacion){
        //INFORMACION USUARIO = nombre, telefono, direccion, email, password, origen, destino, rol, datovehiculo, fecha, hora, valoracion
        final Object[] datos = new Object[2];

        String currentDisplayName = auth.getCurrentUser().getDisplayName();
        String[] partes = currentDisplayName.split("#");
        String currentName = partes[0];
        String currentPhone = partes[1];
        boolean currentRol = Boolean.valueOf(partes[3]);
        boolean newRol = Boolean.valueOf((String)informacion[7]);

        //igual displayname
        if(currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol == newRol){
            finalDisplayName = currentDisplayName;
        //distinto rol
        }if(currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol != newRol){
            finalDisplayName = currentName + "#" + currentPhone + "#" + informacion[7];
            login.setRol((Boolean)informacion[7]);
        //distinto telefono
        }if(currentName.equals(informacion[0]) && !currentPhone.equals(informacion[1]) && currentRol == newRol){
            finalDisplayName = currentName + "#" + informacion[1] + "#"+ currentRol;
            login.setTelefono((String)informacion[1]);
        //distinto nombre
        }if(!currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol == newRol){
            finalDisplayName = informacion[0]+ "#" +currentPhone+ "#" +currentRol;
            login.setNombre((String)informacion[0]);
        //distinto rol y telefono
        }if(currentName.equals(informacion[0]) && !currentPhone.equals(informacion[1]) && currentRol != newRol){
            finalDisplayName = currentName+ "#" +informacion[1]+ "#" +informacion[7];
            login.setTelefono((String)informacion[1]);
            login.setRol((Boolean)informacion[7]);
        //distinto nombre y rol
        }if(!currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol != newRol){
            finalDisplayName = informacion[0]+ "#" +currentPhone+ "#" +informacion[7];
            login.setNombre((String)informacion[0]);
            login.setRol((Boolean)informacion[7]);
        //distinto nombre y telefono
        }if(!currentName.equals(informacion[0]) && !currentPhone.equals(informacion[1]) && currentRol == newRol) {
            finalDisplayName = informacion[0] + "#" + informacion[1] + "#" + currentRol;
            login.setNombre((String)informacion[0]);
            login.setTelefono((String)informacion[1]);
        //Distinto displayName
        }else{
            finalDisplayName = informacion[0] + "#" + informacion[1] + "#" + informacion[7];
            login.setNombre((String)informacion[0]);
            login.setTelefono((String)informacion[1]);
            login.setRol((Boolean)informacion[7]);
        }

        final String correoActual = auth.getCurrentUser().getEmail();
        String password = (String) informacion[4];
        AuthCredential credential = EmailAuthProvider.getCredential(correoActual,password);

        usuarioActual.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    UserProfileChangeRequest actualizacion = new UserProfileChangeRequest.Builder().setDisplayName(finalDisplayName).build();
                    usuarioActual.updateProfile(actualizacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Actualizacion del Display Name en tabla Login
                            if(task.isSuccessful()){
                                String idUsuario = usuario.getIdUser();
                                DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(idUsuario);
                                Map<String, Object> taskMap = new HashMap<>();
                                if (!informacion[0].toString().isEmpty()) {
                                    taskMap.put("nombre", informacion[0]);
                                    usuario.setNombre((String) informacion[0]);
                                }
                                if (!informacion[5].toString().isEmpty()) {
                                    taskMap.put("origen", informacion[5]);
                                    usuario.setOrigen((String) informacion[5]);
                                }
                                if (!informacion[6].toString().isEmpty()) {
                                    taskMap.put("destino", informacion[6]);
                                    usuario.setDestino((String) informacion[6]);
                                }
                                if (!informacion[8].toString().isEmpty()) {
                                    taskMap.put("datoVehiculo", informacion[8]);
                                    usuario.setDatoVehiculo(informacion[8]);
                                }
                                if (!informacion[9].toString().isEmpty()) {
                                    taskMap.put("fecha", informacion[9]);
                                    usuario.setFecha((String) informacion[9]);
                                }
                                if (!informacion[10].toString().isEmpty()) {
                                    taskMap.put("hora", informacion[10]);
                                    usuario.setHora((String) informacion[10]);
                                }
                                if (!informacion[11].toString().isEmpty()) {
                                    taskMap.put("valoracion", informacion[11]);
                                    usuario.setValoracion((String) informacion[11]);
                                }

                                referenciaUsuario.updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Actualizado usuario en tabla usuarios
                                            if (!informacion[3].equals(correoActual)) {
                                                login.setEmail((String) informacion[3]);
                                                usuarioActual.updateEmail((String) informacion[3]).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Correo Actualizado, actualizacion completa.
                                                            datos[0] = true;
                                                            datos[1] = null;

                                                            Bundle extras = new Bundle();
                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                                        } else {
                                                            //Error actualizacion correo
                                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_CORREO;

                                                            datos[0] = false;
                                                            datos[1] = error;

                                                            Bundle extras = new Bundle();
                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                                        }
                                                    }
                                                });
                                            } else {
                                                //No se actualiza el correo, termina la actualizacion aqui
                                                datos[0] = true;
                                                datos[1] = null;

                                                Bundle extras = new Bundle();
                                                extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                            }
                                        } else {
                                            //Error a la hora de actualizar los parametros del usuario en la base de datos
                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_PARAMETROS;
                                            datos[0] = false;
                                            datos[1] = error;
                                            Bundle extras = new Bundle();
                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                        }
                                    }
                                });

                            } else {
                                //Error de actualizacion del DisplayName
                                String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_INFO;
                                datos[0] = false;
                                datos[1] = error;
                                Bundle extras = new Bundle();
                                extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                            }
                        }
                    });
                } else {
                    //Error reautenticacion
                    String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_REAUTENTICACION;
                    datos[0] = false;
                    datos[1] = error;
                    Bundle extras = new Bundle();
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                }
            }
        });

    }

    /**
     * Elimina de la tabla Usuarios el registro donde el campo coincide con el del parámetro.
     * @param idUser contendra:
     */
    public void eliminarUsuario(String idUser){

    }
}
