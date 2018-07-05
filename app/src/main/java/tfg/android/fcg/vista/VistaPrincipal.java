package tfg.android.fcg.vista;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.presentador.IPresentadorPrincipal;
import tfg.android.fcg.vista.adaptadores.AdapterPrincipalLista;
import tfg.android.fcg.vista.fragmentos.FragmentoPrincipalLista;

public class VistaPrincipal extends AppCompatActivity implements IVistaPrincipal, View.OnClickListener {

    private Button botonPerfil, botonSalir;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorPrincipal presentadorPrincipal;
    private AdapterPrincipalLista adaptador;
    private List<Usuario> listaUsuarios;
    private List<Vehiculo> listaVehiculos;
    private final static String TAG = "depurador";
    private FloatingActionButton floatPrincipal;
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    private AlertDialog.Builder dialogBuild;

    private boolean deshabilitoBack = true;
    private boolean rol;

    //Fecha y hora
    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    public final Calendar c = Calendar.getInstance();

    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private ImageButton botonFecha, botonHora;
    private EditText eFecha, eHora;

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
            adapter.addFragment(new VistaOTGConductor(), "On The Go Conductor");
            //floatPrincipal.setImageResource(R.drawable.icon_edit_salida);
            Log.i(TAG, "Vista principal - Modo conductor");
        } else {
            adapter.addFragment(new FragmentoPrincipalLista(), "Pick Up Pasajero");
            adapter.addFragment(new VistaOTGPasajero(), "On The Go Pasajero");
            //floatPrincipal.setImageResource(R.drawable.icon_edit_destino);
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
        int tarea = (int)informacion;
        dialogBuild = new AlertDialog.Builder(this);
        final View dialogoOrigenDestino = getLayoutInflater().inflate(R.layout.layout_destino,null);
        final View dialogoFechaHora = getLayoutInflater().inflate(R.layout.layout_fecha_hora,null);
        switch(tarea){
            case 0:
                //Editar Destino
                //Toast.makeText(getApplicationContext(),"Editar destino",Toast.LENGTH_SHORT).show();
                dialogBuild.setView(dialogoOrigenDestino);
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 1:
                //Editar Fecha y hora de salida
                //Toast.makeText(getApplicationContext(),"Editar Fecha y hora",Toast.LENGTH_SHORT).show();
                dialogBuild.setView(dialogoFechaHora);
                dialogo =dialogBuild.create();
                dialogo.show();
                botonFecha = (ImageButton) dialogo.findViewById(R.id.obtener_fecha);
                botonHora = (ImageButton) dialogo.findViewById(R.id.obtener_hora);
                botonFecha.setOnClickListener(this);
                botonHora.setOnClickListener(this);
                eFecha = (EditText)dialogo.findViewById(R.id.et_mostrar_fecha_picker) ;
                eHora = (EditText)dialogo.findViewById(R.id.et_mostrar_hora_picker);
                break;
            case 3:
                //Error a la hora de presentar lista
                break;
        }
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
        listaUsuarios = (ArrayList<Usuario>) respuesta[1];
        if(respuesta.length > 2){
            listaVehiculos = (ArrayList<Vehiculo>)respuesta[2];
            adaptador = new AdapterPrincipalLista(VistaPrincipal.this, listaUsuarios, appMediador, listaVehiculos);
        }else{
            adaptador = new AdapterPrincipalLista(VistaPrincipal.this, listaUsuarios, appMediador);
        }
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
                Object[] informacion = new Object[]{0,""};
                presentadorPrincipal.tratarConfiguracion(informacion);
                break;
            case R.id.botonSalir:
                Log.i(TAG, "Salir");
                Object[] datos = new Object[]{0,""};
                presentadorPrincipal.tratarConfiguracion(datos);
                break;
            case R.id.floatPrincipal:
                if(rol){
                    //Editar fecha y hora
                    mostrarDialogo(1);
                }else{
                    //Editar Destino
                    mostrarDialogo(0);
                }
                break;
            case R.id.botonGuardarDestino:
                Spinner destinos = dialogo.findViewById(R.id.spinnerDestinoPrincipal);
                if(destinos.getSelectedItem().toString().equals("Destinos")){
                    Toast.makeText(getApplicationContext(),
                            "Por favor, seleccione un destino", Toast.LENGTH_SHORT).show();
                }else{
                    String destino = destinos.getSelectedItem().toString();
                    Object[] respuesta = new Object[]{2,destino};
                    presentadorPrincipal.tratarConfiguracion(respuesta);

                }
            case R.id.obtener_hora:

                TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaformateada = (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                        String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                        String AM_PM;
                        if(hourOfDay < 12) {
                            AM_PM = "a.m.";
                        } else {
                            AM_PM = "p.m.";
                        }
                        eHora.setText(horaformateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                    }
                },hora,minuto,false);
                recogerHora.show();
                Log.i(TAG,"Muestra time picker");
                break;

            case R.id.obtener_fecha:

                DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month+1;
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        eFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                    }
                },anio,mes,dia);
                recogerFecha.show();
                Log.i(TAG,"Muestra date picker");
                break;

            case R.id.guardarFechaHora:
                if(eFecha.getText().toString().isEmpty()){

                }else if(eHora.getText().toString().isEmpty()){

                }else{
                    Object[] config = new Object[2];
                    config[0] = 3;
                    Log.i(TAG,eFecha.getText().toString());
                    Log.i(TAG,eHora.getText().toString());
                    String[] fechaHora = {eFecha.getText().toString(), eHora.getText().toString()};
                    config[1] = fechaHora;
                    presentadorPrincipal.tratarConfiguracion(config);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        rol = sharedPreferences.getBoolean("rol", false);
        setUpViewPager(rol);

        String idUser = sharedPreferences.getString("idUser", null);
        presentadorPrincipal.iniciar(idUser);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onDestroy() {
        viewPager = null;
        adaptador = null;
        tabLayout = null;
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
