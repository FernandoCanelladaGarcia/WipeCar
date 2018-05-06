package tfg.android.fcg.modelo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorVinculo {

    private AppMediador appMediador;
    private DatabaseReference reference;
    private Vinculo vinculo;

    private final String TAG = "depurador";

    public BDAdaptadorVinculo(){
        appMediador = AppMediador.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("vinculo");

    }

    /**
     * Añade a la tabla Vínculo, un vínculo determinado entre pasajero y conductor.
     * @param informacion contendra:
     */
    public void agregarVinculo(Object[] informacion){
    //Informacion: 0 = idUserPasajero, 1 = idUserConductor, 2 fecha, 3 hora, 4 origen 5 destino
        String idPasajero = (String)informacion[0];
        String idConductor = (String)informacion[1];
        String fecha = (String)informacion[2];
        String hora = (String)informacion[3];
        String origen = (String)informacion[4];
        String destino = (String)informacion[5];

        vinculo = new Vinculo(idPasajero,idConductor, false, fecha,hora,origen,destino);

        reference.setValue(vinculo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Se ha agregado vinculo de pasajero a conductor
                    Log.i(TAG,"Agregado vinculo");
                    Bundle extras = new Bundle();

                    extras.putBoolean(AppMediador.CLAVE_CREACION_VINCULO,true);
                    appMediador.sendBroadcast(AppMediador.AVISO_CREACION_VINCULO,extras);
                }else{
                    //Error a la hora de agregar vinculo
                    Log.i(TAG,"Error a la hora de agregar vinculo");
                    Bundle extras = new Bundle();

                    extras.putBoolean(AppMediador.CLAVE_CREACION_VINCULO,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_CREACION_VINCULO,extras);
                }
            }
        });
    }

    /**
     * Actualiza en la tabla Vínculo, un vínculo determinado entre pasajero y conductor.
     * @param idPasajero contiene:
     * @param idConductor contiene:
     */
    public void concretarVinculo(String idPasajero, String idConductor){
    //TODO query vinculo, update taskmap vinculo = true
    }

    /**
     * Elimina de la tabla Vínculo, un vínculo determinado entre un pasajero y un conductor.
     * @param idPasajero contiene:
     * @param idConductor contiene:
     */
    public void eliminarVinculo(String idPasajero, String idConductor){
    //TODO query, delete
    }

    /**
     * Busca en la tabla Vínculo, los pasajeros que coinciden en el atributo idConductor, el valor pasado por parámetro.
     * @param idUser contendra:
     */
    public void obtenerListaPasajeros(String idUser){

    }
}
