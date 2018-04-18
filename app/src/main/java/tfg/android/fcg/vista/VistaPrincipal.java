package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class VistaPrincipal extends AppCompatActivity implements IVistaPrincipal{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_principal);
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
     * Presenta la lista de conductores o pasajeros.
     * @param informacion contendra:
     */
    @Override
    public void mostrarUsuarios(Object informacion) {

    }

    /**
     * Destaca un pasajero o conductor tras su seleccion.
     * @param informacion contendra:
     */
    @Override
    public void mostrarSeleccion(Object informacion) {

    }

    /**
     * Desmarca un pasajero o conductor y muestra el resultado.
     * @param informacion contendra:
     */
    @Override
    public void desmarcarSeleccion(Object informacion) {

    }
}
