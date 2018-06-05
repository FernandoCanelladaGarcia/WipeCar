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

import java.util.HashMap;
import java.util.Map;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorVehiculo {

    private AppMediador appMediador;
    private DatabaseReference database;
    private Vehiculo vehiculo;

    private final String TAG = "depurador";

    public BDAdaptadorVehiculo(){
        appMediador = AppMediador.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("vehiculos");
    }

    /**
     * Busca en la tabla Vehículo aquel registro que coincide con el parámetro.
     * @param datoVehiculo contendra:
     */
    public void obtenerVehiculo(final String datoVehiculo){
        database.child(datoVehiculo).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Se ha obtenido vehiculo
                vehiculo = dataSnapshot.getValue(Vehiculo.class);

                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_VEHICULO,vehiculo);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_VEHICULO,extras);

                database.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Error a la hora de obtener vehiculo
                vehiculo = null;

                Bundle extras = new Bundle();
                extras.putSerializable(AppMediador.CLAVE_OBTENER_VEHICULO,vehiculo);
                appMediador.sendBroadcast(AppMediador.AVISO_OBTENER_VEHICULO,extras);

                database.removeEventListener(this);
            }
        });
    }

    /**
     * Añade a la tabla Vehículo un vehículo determinado que se indica a través del parámetro.
     * @param informacion contendra:
     */
    public void agregarVehiculo(Object[] informacion){
        //TODO: ENCRIPTAR
        String marca = (String)informacion[0];
        String modelo = (String)informacion[1];
        String matricula = (String)informacion[2];
        final String datoVehiculo = database.push().getKey();

        vehiculo = new Vehiculo(datoVehiculo,marca,modelo,matricula);

        database.child(datoVehiculo).setValue(vehiculo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                //Se ha agregado correctamente vehiculo
                    Log.i(TAG,"Agregado vehiculo correctamente");
                    Bundle extras = new Bundle();
                    extras.putString(AppMediador.CLAVE_RESULTADO_NUEVO_VEHICULO,datoVehiculo);

                    appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_VEHICULO,extras);
                }else{
                    Log.i(TAG,"Error a la hora de agregar el vehiculo");
                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_NUEVO_VEHICULO,false);

                    appMediador.sendBroadcast(AppMediador.AVISO_REGISTRO_VEHICULO,extras);
                }
            }
        });

    }

    /**
     * Modifica en la tabla Vehículo, un determinado vehículo que se indica a través del parámetro.
     * @param informacion contendra:
     */
    //TODO BUSCAR VEHICULO A TRAVES DE ID USER!!
    public void actualizarVehiculo(Object[] informacion){
        //INFORMACION 0=datovehiculo, 1=marca 2=modelo, 3=matricula.
        String datoVehiculo = (String)informacion[0];
        String marca = (String)informacion[1];
        String modelo = (String)informacion[2];
        String matricula = (String)informacion[3];

        Map<String, Object> vehiculoTask = new HashMap<>();
        vehiculoTask.put("marca", marca);
        vehiculo.setMarca(marca);

        vehiculoTask.put("modelo", modelo);
        vehiculo.setModelo(modelo);

        vehiculoTask.put("matricula",matricula);
        vehiculo.setMatricula(matricula);

        database.child(datoVehiculo).updateChildren(vehiculoTask).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Actualizacion vehiculo correctamente
                    Log.i(TAG,"Actualizada posicion correctamente");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_ACTUALIZACION_VEHICULO,true);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_VEHICULO,extras);
                }else{
                    Log.i(TAG,"Error a la hora de actualizar vehiculo");

                    Bundle extras = new Bundle();
                    extras.putBoolean(AppMediador.CLAVE_RESULTADO_ACTUALIZACION_VEHICULO,false);
                    appMediador.sendBroadcast(AppMediador.AVISO_ACTUALIZACION_VEHICULO,extras);
                }
            }
        });
    }

    public void eliminarVehiculo(Object informacion){
    //Informacion Query del datovehiculo, eliminar vehiculo de tabla vehiculos
        String idUser = (String)informacion;

        final DatabaseReference referenciaUsuario = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(idUser);
        referenciaUsuario.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Obtenido usuario
                Usuario user = dataSnapshot.getValue(Usuario.class);

                String datoVehiculo = (String) user.getDatoVehiculo();

                database.child(datoVehiculo).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Vehiculo eliminado, quitar referencia vehiculo de tabla usuarios.
                            Map<String, Object> vehiculoTask = new HashMap<>();
                            vehiculoTask.put("datoVehiculo","");

                            referenciaUsuario.updateChildren(vehiculoTask).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //Quitada la referencia del datoVehiculo del usuario
                                        Bundle extras = new Bundle();

                                        extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINACION_VEHICULO,true);
                                        appMediador.sendBroadcast(AppMediador.AVISO_ELIMINACION_VEHICULO,extras);

                                    }else{
                                        //Error al quitar la referencia
                                        Bundle extras = new Bundle();

                                        extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINACION_VEHICULO,false);
                                        appMediador.sendBroadcast(AppMediador.AVISO_ELIMINACION_VEHICULO,extras);
                                    }

                                }
                            });

                        }else{
                            //No se ha eliminado vehiculo de la tabla vehiculos
                            Bundle extras = new Bundle();

                            extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINACION_VEHICULO,false);
                            appMediador.sendBroadcast(AppMediador.AVISO_ELIMINACION_VEHICULO,extras);
                        }
                    }
                });
                referenciaUsuario.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //No hay coincidencia del referencia del usuario
                Usuario user = null;

                Bundle extras = new Bundle();

                extras.putBoolean(AppMediador.CLAVE_RESULTADO_ELIMINACION_VEHICULO,false);
                appMediador.sendBroadcast(AppMediador.AVISO_ELIMINACION_VEHICULO,extras);

                referenciaUsuario.removeEventListener(this);
            }
        });

    }
}
