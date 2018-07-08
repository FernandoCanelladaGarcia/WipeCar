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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorUsuario {

    private AppMediador appMediador;
    private FirebaseAuth auth;
    private FirebaseUser usuarioActual;
    private Usuario usuario;
    private Login login;
    private String finalDisplayName = "";
    private SharedPreferences sharedPreferences;
    private String newDisplayName = "";
    private final String TAG = "depurador";
    private DatabaseReference database;
    private Modelo modelo;

    public BDAdaptadorUsuario() {
        appMediador = AppMediador.getInstance();
        auth = FirebaseAuth.getInstance();
        sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        database = FirebaseDatabase.getInstance().getReference().child("usuarios");
    }

    /**
     * Busca en la tabla Usuarios aquel registro que coincida con el parámetro
     * @param idUser
     */
    public void obtenerUsuario(String idUser){
        database.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_USUARIO,usuario);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_USUARIO,extras);
                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_USUARIO,null);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_USUARIO,extras);
                database.removeEventListener(this);
            }
        });
    }

    //TODO: NUEVO, REDACCION

    /**
     * Metodo al que se recurre en caso de una mala actualización, desinstalación de la aplicación
     * o pérdida de cache para que no se sucedan errores debido a la falta de datos.
     * @param idUser
     */
    public void obtenerSharedPreferences(String idUser){
        database.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("origendef",usuario.getOrigenDef());
                editor.putBoolean("rol",usuario.isRol());
                editor.putString("idUser",usuario.getIdUser());
                editor.apply();
                Log.i(TAG,"SHARED PREFERENCES ACTUALIZADO");
                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                database.removeEventListener(this);
            }
        });
    }

    /**
     * Busca en la tabla Usuarios aquellos registros que coincidan con el parámetro.
     *
     * @param destino contendra:
     */
    public void obtenerListaConductores(final String destino) {
        //TODO comprobar rol
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Usuario> conductores = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if (usuario.getDestino().equals(destino) && usuario.isRol()) {
                        conductores.add(usuario);
                    }
                }
                Log.i(TAG, String.valueOf(conductores.size()));
                //Se ha creado la lista de conductores que van al destino
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_LISTA_CONDUCTORES, conductores);
                appMediador.sendBroadcast(AppMediador.AVISO_LISTA_CONDUCTORES, extras);
                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error en la referencia de la base de datos
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_LISTA_CONDUCTORES, null);
                appMediador.sendBroadcast(AppMediador.AVISO_LISTA_CONDUCTORES, extras);
                database.removeEventListener(this);
            }
        });
    }

    /**
     * Busca en la tabla Usuarios aquellos registros que coincidan con el parámetro.
     *
     * @param destino contendra:
     */
    public void obtenerListaPasajeros(final String destino) {
        //TODO comprobar rol
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Usuario> pasajeros = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if (usuario.getDestino().equals(destino) && !usuario.isRol()) {
                        Log.i(TAG,"Pasajero");
                        pasajeros.add(usuario);
                    }
                }
                Log.i(TAG, String.valueOf(pasajeros.size()));
                //Se ha creado la lista de pasajeros que van al destino
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_LISTA_PASAJEROS, pasajeros);
                appMediador.sendBroadcast(AppMediador.AVISO_LISTA_PASAJEROS, extras);
                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error en la referencia de la base de datos
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_LISTA_PASAJEROS, null);
                appMediador.sendBroadcast(AppMediador.AVISO_LISTA_PASAJEROS, extras);
                database.removeEventListener(this);
            }
        });
    }

    /**
     * Busca en la tabla Usuarios aquel registro donde el campo idUser coincida con el parámetro.
     *
     * @param idUser contendra:
     */
    public void obtenerConductor(String idUser) {
        database.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Obtenido conductor
                usuario = dataSnapshot.getValue(Usuario.class);
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_CONDUCTOR, usuario);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_CONDUCTOR, extras);
                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error a la hora de obtener conductor
                usuario = null;
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_CONDUCTOR, usuario);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_CONDUCTOR, extras);
                database.removeEventListener(this);
            }
        });
    }

    /**
     * Busca en la tabla Usuarios aquel registro donde el campo idUser coincide con el parámetro.
     *
     * @param idUser contendra:
     */
    public void obtenerPasajero(String idUser) {
        database.child(idUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Obtenido Pasajero
                usuario = dataSnapshot.getValue(Usuario.class);

                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_PASAJERO, usuario);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_PASAJERO, extras);

                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error a la hora de obtener pasajero

                usuario = null;

                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_PASAJERO, usuario);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_PASAJERO, extras);

                database.removeEventListener(this);
            }
        });
    }

    /**
     * Añade un nuevo usuario a la tabla Usuarios.
     *
     * @param informacion contendra:
     */
    public void agregarUsuario(final Object[] informacion) {
        //TODO ENCRIPTAR
        //INFORMACION LOGIN = 0 email, 1 contraseña, 2 nombre
        //INFORMACION USUARIO = nombre, telefono, rol valoracion, origen, destino, fecha, hora, datovehiculo
        final String email = (String) informacion[0];
        final String password = (String) informacion[1];
        final String nombre = (String) informacion[2];
        final String telefono = (String) informacion[3];
        final String origen = (String) informacion[4];
        final boolean rol = false;
        final String displayName = nombre;

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //Creacion de usuario exitosa
                    Log.i(TAG, "Exito en la creacion del usuario");
                    usuarioActual = auth.getCurrentUser();
                    UserProfileChangeRequest actualizacion = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
                    usuarioActual.updateProfile(actualizacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i(TAG, "Exito en Actualizacion de Display Name de Usuario");
                                //Actualizacion de Display Name de Usuario.
                                //Guardamos la sesion del usuario
                                String idUser = usuarioActual.getUid();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.putString("origendef",origen);
                                editor.putBoolean("rol",rol);
                                editor.putString("idUser",idUser);
                                editor.apply();
                                //TODO ENCRIPTAR
                                String nombreEncriptado = nombre;
                                //Creamos el Login del usuario
                                login = new Login(idUser, nombreEncriptado, email);
                                //Guardamos en la tabla el usuario
                                usuario = new Usuario(idUser, nombreEncriptado, telefono, rol, "", "",origen, "", "", "", null);
                                DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(idUser);
                                referenciaUsuario.setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    //                                    referenciaUsuario.setValue(usuario, new DatabaseReference.CompletionListener() {
//                                        @Override
//                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                                            if(databaseError != null){
//                                                Log.i(TAG, databaseError.getMessage());
//                                            }
//                                        }
//                                    });
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.i(TAG, "Completado registro y almacenamiento en base de datos");
                                        if (task.isSuccessful()) {
                                            //Usuario correctamente registrado
                                            Bundle extras = new Bundle();
                                            extras.putBoolean(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO, true);
                                            appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO, extras);
                                        } else {
                                            Log.i(TAG, task.getException().toString());
                                            //Fallo en registro
                                            Bundle extras = new Bundle();
                                            extras.putBoolean(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO, false);
                                            appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO, extras);
                                        }
                                    }
                                });
                            } else {
                                Log.i(TAG, task.getException().toString());
                                //Fallo en actualizacion de DisplayName
                                Bundle extras = new Bundle();
                                extras.putBoolean(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO, false);
                                appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO, extras);
                            }
                        }
                    });
                } else {
                    Log.i(TAG, task.getException().toString());
                    //Fallo en la creacion del usuario
                    Bundle extras = new Bundle();
                    extras.putString(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO, "Usuario ya existente");
                    appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_USUARIO, extras);
                }
            }
        });
    }

    /**
     * Modifica la información de un determinado usuario de la tabla Usuarios.
     *
     * @param informacion contendra:
     */
    public void actualizarUsuario(final Object[] informacion) {
        //INFORMACION USUARIO = nombre, telefono, email, password, origen, destino, rol, datovehiculo, fecha, hora, valoracion

        String[] loginUsuario = new String[]{(String) informacion[1], (String) informacion[2], (String) informacion[3], (String) informacion[4], (String) informacion[7]};
        String[] origenDestino = new String[]{(String) informacion[5], (String) informacion[6]};
        String[] valoracion = new String[]{(String) informacion[11],(String)informacion[12]};
        String[] fechaYHora = new String[]{(String) informacion[9], (String) informacion[10]};
        String datoVehiculo = (String) informacion[8];

        int tarea = (int) informacion[0];

        switch (tarea) {
            case 1:
                Log.i(TAG, "actualizar origen y destino");
                actualizarOrigenYDestino(origenDestino);
                break;
            case 2:
                Log.i(TAG, "actualizar Login");
                actualizarLogin(loginUsuario);
                break;
            case 3:
                Log.i(TAG, "actualizar valoracion");
                actualizarValoracion(valoracion);
                break;
            case 4:
                Log.i(TAG, "actualizar hora y fecha");
                actualizarHoraYFecha(fechaYHora);
                break;
            case 5:
                Log.i(TAG, "actualizar datos vehiculo");
                actualizarDatosVehiculo(datoVehiculo);
            default:
                Log.i(TAG, "TAREA == " + informacion[0]);
                //return;
        }
//TODO EN CASO DE ALGUN PROBLEMA. CODIGO MUERTO DE LA SEPARACION DE ACTUALIZARUSUARIO

//        String currentDisplayName = auth.getCurrentUser().getDisplayName();
//        String[] partes = currentDisplayName.split("#");
//        String currentName = partes[0];
//        String currentPhone = partes[1];
//        final String password = sharedPreferences.getString("password", null);
//        final String correoActual = sharedPreferences.getString("email", null);
//        boolean currentRol = Boolean.valueOf(partes[2]);
//        boolean newRol = Boolean.valueOf((String) informacion[7]);
//
//        //igual displayname
//        if (currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol == newRol) {
//            finalDisplayName = currentDisplayName;
//            //distinto rol
//        }
//        if (currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol != newRol) {
//            finalDisplayName = currentName + "#" + currentPhone + "#" + informacion[7];
//            login.setRol((Boolean) informacion[7]);
//            //distinto telefono
//        }
//        if (currentName.equals(informacion[0]) && !currentPhone.equals(informacion[1]) && currentRol == newRol) {
//            finalDisplayName = currentName + "#" + informacion[1] + "#" + currentRol;
//            login.setTelefono((String) informacion[1]);
//            //distinto nombre
//        }
//        if (!currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol == newRol) {
//            finalDisplayName = informacion[0] + "#" + currentPhone + "#" + currentRol;
//            login.setNombre((String) informacion[0]);
//            //distinto rol y telefono
//        }
//        if (currentName.equals(informacion[0]) && !currentPhone.equals(informacion[1]) && currentRol != newRol) {
//            finalDisplayName = currentName + "#" + informacion[1] + "#" + informacion[7];
//            login.setTelefono((String) informacion[1]);
//            login.setRol((Boolean) informacion[7]);
//            //distinto nombre y rol
//        }
//        if (!currentName.equals(informacion[0]) && currentPhone.equals(informacion[1]) && currentRol != newRol) {
//            finalDisplayName = informacion[0] + "#" + currentPhone + "#" + informacion[7];
//            login.setNombre((String) informacion[0]);
//            login.setRol((Boolean) informacion[7]);
//            //distinto nombre y telefono
//        }
//        if (!currentName.equals(informacion[0]) && !currentPhone.equals(informacion[1]) && currentRol == newRol) {
//            finalDisplayName = informacion[0] + "#" + informacion[1] + "#" + currentRol;
//            login.setNombre((String) informacion[0]);
//            login.setTelefono((String) informacion[1]);
//            //Distinto displayName
//        } else {
//            finalDisplayName = informacion[0] + "#" + informacion[1] + "#" + informacion[7];
//            login.setNombre((String) informacion[0]);
//            login.setTelefono((String) informacion[1]);
//            login.setRol((Boolean) informacion[7]);
//        }
//
//        AuthCredential credential = EmailAuthProvider.getCredential(correoActual, password);
//        usuarioActual.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    UserProfileChangeRequest actualizacion = new UserProfileChangeRequest.Builder().setDisplayName(finalDisplayName).build();
//                    usuarioActual.updateProfile(actualizacion).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            //Actualizacion del Display Name en tabla Login
//                            if (task.isSuccessful()) {
//                                String idUsuario = usuario.getIdUser();
//                                DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(idUsuario);
//                                Map<String, Object> taskMap = new HashMap<>();
//                                if (!informacion[0].toString().isEmpty()) {
//                                    taskMap.put("nombre", informacion[0]);
//                                    usuario.setNombre((String) informacion[0]);
//                                }
//                                if (!informacion[5].toString().isEmpty()) {
//                                    taskMap.put("origen", informacion[5]);
//                                    usuario.setOrigen((String) informacion[5]);
//                                }
//                                if (!informacion[6].toString().isEmpty()) {
//                                    taskMap.put("destino", informacion[6]);
//                                    usuario.setDestino((String) informacion[6]);
//                                }
//                                if (!informacion[8].toString().isEmpty()) {
//                                    taskMap.put("datoVehiculo", informacion[8]);
//                                    usuario.setDatoVehiculo(informacion[8]);
//                                }
//                                if (!informacion[9].toString().isEmpty()) {
//                                    taskMap.put("fecha", informacion[9]);
//                                    usuario.setFecha((String) informacion[9]);
//                                }
//                                if (!informacion[10].toString().isEmpty()) {
//                                    taskMap.put("hora", informacion[10]);
//                                    usuario.setHora((String) informacion[10]);
//                                }
//                                if (!informacion[11].toString().isEmpty()) {
//                                    taskMap.put("valoracion", informacion[11]);
//                                    usuario.setValoracion((String) informacion[11]);
//                                }
//
//                                referenciaUsuario.updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            //Actualizado usuario en tabla usuarios
//                                            if (!informacion[3].equals(correoActual)) {
//                                                login.setEmail((String) informacion[3]);
//                                                usuarioActual.updateEmail((String) informacion[3]).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//                                                            if (!informacion[4].equals(password)) {
//                                                                usuarioActual.updatePassword((String) informacion[4]).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                    @Override
//                                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                                        if (task.isSuccessful()) {
//                                                                            //Correo Actualizado, contraseña actualizada actualizacion completa.
//                                                                            datos[0] = true;
//                                                                            datos[1] = null;
//
//                                                                            Bundle extras = new Bundle();
//                                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                                                                        } else {
//                                                                            //Error actualizacion contraseña
//                                                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_PASSWORD;
//
//                                                                            datos[0] = false;
//                                                                            datos[1] = error;
//
//                                                                            Bundle extras = new Bundle();
//                                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                                                                        }
//                                                                    }
//                                                                });
//                                                            } else {
//                                                                //No se actualiza la password, termina aqui.
//                                                                datos[0] = true;
//                                                                datos[1] = null;
//
//                                                                Bundle extras = new Bundle();
//                                                                extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                                                                appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                                                            }
//                                                        } else {
//                                                            //Error actualizacion correo
//                                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_CORREO;
//
//                                                            datos[0] = false;
//                                                            datos[1] = error;
//
//                                                            Bundle extras = new Bundle();
//                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                                                        }
//                                                    }
//                                                });
//                                            } else {
//                                                //No se actualiza el correo, termina la actualizacion aqui
//                                                datos[0] = true;
//                                                datos[1] = null;
//
//                                                Bundle extras = new Bundle();
//                                                extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                                                appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                                            }
//                                        } else {
//                                            //Error a la hora de actualizar los parametros del usuario en la base de datos
//                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_NOMBRE;
//                                            datos[0] = false;
//                                            datos[1] = error;
//                                            Bundle extras = new Bundle();
//                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                                        }
//                                    }
//                                });
//                            } else {
//                                //Error de actualizacion del DisplayName
//                                String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_INFO;
//                                datos[0] = false;
//                                datos[1] = error;
//                                Bundle extras = new Bundle();
//                                extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                                appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                            }
//                        }
//                    });
//                } else {
//                    //Error reautenticacion
//                    String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_REAUTENTICACION;
//                    datos[0] = false;
//                    datos[1] = error;
//                    Bundle extras = new Bundle();
//                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
//                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
//                }
//            }
//        });
    }

    /**
     * Elimina de la tabla Usuarios el registro donde el campo coincide con el del parámetro.
     * @param idUser   contendra:
     * @param email    contendra:
     * @param password contendra:
     */
    public void eliminarUsuario(String idUser, final String email, final String password) {
        usuarioActual = auth.getCurrentUser();
        database.child(idUser).removeValue();
        Log.i(TAG, "Eliminado registro de la base de datos");
        //Eliminado registro de la base de datos, notifica resultado
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        usuarioActual.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Reautenticado");
                    //Reautenticado
                    usuarioActual.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Eliminado correctamente.
                                Log.i(TAG, "Eliminado correctamente.");
                                Bundle extras = new Bundle();
                                extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINAR_USUARIO, true);
                                appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_USUARIO, extras);
                            } else {
                                //Fallo al eliminar al usuario
                                Log.i(TAG, "Fallo al eliminar auth");
                                Bundle extras = new Bundle();
                                extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINAR_USUARIO, false);
                                appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_USUARIO, extras);
                            }
                        }
                    });
                } else {
                    //Fallo al reautenticar
                    Log.i(TAG, "Fallo al reautenticar");
                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINAR_USUARIO, false);
                    appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_USUARIO, extras);
                }
            }
        });
    }

    public void cambiarRol(final boolean rol){
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("rol",rol);

        DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(usuarioActual.getUid());
        referenciaUsuario.updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i(TAG, "Actualizacion Rol");

                    SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("rol",rol);
                    editor.apply();

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_CAMBIO_ROL,true);
                    appMediador.sendBroadcast(AppMediador.AVISO_CAMBIO_ROL,extras);
                }else{
                    Log.i(TAG, "Error actualizacion Rol");
                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_CAMBIO_ROL,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_CAMBIO_ROL,extras);
                }
            }
        });
    }


    //**********METODOS PRIVADOS**************//

    private void actualizarLogin(final String[] informacion) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        usuarioActual = auth.getCurrentUser();
        final Object datos[] = new Object[2];
        final String currentPassword = sharedPreferences.getString("password", null);
        final String currentEmail = sharedPreferences.getString("email", null);
        final boolean currentRol = sharedPreferences.getBoolean("rol", false);
        final Map<String, Object> taskMap = new HashMap<>();

        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPassword);

        usuarioActual.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Reautenticacion correcta");

                    String currentName = auth.getCurrentUser().getDisplayName();

                    final String newName = informacion[0];
                    //final String newPhone = informacion[1];
                    final String newEmail = informacion[2];
                    final String newPassword = informacion[3];
                    final boolean newRol = Boolean.valueOf(informacion[4]);

                    if (currentName.equals(newName)) {
                        newDisplayName = currentName;
                        taskMap.put("nombre", currentName);
                    } else {
                        newDisplayName = newName;
                        taskMap.put("nombre", newName);
                    }

                    UserProfileChangeRequest actualizacion = new UserProfileChangeRequest.Builder().setDisplayName(newDisplayName).build();
                    usuarioActual.updateProfile(actualizacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //Actualizacion de DisplayName
                                Log.i(TAG, "Actualizacion display");
                                //Comprobamos el rol, sino no varia.
                                if (currentRol != newRol) {
                                    taskMap.put("rol", newRol);
                                    editor.putBoolean("rol", newRol);
                                }
                                DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(usuarioActual.getUid());
                                referenciaUsuario.updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Actualizacion name en tabla usuarios
                                            Log.i(TAG, "Actualizacion nombre");
                                            if (!currentPassword.equals(newPassword)) {
                                                editor.putString("password", newPassword);
                                                editor.apply();
                                                //TODO ENCRIPTAR
                                                usuarioActual.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Actualizacion de password
                                                            Log.i(TAG, "Actualizacion password");
                                                            datos[0] = true;
                                                            datos[1] = "LOGIN";
                                                            Bundle extras = new Bundle();
                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                                        } else {
                                                            //Error password
                                                            Log.i(TAG, "Error password");
                                                            Bundle extras = new Bundle();
                                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_PASSWORD;
                                                            datos[0] = false;
                                                            datos[1] = error;
                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                                        }
                                                    }
                                                });
                                            } else if (!currentEmail.equals(newEmail)) {
                                                editor.putString("email", newEmail);
                                                editor.apply();
                                                usuarioActual.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //Actualizacion de email
                                                            Log.i(TAG, "Actualizacion email");
                                                            Bundle extras = new Bundle();
                                                            datos[0] = true;
                                                            datos[1] = "LOGIN";
                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                                        } else {
                                                            //Error password
                                                            Log.i(TAG, "Error email");
                                                            Bundle extras = new Bundle();
                                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_CORREO;
                                                            datos[0] = false;
                                                            datos[1] = error;
                                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                                        }
                                                    }
                                                });
                                            } else {
                                                //Solo actualizacion displayName y la tabla usuarios
                                                Log.i(TAG, "Actualizacion solo display");
                                                Bundle extras = new Bundle();
                                                datos[0] = true;
                                                datos[1] = "LOGIN";
                                                extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                                appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                            }
                                        } else {
                                            //Error a la hora de actualizar el nombre en la base de usuarios.
                                            Log.i(TAG, "Error nombre");
                                            Bundle extras = new Bundle();
                                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_NOMBRE;
                                            datos[0] = false;
                                            datos[1] = error;
                                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                                        }
                                    }
                                });
                            } else {
                                //Error actualizacion displayName
                                Log.i(TAG, "Error display");
                                Bundle extras = new Bundle();
                                String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_INFO;
                                datos[0] = false;
                                datos[1] = error;
                                extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                                appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                            }
                        }
                    });
                } else {
                    Log.i(TAG, "Error reautenticacion");
                    //Error en reautenticacion
                    Bundle extras = new Bundle();
                    String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_REAUTENTICACION;
                    datos[0] = false;
                    datos[1] = error;
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                }
            }
        });
    }

    private void actualizarValoracion(String[] informacion) {
        String idUserValorar = informacion[0];
        final float valoracion = Float.parseFloat(informacion[1]);
        DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(idUserValorar);

        referenciaUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuario = dataSnapshot.getValue(Usuario.class);
                float valoracionActual = 0;
                if(usuario.getValoracion() != null){
                    float valoracionUsuario = Float.parseFloat(usuario.getValoracion());
                    valoracionActual = (valoracion + valoracionUsuario)/2;
                }else{
                    valoracionActual = valoracion;
                }
                dataSnapshot.getRef().child("valoracion").setValue(valoracionActual).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Actualizacion valoracion
                            Bundle extras = new Bundle();
                            Object[] datos = new Object[2];
                            datos[0] = true;
                            datos[1] = "valoracion";
                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                        } else {
                            //Error de actualizacion valoracion
                            Bundle extras = new Bundle();
                            Object[] datos = new Object[2];
                            String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_VALORACION;
                            datos[0] = true;
                            datos[1] = error;
                            extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                            appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //TODO DEBE LLAMARSE TRAS ACTUALIZAR O AGREGAR UN VEHICULO
    private void actualizarDatosVehiculo(String informacion) {
        final Object[] datos = new Object[2];
        usuarioActual = auth.getCurrentUser();
        String idUsuario = usuarioActual.getUid();
        DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(idUsuario);
        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("datoVehiculo", informacion);

        referenciaUsuario.updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Actualizacion dato vehiculo
                    Bundle extras = new Bundle();
                    datos[0] = true;
                    datos[1] = "datovehiculo";
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                } else {
                    //Error de actualizacion dato vehiculo
                    Bundle extras = new Bundle();
                    String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_DATOVEHICULO;
                    datos[0] = true;
                    datos[1] = error;
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                }
            }
        });
    }

    private void actualizarHoraYFecha(String[] informacion) {
        usuarioActual = auth.getCurrentUser();
        final Object[] datos = new Object[2];
        String idUsuario = usuarioActual.getUid();
        DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(idUsuario);

        Map<String, Object> taskMap = new HashMap<>();
        taskMap.put("fecha", informacion[0]);
        taskMap.put("hora", informacion[1]);

        referenciaUsuario.updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Actualizacion fecha y hora
                    Bundle extras = new Bundle();
                    datos[0] = true;
                    datos[1] = "fechayhora";
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                } else {
                    //Error de actualizacion Origen y Destino
                    Bundle extras = new Bundle();
                    String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_FECHAYHORA;
                    datos[0] = true;
                    datos[1] = error;
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                }
            }
        });

    }

    private void actualizarOrigenYDestino(String[] informacion) {
        usuarioActual = auth.getCurrentUser();
        final Object[] datos = new Object[2];
        String origen = informacion[0];
        String destino = informacion[1];
        DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios").child(usuarioActual.getUid());

        Map<String, Object> taskMap = new HashMap<>();
        if(!origen.equals("1")){
            taskMap.put("origen", informacion[0]);
        }
        taskMap.put("destino", informacion[1]);
        referenciaUsuario.updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Actualizacion Origen y Destino
                    Bundle extras = new Bundle();
                    datos[0] = true;
                    datos[1] = "origenydestino";
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                } else {
                    //Error de actualizacion Origen y Destino
                    Bundle extras = new Bundle();
                    String error = AppMediador.ERROR_ACTUALIZACION_USUARIO_ORIGENYDESTINO;
                    datos[0] = true;
                    datos[1] = error;
                    extras.putSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO, datos);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_USUARIO, extras);
                }
            }
        });
    }
}
