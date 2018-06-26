package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Historial;
import tfg.android.fcg.presentador.IPresentadorHistorial;
import tfg.android.fcg.vista.adaptadores.AdapterHistorialLista;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaHistorial extends AppCompatActivity implements IVistaHistorial, View.OnClickListener {

    private Button botonBack, botonSalir;
    private AppMediador appMediador;
    private IPresentadorHistorial presentadorHistorial;

    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;

    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private AdapterHistorialLista adaptador;
    private List<Historial> listaHistorial;

    private final static String TAG = "depurador";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_historial);
        appMediador = (AppMediador) this.getApplication();
        appMediador.setVistaHistorial(this);
        presentadorHistorial = appMediador.getPresentadorHistorial();

        botonBack = (Button) findViewById(R.id.botonBackHistorial);
        botonBack.setOnClickListener(this);

        botonSalir = (Button) findViewById(R.id.botonSalirHistorial);
        botonSalir.setOnClickListener(this);

        Toolbar topToolbar = (Toolbar) findViewById(R.id.toolbarLayoutHistorial);
        setSupportActionBar(topToolbar);

        layoutManager = new LinearLayoutManager(VistaHistorial.this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);

        Log.i(TAG,"Vista Historial");
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
        adaptador = new AdapterHistorialLista(VistaHistorial.this, listaHistorial);
        recyclerView.setAdapter(adaptador);
    }

    @Override
    public void onClick(View v) {

    }
}
