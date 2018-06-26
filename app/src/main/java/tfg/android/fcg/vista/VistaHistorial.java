package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import tfg.android.fcg.R;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaHistorial extends AppCompatActivity implements IVistaHistorial{

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_historial);
    }


    @Override
    public void mostrarProgreso() {

    }

    @Override
    public void cerrarProgreso() {

    }


    @Override
    public void mostrarDialogo(Object informacion) {

    }


    @Override
    public void cerrarDialogo() {

    }


    @Override
    public void mostrarHistorial(Object informacion) {

    }
}
