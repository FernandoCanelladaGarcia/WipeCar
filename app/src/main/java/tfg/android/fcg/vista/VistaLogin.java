package tfg.android.fcg.vista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VistaLogin extends AppCompatActivity implements IVistaLogin {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_login);
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
}
