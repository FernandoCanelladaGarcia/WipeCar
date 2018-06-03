package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.presentador.IPresentadorPrincipal;

public class VistaPrincipal extends AppCompatActivity implements IVistaPrincipal, View.OnClickListener{

    private Button botonPerfil, botonSalir;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorPrincipal presentadorPrincipal;
    private final static String TAG = "depurador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_principal);
        appMediador = (AppMediador) this.getApplication();
        appMediador.setVistaPrincipal(this);
        presentadorPrincipal = appMediador.getPresentadorPrincipal();

        botonPerfil = (Button) findViewById(R.id.botonPerfil);
        botonPerfil.setOnClickListener(this);
        botonSalir = (Button) findViewById(R.id.botonSalir);
        botonSalir.setOnClickListener(this);

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        Boolean rol = sharedPreferences.getBoolean("rol",false);
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
    public void mostrarUsuarios(Object informacion) {

    }

    @Override
    public void mostrarSeleccion(Object informacion) {

    }

    @Override
    public void desmarcarUsuario(Object informacion) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.botonPerfil:
                Log.i(TAG,"Perfil");
                presentadorPrincipal.tratarConfiguracion(0);
                break;
            case R.id.botonSalir:
                Log.i(TAG, "Salir");
                presentadorPrincipal.tratarConfiguracion(1);
                break;
        }
    }
}
