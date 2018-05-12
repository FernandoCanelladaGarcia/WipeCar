package tfg.android.fcg.modelo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorVinculo {

    private AppMediador appMediador;
    private DatabaseReference reference;
    private Vinculo vinculo;

    private final String TAG = "depurador";

    public BDAdaptadorVinculo() {
        appMediador = AppMediador.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("vinculo");

    }

    /**
     * Añade a la tabla Vínculo, un vínculo determinado entre pasajero y conductor.
     *
     * @param informacion contendra:
     */
    public void agregarVinculo(Object[] informacion) {
        //Informacion: 0 = idUserPasajero, 1 = idUserConductor, 2 fecha, 3 hora, 4 origen 5 destino, 6 tarea
        int tarea = (int) informacion[6];
        String idPasajero = (String) informacion[0];
        String idConductor = (String) informacion[1];
        String fecha = (String) informacion[2];
        String hora = (String) informacion[3];
        String origen = (String) informacion[4];
        String destino = (String) informacion[5];
        switch (tarea) {
            //PickUp
            case 0:
                vinculo = new Vinculo(idPasajero, idConductor, false, fecha, hora, origen, destino);

                reference.setValue(vinculo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Se ha agregado vinculo de pasajero a conductor
                            Log.i(TAG, "Agregado vinculo");
                            Bundle extras = new Bundle();

                            extras.putBoolean(AppMediador.CLAVE_CREACION_VINCULO, true);
                            appMediador.sendBroadcast(AppMediador.AVISO_CREACION_VINCULO, extras);
                        } else {
                            //Error a la hora de agregar vinculo
                            Log.i(TAG, "Error a la hora de agregar vinculo");
                            Bundle extras = new Bundle();

                            extras.putBoolean(AppMediador.CLAVE_CREACION_VINCULO, false);
                            appMediador.sendBroadcast(AppMediador.AVISO_CREACION_VINCULO, extras);
                        }
                    }
                });
                break;
            case 1:
                vinculo = new Vinculo(idPasajero, idConductor, false, fecha, hora, origen, destino);

                reference.setValue(vinculo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Se ha agregado vinculo de pasajero a conductor
                            Log.i(TAG, "Agregado vinculo");
                            Bundle extras = new Bundle();
                            extras.putSerializable(AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR, vinculo);
                            appMediador.sendBroadcast(AppMediador.AVISO_PETICION_OTGCONDUCTOR, extras);
                        } else {
                            //Error a la hora de agregar vinculo
                            Log.i(TAG, "Error a la hora de agregar vinculo");
                            Bundle extras = new Bundle();
                            extras.putSerializable(AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR, null);
                            appMediador.sendBroadcast(AppMediador.AVISO_PETICION_OTGCONDUCTOR, extras);
                        }
                    }
                });
            default:
                Log.i(TAG, "Error, no se ha introducido tipo de tarea");
                return;

        }

    }

    /**
     * Actualiza en la tabla Vínculo, un vínculo determinado entre pasajero y conductor.
     *
     * @param informacion contiene:
     */
    public void concretarVinculo(final Object[] informacion) {
        //INT INFORMACION 0 = 0=> PICK UP, 0 = 1 => OTG.
        int tarea = (int) informacion[0];
        final String idPasajero = (String) informacion[1];
        final String idConductor = (String) informacion[2];

        switch (tarea) {
            //PICK UP
            case 0:
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            vinculo = snapshot.getValue(Vinculo.class);
                            if (vinculo.getIdPasajero().equals(idPasajero) && vinculo.getIdConductor().equals(idConductor)) {
                                Map<String, Object> task = new HashMap<>();
                                task.put("vinculo", true);
                                dataSnapshot.getRef().updateChildren(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Creado vinculo
                                            Bundle extras = new Bundle();
                                            extras.putBoolean(AppMediador.CLAVE_CONCRETAR_VINCULO, true);
                                            appMediador.sendBroadcast(AppMediador.AVISO_CONCRETAR_VINCULO, extras);
                                        } else {
                                            //No se ha concretado vinculo
                                            Bundle extras = new Bundle();
                                            extras.putBoolean(AppMediador.CLAVE_CONCRETAR_VINCULO, false);
                                            appMediador.sendBroadcast(AppMediador.AVISO_CONCRETAR_VINCULO, extras);
                                        }
                                    }
                                });
                            }
                        }
                        reference.removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //No se ha encontrado referencia
                        Bundle extras = new Bundle();
                        extras.putBoolean(AppMediador.CLAVE_CONCRETAR_VINCULO, false);
                        appMediador.sendBroadcast(AppMediador.AVISO_CONCRETAR_VINCULO, extras);
                        reference.removeEventListener(this);
                    }
                });
                break;

            //OTG
            case 1:
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            vinculo = snapshot.getValue(Vinculo.class);
                            if (vinculo.getIdPasajero().equals(idPasajero) && vinculo.getIdConductor().equals(idConductor)) {
                                Map<String, Object> task = new HashMap<>();
                                task.put("vinculo", true);
                                dataSnapshot.getRef().updateChildren(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Creado vinculo
                                            Bundle extras = new Bundle();
                                            extras.putSerializable(AppMediador.CLAVE_ACEPTAR_PETICION_OTGCONDUCTOR, vinculo);
                                            appMediador.sendBroadcast(AppMediador.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR, extras);
                                        } else {
                                            //No se ha concretado vinculo
                                            Bundle extras = new Bundle();
                                            extras.putSerializable(AppMediador.CLAVE_ACEPTAR_PETICION_OTGCONDUCTOR, null);
                                            appMediador.sendBroadcast(AppMediador.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR, extras);
                                        }
                                    }
                                });
                            }
                        }
                        reference.removeEventListener(this);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //No se ha encontrado referencia
                        Bundle extras = new Bundle();
                        extras.putSerializable(AppMediador.CLAVE_ACEPTAR_PETICION_OTGCONDUCTOR, null);
                        appMediador.sendBroadcast(AppMediador.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR, extras);
                        reference.removeEventListener(this);
                    }
                });
                break;
            default:
                Log.i(TAG, "Error, no se ha introducido tipo de tarea");
                return;
        }
    }

    /**
     * Elimina de la tabla Vínculo, un vínculo determinado entre un pasajero y un conductor.
     *
     * @param informacion contiene:
     */
    public void eliminarVinculo(final Object[] informacion) {
        //INT INFORMACION 0 = 0=> PICK UP, 0 = 1 => OTG.
        int tarea = (int) informacion[0];

        final String idPasajero = (String) informacion[0];
        final String idConductor = (String) informacion[1];
        switch (tarea) {
            case 0:
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            vinculo = snapshot.getValue(Vinculo.class);

                            if (vinculo.getIdPasajero().equals(idPasajero) && vinculo.getIdConductor().equals(idConductor)) {
                                snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Se ha completado la eliminacion
                                            Bundle extras = new Bundle();

                                            extras.putBoolean(AppMediador.CLAVE_ELIMINAR_VINCULO, true);
                                            appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_VINCULO, extras);
                                        } else {
                                            //No ha eliminado el vinculo
                                            Bundle extras = new Bundle();

                                            extras.putBoolean(AppMediador.CLAVE_ELIMINAR_VINCULO, false);
                                            appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_VINCULO, extras);
                                        }
                                    }
                                });
                            }
                        }
                        reference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //No ha encontrado vinculo
                        Bundle extras = new Bundle();

                        extras.putBoolean(AppMediador.CLAVE_ELIMINAR_VINCULO, false);
                        appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_VINCULO, extras);

                        reference.removeEventListener(this);
                    }
                });
                break;
            case 1:
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            vinculo = snapshot.getValue(Vinculo.class);

                            if (vinculo.getIdPasajero().equals(idPasajero) && vinculo.getIdConductor().equals(idConductor)) {
                                snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            //Se ha completado la eliminacion
                                            Bundle extras = new Bundle();

                                            extras.putString(AppMediador.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR, vinculo.getIdConductor());
                                            appMediador.sendBroadcast(AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR, extras);
                                        } else {
                                            //No ha eliminado el vinculo
                                            Bundle extras = new Bundle();

                                            extras.putBoolean(AppMediador.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR, false);
                                            appMediador.sendBroadcast(AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR, extras);
                                        }
                                    }
                                });
                            }
                        }
                        reference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //No ha encontrado vinculo
                        Bundle extras = new Bundle();

                        extras.putBoolean(AppMediador.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR, false);
                        appMediador.sendBroadcast(AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR, extras);

                        reference.removeEventListener(this);
                    }
                });
                break;
            default:
                Log.i(TAG, "Error, no se ha introducido tipo de tarea");
                return;
        }

    }

    /**
     * Busca en la tabla Vínculo, los pasajeros que coinciden en el atributo idConductor, el valor pasado por parámetro.
     *
     * @param idConductor contendra:
     */
    public void obtenerListaPasajeros(final String idConductor) {
        final ArrayList<Usuario> pasajeros = new ArrayList<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vinculo vinculo = snapshot.getValue(Vinculo.class);
                    if (vinculo.getIdConductor().equals(idConductor)) {

                        //Se envia a OTGConductor
                        Bundle extras = new Bundle();
                        extras.putSerializable(AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR, vinculo);
                        appMediador.sendBroadcast(AppMediador.AVISO_PETICION_OTGCONDUCTOR, extras);

                        final DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference().child("usuarios");
                        referenciaUsuario.child(vinculo.getIdPasajero()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Usuario pasajero = dataSnapshot.getValue(Usuario.class);
                                pasajeros.add(pasajero);
                                referenciaUsuario.removeEventListener(this);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //Error recibiendo referencias de la tabla usuario
                                Bundle extras = new Bundle();
                                extras.putSerializable(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO, null);
                                appMediador.sendBroadcast(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO, extras);
                                referenciaUsuario.removeEventListener(this);
                            }
                        });
                    }
                }
                if (!pasajeros.isEmpty()) {
                    //Se han agregado a la lista todos los usuarios pasajeros del conductor referencia.
                    Bundle extras = new Bundle();
                    extras.putSerializable(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO, pasajeros);
                    appMediador.sendBroadcast(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO, extras);
                    reference.removeEventListener(this);
                } else {
                    //No hay peticiones ni pasajeros con vinculo.
                    Bundle extras = new Bundle();
                    extras.putSerializable(AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR, null);
                    appMediador.sendBroadcast(AppMediador.AVISO_PETICION_OTGCONDUCTOR, extras);

                    extras.putSerializable(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO, null);
                    appMediador.sendBroadcast(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO, extras);
                    reference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error recibiendo la referencia de la tabla vinculo y error para Peticiones
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO, null);
                appMediador.sendBroadcast(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO, extras);

                extras.putSerializable(AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR, null);
                appMediador.sendBroadcast(AppMediador.AVISO_PETICION_OTGCONDUCTOR, extras);

                reference.removeEventListener(this);
            }
        });
    }
}
