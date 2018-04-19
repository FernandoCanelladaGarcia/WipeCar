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

    @Override
    public void mostrarProgreso() {

    }

    @Override
    public void eliminarProgreso() {

    }

    @Override
    public void mostrarDialogo(Object informacion) {

    }

    @Override
    public void cerrarDialogo() {

    }

    @Override
    public void mostrarMapaConPosicion(Object informacion) {

    }
    
    @Override
    public void mostrarOrigen(Object informacion) {

    }
}
