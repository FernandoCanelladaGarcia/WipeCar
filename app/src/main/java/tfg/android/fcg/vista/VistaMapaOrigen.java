package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaMapaOrigen extends AppCompatActivity implements IVistaMapaOrigen{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_mapaorigen);
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
     * Muestra el mapa en la pantalla centrado en la ubicacion del usuario.
     * @param informacion contendra:
     */
    @Override
    public void mostrarMapaConPosicion(Object informacion) {

    }

    /**
     * Muestra una marca en el mapa.
     * @param informacion contendra:
     */
    @Override
    public void mostrarOrigen(Object informacion) {

    }
}
