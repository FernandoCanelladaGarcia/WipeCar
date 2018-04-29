package tfg.android.fcg.modelo;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tfg.android.fcg.AppMediador;

public class BDAdaptadorVehiculo {

    private AppMediador appMediador;
    private DatabaseReference database;
    private Vehiculo vehiculo;

    public BDAdaptadorVehiculo(){
        appMediador = AppMediador.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("vehiculos");
    }

    /**
     * Busca en la tabla Vehículo aquel registro que coincide con el parámetro.
     * @param datoVehiculo contendra:
     */
    public void obtenerVehiculo(String datoVehiculo){

    }

    /**
     * Añade a la tabla Vehículo un vehículo determinado que se indica a través del parámetro.
     * @param informacion contendra:
     */
    public void agregarVehiculo(Object[] informacion){
        //Informacion
        String marca = (String)informacion[0];
        String modelo = (String)informacion[1];
        String matricula = (String)informacion[2];
    }

    /**
     * Modifica en la tabla Vehículo, un determinado vehículo que se indica a través del parámetro.
     * @param informacion contendra:
     */
    public void actualizarVehiculo(Object informacion){

    }
}
