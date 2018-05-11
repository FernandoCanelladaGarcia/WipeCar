package tfg.android.fcg.modelo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.bajonivel.ServicioLocalizacion;

public class BDAdaptadorPosicion {

    private AppMediador appMediador;
    private DatabaseReference database;
    private Posicion posicion;

    private final String TAG = "depurador";

    public BDAdaptadorPosicion(){
        appMediador = AppMediador.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("posicion");
    }

    public void iniciarGps(){
        appMediador.launchService(ServicioLocalizacion.class,null);
    }
    /**
     * Busca la posicion del usuario y la almacena en la tabla usuario que posee
     * el identificador que coincide con el parámetro.
     * @param idUser contendra:
     */
    public void obtenerPosicion(String idUser){
        //TODO Llamada al GPS para obtener ubicacion
        iniciarGps();
    }

    /**
     * Añade a la tabla Posición una latitud y longitud a un usuario determinado.
     * @param informacion contendra:
     */
    public void agregarPosicion(Object[] informacion){
        //INFORMACION 0=idUser, 1=latitud 2=longitud
        String idUser = (String)informacion[0];
        String latitud = (String)informacion[1];
        String longitud = (String)informacion[2];

        posicion = new Posicion(idUser,latitud,longitud);

        database.setValue(posicion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                //Agregada posicion correctamente
                    Log.i(TAG,"Agregada posicion correctamente");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA,true);
                    appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_GUARDADA,extras);
                }else{
                    Log.i(TAG,"Error a la hora de agregar posicion");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_GUARDADA,extras);
                }
            }
        });


    }

    /**
     * Modifica en la tabla Posición una latitud y longitud de un usuario determinado.
     * @param informacion
     */
    public void actualizarPosicion(Object[]informacion){
        //INFORMACION 0=idUser, 1=latitud 2=longitud
        String idUser = (String)informacion[0];
        String latitud = (String)informacion[1];
        String longitud = (String)informacion[2];

        Map<String, Object> posicionTask = new HashMap<>();
        posicionTask.put("latitud", informacion[1]);
        posicion.setLatitud(latitud);

        posicionTask.put("longitud", informacion[2]);
        posicion.setLongitud(longitud);

        database.child(idUser).updateChildren(posicionTask).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Agregada posicion correctamente
                    Log.i(TAG,"Actualizada posicion correctamente");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_ACTUALIZACION_POSICION,true);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_POSICION,extras);
                }else{
                    Log.i(TAG,"Error a la hora de actualizar posicion");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_ACTUALIZACION_POSICION,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_POSICION,extras);
                }
            }
        });
    }
}
