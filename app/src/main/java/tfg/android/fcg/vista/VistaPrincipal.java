package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class VistaPrincipal extends AppCompatActivity implements IVistaPrincipal{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_principal);
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
    public void mostrarUsuarios(Object informacion) {

    }

    @Override
    public void mostrarSeleccion(Object informacion) {

    }

    @Override
    public void desmarcarSeleccion(Object informacion) {

    }
}
