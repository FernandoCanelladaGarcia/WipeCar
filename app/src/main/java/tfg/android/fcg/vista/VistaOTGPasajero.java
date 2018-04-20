package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class VistaOTGPasajero extends AppCompatActivity implements IVistaOTGPasajero{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_otgpasajero);
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
    public void mostrarMapaConPosicion(Object informacion) {

    }

    @Override
    public void mostrarVehiculos(Object informacion) {

    }
}
