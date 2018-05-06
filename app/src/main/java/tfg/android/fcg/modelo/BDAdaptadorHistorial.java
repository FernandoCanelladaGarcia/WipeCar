package tfg.android.fcg.modelo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        historial = new Historial(idPasajero,idConductor,fecha,hora,origen,destino,"", "");

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
     * especificado en el parámetro idUser.
     * @param idUser contendra:
     */
    public void eliminarHistorial(String idUser){

    }

    /**
     * Busca en la tabla Historial, el historial referido al identificador
     * que coincide con el parámetro idUser.
     * @param idUser contendra:
     */
    public void obtenerHistorial(String idUser){

    }

    public void agregarValoracion(String[] informacion){
    //Informacion => 0 idPasajero, 1 idConductor, 2 valorPasajero, 3 valorConductor.
    }
}
