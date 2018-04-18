package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaVehiculo extends AppCompatActivity implements IVistaVehiculo{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_vehiculo);
    }

    /**
     * Muestra una barra de progreso. Existe una acción en background.
     */
    @Override
    public void mostrarProgreso() {

    }

    /**
     * Elimina la barra de progreso. Acción en background finalizada.
     */
    @Override
    public void eliminarProgreso() {

    }

    /**
     * Muestra un diálogo al usuario para realizar una determinada acción.
     * @param informacion contendra:
     */
    @Override
    public void mostrarDialogo(Object informacion) {

    }

    /**
     * Cierra el dialogo mostrado al usuario tras su uso util.
     */
    @Override
    public void cerrarDialogo() {

    }

    /**
     * Habilita la edicion de los datos del vehiculo
     * @param informacion contendra:
     */
    @Override
    public void prepararEdicion(Object informacion) {

    }
}
