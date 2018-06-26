package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.presentador.IPresentadorPrincipal;
import tfg.android.fcg.vista.adaptadores.AdapterPrincipalLista;

public class VistaPrincipal extends AppCompatActivity implements IVistaPrincipal, View.OnClickListener{

    private Button botonPerfil, botonSalir;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorPrincipal presentadorPrincipal;
    private AdapterPrincipalLista adaptador;
    private final static String TAG = "depurador";

    private static ViewPager viewPager;
    private static TabLayout tabLayout;

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

//        viewPager = (ViewPager) findViewById(R.id.viewPagerPrincipal);
//        setUpViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                switch (tab.getPosition()){
//                    case 0:
//                        //PickUp
//                        break;
//                    case 1:
//                        //OnTheGo
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    private void setUpViewPager(ViewPager viewPager){

    }

    @Override
    public void mostrarProgreso() {
        Log.i(TAG," mostrar Progreso");
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
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        dialogo = dialogBuild.create();
        dialogo.show();
    }

    @Override
    public void cerrarDialogo() {
        Log.i(TAG,"cerrar Dialogo");
        dialogo.cancel();
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        appMediador.removePresentadorPrincipal();
    }
}
