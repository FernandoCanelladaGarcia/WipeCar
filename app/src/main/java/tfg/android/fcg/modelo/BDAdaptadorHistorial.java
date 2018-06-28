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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorHistorial {

    private AppMediador appMediador;
    private DatabaseReference reference;
    private Historial historial;

    private final String TAG = "depurador";

    public BDAdaptadorHistorial(){
        appMediador = AppMediador.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("historial");
    }

    /**
     * Añade a la tabla Historial, un historial relacionado
     * con un pasajero y un conductor cuando finaliza su vínculo.
     * @param informacion contendra:
     */
    public void agregarHistorial(Object[] informacion){
        //Informacion: 0 = idUserPasajero, 1 = idUserConductor, 2 fecha, 3 hora, 4 origen 5 destino
        String idPasajero = (String)informacion[0];
        String idConductor = (String)informacion[1];
        String fecha = (String)informacion[2];
        String hora = (String)informacion[3];
        String origen = (String)informacion[4];
        String destino = (String)informacion[5];
        //TODO: NUEVOS VALORES, REDACCION
        String nombrePasajero = (String)informacion[6];
        String nombreConductor = (String)informacion[7];

        historial = new Historial(idPasajero,idConductor,fecha,hora,origen,destino, nombreConductor,nombrePasajero);

        reference.setValue(historial).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Se ha agregado historial de pasajero a conductor
                    Log.i(TAG, "Agregado historial");
                    Bundle extras = new Bundle();

                    extras.putBoolean(AppMediador.CLAVE_CREACION_HISTORIAL, true);
                    appMediador.sendBroadcast(AppMediador.AVISO_CREACION_HISTORIAL, extras);
                } else {
                    //Error a la hora de agregar historial
                    Log.i(TAG, "Error a la hora de agregar historial");
                    Bundle extras = new Bundle();

                    extras.putBoolean(AppMediador.CLAVE_CREACION_HISTORIAL, false);
                    appMediador.sendBroadcast(AppMediador.AVISO_CREACION_HISTORIAL, extras);
                }
            }
        });

    }

    /**
     * Elimina de la tabla Historial, un historial determiando de un usuario,
     * especificado en el parámetro idPasajero, idConductor.
     * @param idPasajero  contendra:
     * @param idConductor contendra:
     */
    public void eliminarHistorial(final String idPasajero, final String idConductor){

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Historial historial = snapshot.getValue(Historial.class);

                    if (historial.getIdPasajero().equals(idPasajero) && historial.getIdConductor().equals(idConductor)){
                        snapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //Eliminacion satisfactoria
                                    Bundle extras = new Bundle();
                                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINAR_HISTORIAL,true);

                                    appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_HISTORIAL,extras);
                                }else{
                                    //Error eliminando
                                    Bundle extras = new Bundle();
                                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINAR_HISTORIAL,false);

                                    appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_HISTORIAL, extras);
                                }
                            }
                        });
                    }
                }
                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error a la hora de coger el valor de la tabla historial
                Bundle extras = new Bundle();
                extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINAR_HISTORIAL,false);

                appMediador.sendBroadcast(AppMediador.AVISO_ELIMINAR_HISTORIAL, extras);
            }
        });
    }

    /**
     * Busca en la tabla Historial, el historial referido al identificador
     * que coincide con el parámetro idUser.
     * @param idUser contendra:
     */
    public void obtenerHistorial(final String idUser){
        final ArrayList<Historial> listaHistorial = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "dentro de tabla Historial");
                Log.i(TAG, "numero de historiales del usuario: " + dataSnapshot.getChildrenCount());

                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Historial historial = snapshot.getValue(Historial.class);
                    if(historial.getIdPasajero().equals(idUser) || historial.getIdConductor().equals(idUser)){
                        //Si coincide añadimos a la lista
                        listaHistorial.add(historial);
                    }
                }

                if(!listaHistorial.isEmpty()){
                    Collections.sort(listaHistorial, new Comparator<Historial>() {
                        @Override
                        public int compare(Historial o1, Historial o2) {
                            return o1.getFecha().compareTo(o2.getFecha());
                        }
                    });
                }

                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_HISTORIAL, listaHistorial);
                appMediador.sendBroadcast(AppMediador.AVISO_HISTORIAL,extras);

                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error a la hora de recoger el historial.
                ArrayList<Historial> listaHistorial = null;

                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_HISTORIAL, listaHistorial);
                appMediador.sendBroadcast(AppMediador.AVISO_HISTORIAL,extras);

                reference.removeEventListener(this);
            }
        });
    }

    //TODO: EDITAR ERROR A LA HORA DE AGREGAR VALORACION
    //AGREGA VALORACION A AMBOS USUARIOS CUANDO DEBERIA AGREGAR VALORACION
    //EN LA TABLA DE USUARIOS, SOBRE EL USUARIO AL QUE VALORA.
    //NO HAY QUE ACCEDER A HISTORIAL, SINO A USUARIO.

    public void agregarValoracion(String[] informacion){
    //Informacion => 0 idPasajero, 1 idConductor, 2 valorPasajero, 3 valorConductor.
        final String idPasajero = informacion[0];
        final String idConductor = informacion[1];
        final String valoracionPasajero = informacion[2];
        final String valoracionConductor = informacion[3];

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG,"obteniendo historial");
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Historial historial = snapshot.getValue(Historial.class);

                    if (historial.getIdPasajero().equals(idPasajero) && historial.getIdConductor().equals(idConductor)){
                       //Encontrado Historial Buscado
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("valoracionPasajero",valoracionPasajero);
                        taskMap.put("valoracionConductor",valoracionConductor);

                        dataSnapshot.getRef().updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    //Actualizada la valoracion
                                    Bundle extras = new Bundle();
                                    extras.putBoolean(AppMediador.CLAVE_VALORACION,true);

                                    appMediador.sendBroadcast(AppMediador.AVISO_VALORACION, extras);
                                }else{
                                    //Error actualizando valoracion
                                    Bundle extras = new Bundle();
                                    extras.putBoolean(AppMediador.CLAVE_VALORACION,false);

                                    appMediador.sendBroadcast(AppMediador.AVISO_VALORACION, extras);
                                }
                            }
                        });
                    }
                }

                reference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error a la hora de resolver la tabla historial
                Bundle extras = new Bundle();
                extras.putBoolean(AppMediador.CLAVE_VALORACION,false);

                appMediador.sendBroadcast(AppMediador.AVISO_VALORACION, extras);

                reference.removeEventListener(this);

            }
        });

    }
}
