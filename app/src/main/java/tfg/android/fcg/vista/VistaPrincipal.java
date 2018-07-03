package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Historial;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.presentador.IPresentadorPrincipal;
import tfg.android.fcg.vista.adaptadores.AdapterPrincipalLista;
import tfg.android.fcg.vista.fragmentos.FragmentoPrincipalLista;
import tfg.android.fcg.vista.fragmentos.FragmentoPrincipalMapa;

public class VistaPrincipal extends AppCompatActivity implements IVistaPrincipal, View.OnClickListener {

    private Button botonPerfil, botonSalir;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorPrincipal presentadorPrincipal;
    private AdapterPrincipalLista adaptador;
    private List<Usuario> listaPrincipal;
    private final static String TAG = "depurador";

    private static ViewPager viewPager;
    private static TabLayout tabLayout;

    private boolean deshabilitoBack = true;
    private boolean rol;

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
    }

    private void setUpViewPager(boolean rol) {
        viewPager = (ViewPager) findViewById(R.id.viewPagerPrincipal);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (rol) {
            adapter.addFragment(new FragmentoPrincipalLista(), "Pick Up Conductor");
            adapter.addFragment(new VistaOTGPasajero(), "On The Go Conductor");
            Log.i(TAG, "Vista principal - Modo conductor");
        } else {
            adapter.addFragment(new FragmentoPrincipalLista(), "Pick Up Pasajero");
            adapter.addFragment(new VistaOTGConductor(), "On The Go Pasajero");
            Log.i(TAG, "Vista principal - Modo pasajero");
        }
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void mostrarProgreso() {
        Log.i(TAG, " mostrar Progreso");
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
        Log.i(TAG, "cerrar Progreso");
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
        Log.i(TAG, "cerrar Dialogo");
        dialogo.cancel();
    }

    @Override
    public void mostrarUsuarios(Object informacion) {
        ListView listView = (ListView) findViewById(R.id.listaPrincipal);
        Object[] respuesta = (Object[]) informacion;
        listaPrincipal = (ArrayList<Usuario>) respuesta[1];
        adaptador = new AdapterPrincipalLista(VistaPrincipal.this, listaPrincipal, appMediador);
        listView.setAdapter(adaptador);
        if ((int) respuesta[0] == 1) {
            findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.GONE);
        }if ((int) respuesta[0] == 0) {
            findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.VISIBLE);
            if(rol){
                TextView mensajeListaVacia = (TextView) findViewById(R.id.mensajeListaPrincipalVacia);
                mensajeListaVacia.setText("No existen pasajeros que le hayan escogido para ir a su destino");
            }else{
                ImageView iconoListaVacia = (ImageView)findViewById(R.id.imagenListaVacia);
                iconoListaVacia.setImageResource(R.drawable.icon_car_user);
            }
        }
        cerrarProgreso();
    }

    @Override
    public void mostrarSeleccion(Object informacion) {

    }

    @Override
    public void desmarcarUsuario(Object informacion) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.botonPerfil:
                Log.i(TAG, "Perfil");
                presentadorPrincipal.tratarConfiguracion(0);
                break;
            case R.id.botonSalir:
                Log.i(TAG, "Salir");
                presentadorPrincipal.tratarConfiguracion(1);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mostrarProgreso();

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        rol = sharedPreferences.getBoolean("rol", false);

        String idUser = sharedPreferences.getString("idUser", null);
        presentadorPrincipal.iniciar(idUser);

        setUpViewPager(rol);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appMediador.removePresentadorPrincipal();
    }

    @Override
    public void onBackPressed(){
        if(deshabilitoBack){

        }else{
            super.onBackPressed();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
