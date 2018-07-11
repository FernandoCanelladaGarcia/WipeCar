package tfg.android.fcg.modelo;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.bajonivel.ServicioLocalizacion;

public class BDAdaptadorPosicion {

    private AppMediador appMediador;
    private DatabaseReference database;
    private Posicion posicion;
    protected Geocoder geocoder;

    private final String TAG = "depurador";

    public BDAdaptadorPosicion(){
        appMediador = AppMediador.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("posicion");
        geocoder = new Geocoder(appMediador.getApplicationContext(), Locale.getDefault());
    }

    /**
     * Inicia el servicio de localización
     */
    public void iniciarGps(){
        appMediador.launchService(ServicioLocalizacion.class,null);
    }
    /**
     * Busca la posición del usuario en la tabla posición que posee
     * el identificador que coincide con el parámetro.
     * @param informacion contendra:
     */
    public void obtenerPosicion(Object informacion){
    //TODO: OBTENER POSICION LAT-LONG DE LA TABLA POSICIONES
        database.child((String)informacion).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Posicion posicion = dataSnapshot.getValue(Posicion.class);
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_POSICION,posicion);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_POSICION,extras);
                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_POSICION,null);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_POSICION,extras);
            }
        });
    }

    /**
     * Añade a la tabla Posición una latitud y longitud a un usuario determinado.
     * @param informacion contendra:
     */
    public void agregarPosicion(final Object[] informacion){
        //INFORMACION 0=idUser, 1=latitud 2=longitud
        String idUser = (String)informacion[0];
        Double latitud = (Double)informacion[1];
        Double longitud = (Double)informacion[2];

        posicion = new Posicion(idUser,latitud.toString(),longitud.toString());

        database.child(idUser).setValue(posicion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                //Agregada posicion correctamente
                    Log.i(TAG,"Agregada posicion correctamente");

                    Bundle extras = new Bundle();
                    extras.putSerializable(AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA,informacion);
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

    public void eliminarPosicion(Object informacion){
        String idUser = (String) informacion;

        database.child(idUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.i(TAG, "Eliminada posicion usuario correctamente");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_LOCALIZACION_ELIMINADA,true);
                    appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_ELIMINADA,extras);
                }else{
                    Log.i(TAG,"Error a la hora de eliminar posicion");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_LOCALIZACION_ELIMINADA,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_ELIMINADA,extras);
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

    public void traducirLatlng(LatLng miLatlng){
        Bundle extras = new Bundle();
        List<Address> addresses = null;
        String errorMessage = "";
        try {
            if(geocoder.isPresent())
            addresses = geocoder.getFromLocation(miLatlng.latitude,miLatlng.longitude,1);

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "Servicio no disponible";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "Latitud y longitud invalidas";
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + miLatlng.latitude +
                    ", Longitude = " +
                    miLatlng.longitude, illegalArgumentException);
        }

        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no se han obtenido direcciones";
                Log.e(TAG, errorMessage);
                extras.putBoolean(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION, false);
                appMediador.sendBroadcast(AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION, extras);
                return;
            }
        }else{
            String address = addresses.get(0).getAddressLine(0);
            Log.i(TAG,"Calle encontrada: " + address);
            extras.putString(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION,address);
            appMediador.sendBroadcast(AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION,extras);
        }
    }

    public void finalizarGps(){
        appMediador.stopService(ServicioLocalizacion.class);
    }
}
