package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tfg.android.fcg.R;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaPerfil extends AppCompatActivity implements IVistaPerfil {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_perfil);
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
    public void prepararEdicion(Object informacion) {

    }
}
