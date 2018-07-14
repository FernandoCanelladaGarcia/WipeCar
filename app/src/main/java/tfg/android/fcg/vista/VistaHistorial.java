package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
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

    private AdapterHistorialLista adaptador;
    private List<Historial> listaHistorial;
    private String idUser;
    private boolean deshabilitoBack = true;

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

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
        idUser = sharedPreferences.getString("idUser",null);

        presentadorHistorial.iniciar(idUser);

        Log.i(TAG,"Vista Historial");
    }


    @Override
    public void mostrarProgreso() {
        Log.i(TAG,"mostrar Progreso");
        dialogoProgreso = new ProgressDialog(this);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogoProgreso.setIndeterminate(true);
        dialogoProgreso.setCancelable(false);
        dialogoProgreso.setTitle("Espere por favor");
        dialogoProgreso.setMessage("Cargando...");
        dialogoProgreso.show();
    }

    @Override
    public void cerrarProgreso() {
        Log.i(TAG,"cerrar Progreso");
        dialogoProgreso.dismiss();
    }


    @Override
    public void mostrarDialogo(Object informacion) {
        int tipo = (int) informacion;
        Log.i(TAG,"informacion " + informacion.toString());
        final AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        switch (tipo){
            case 0:
                dialogBuild.setTitle("Error a la hora de acceder a su historial");
                dialogBuild.setMessage("Compruebe su conexión para poder acceder correctamente a los datos");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                        appMediador.launchActivity(VistaPerfil.class, this,null);
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 1:
                CharSequence[] values = {"0","1","2","3","4","5"};
                dialogBuild.setTitle("Valoración del usuario");
                dialogBuild.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        //Toast.makeText(appMediador.getApplicationContext(),i,Toast.LENGTH_SHORT).show();
                        presentadorHistorial.tratarValoracion(i);
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 2:
                dialogBuild.setTitle("Eliminar Historial");
                dialogBuild.setMessage("¿Esta seguro?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                        presentadorHistorial.eliminarHistorial();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;

        }
    }


    @Override
    public void cerrarDialogo() {
        dialogo.cancel();
    }


    @Override
    public void mostrarHistorial(Object informacion) {
        ListView listView = (ListView) findViewById(R.id.ListaHistorial);
        Object[] respuesta = (Object[])informacion;
        listaHistorial = (ArrayList<Historial>)respuesta[1];
        adaptador = new AdapterHistorialLista(VistaHistorial.this, listaHistorial, idUser, appMediador);
        listView.setAdapter(adaptador);
        if((int)respuesta[0] == 1){
            findViewById(R.id.elementoListaHistorialVacia).setVisibility(View.GONE);
        }if((int)respuesta[0] == 0){
            findViewById(R.id.elementoListaHistorialVacia).setVisibility(View.VISIBLE);
        }
        cerrarProgreso();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.botonBackHistorial:
                presentadorHistorial.tratarVolver();
                break;
            case R.id.botonSalirHistorial:
                presentadorHistorial.tratarSalir();
                break;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        appMediador.removePresentadorHistorial();
    }

    @Override
    public void onBackPressed() {
        if (deshabilitoBack) {

        } else {
            super.onBackPressed();
        }
    }
}
