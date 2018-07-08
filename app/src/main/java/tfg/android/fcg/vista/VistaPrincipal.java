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
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private FragmentoPrincipalLista fragmentoPrincipal;
    private ArrayList<Usuario> listaUsuarios;
    private Usuario user;
    private String idUser;
    private ArrayList<Vehiculo> listaVehiculos;

    private FloatingActionButton floatPrincipal;
    private static ViewPager viewPager;
    private static TabLayout tabLayout;
    private AlertDialog.Builder dialogBuild;

    private boolean deshabilitoBack = true;
    private boolean rol;
    private boolean pausada;
    private boolean tabsPreparadas;

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
    private final static String TAG = "depurador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_principal);
        appMediador = (AppMediador) this.getApplication();
        appMediador.setVistaPrincipal(this);

        tabsPreparadas = false;

        botonPerfil = (Button) findViewById(R.id.botonPerfil);
        botonPerfil.setOnClickListener(this);
        botonSalir = (Button) findViewById(R.id.botonSalir);
        botonSalir.setOnClickListener(this);

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        idUser = sharedPreferences.getString("idUser", null);

        appMediador.getPresentadorPrincipal().iniciar(idUser);
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
        int tarea = (int) informacion;
        dialogBuild = new AlertDialog.Builder(this);
        final View dialogoOrigenDestino = getLayoutInflater().inflate(R.layout.layout_destino, null);
        final View dialogoFechaHora = getLayoutInflater().inflate(R.layout.layout_fecha_hora, null);
        switch (tarea) {
            case 0:
                if (!rol) {
                    //Editar Destino
                    dialogBuild.setView(dialogoOrigenDestino);
                    dialogo = dialogBuild.create();
                    dialogo.show();
                }
                break;
            case 1:
                if (rol) {
                    //Editar Fecha y hora de salida
                    dialogBuild.setView(dialogoFechaHora);
                    dialogo = dialogBuild.create();
                    dialogo.show();
                    botonFecha = (ImageButton) dialogo.findViewById(R.id.obtener_fecha);
                    botonHora = (ImageButton) dialogo.findViewById(R.id.obtener_hora);
                    botonFecha.setOnClickListener(this);
                    botonHora.setOnClickListener(this);
                    eFecha = (EditText) dialogo.findViewById(R.id.et_mostrar_fecha_picker);
                    eHora = (EditText) dialogo.findViewById(R.id.et_mostrar_hora_picker);
                }
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

    //TODO: NO USADO, REDACCION
    @Override
    public void mostrarUsuarios(Object informacion) {
//        ListView listView = (ListView) findViewById(R.id.listaPrincipal);
//        Object[] respuesta = (Object[]) informacion;
//        listaUsuarios = (ArrayList<Usuario>) respuesta[1];
//        Log.i(TAG,"CONDUCTORES " + listaUsuarios.size());
//        if(respuesta[2] != null){
//            listaVehiculos = (ArrayList<Vehiculo>)respuesta[2];
//            adaptador = new AdapterPrincipalLista(VistaPrincipal.this, listaUsuarios, appMediador, listaVehiculos);
//        }else{
//            adaptador = new AdapterPrincipalLista(VistaPrincipal.this, listaUsuarios, appMediador);
//        }
//        listView.setAdapter(adaptador);
//        if ((int) respuesta[0] == 1) {
//            findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.GONE);
//        }if ((int) respuesta[0] == 0) {
//            findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.VISIBLE);
//            if(rol){
//                TextView mensajeListaVacia = (TextView) findViewById(R.id.mensajeListaPrincipalVacia);
//                mensajeListaVacia.setText("No existen pasajeros que le hayan escogido para ir a su destino");
//            }else{
//                ImageView iconoListaVacia = (ImageView)findViewById(R.id.imagenListaVacia);
//                iconoListaVacia.setImageResource(R.drawable.icon_car_user);
//            }
//        }
    }

    @Override
    public void mostrarSeleccion(Object informacion) {

    }

    @Override
    public void desmarcarUsuario(Object informacion) {

    }

    @Override
    public void setUsuario(Object informacion) {

        Usuario getUser = (Usuario) informacion;
        if (user == null) {
            Log.i(TAG, "set usuario new");
            user = getUser;
        } else {
            Log.i(TAG, "set usuario refresh");
            user = null;
            user = getUser;
        }
        if (user.isRol()) {
            rol = user.isRol();
            Log.i(TAG, "Usuario conductor " + user.getIdUser());
            appMediador.getPresentadorPrincipal().obtenerPeticionesPasajeros(user.getIdUser());
        } else {
            rol = user.isRol();
            Log.i(TAG, "Usuario pasajero");
            appMediador.getPresentadorPrincipal().obtenerConductores(user.getDestino());
        }
    }

    @Override
    public void setConductores(Object informacion) {

        ArrayList<Usuario> getConduct = (ArrayList<Usuario>) informacion;

        if (listaUsuarios == null) {
            Log.i(TAG, "set conductores new");
            listaUsuarios = getConduct;
        } else {
            Log.i(TAG, "set conductores refresh");
            listaUsuarios = null;
            listaUsuarios = getConduct;
        }

        if (!getConduct.isEmpty()) {
            appMediador.getPresentadorPrincipal().obtenerVehiculos(listaUsuarios);
        } else {
            listaUsuarios = new ArrayList<>();
            listaVehiculos = new ArrayList<>();
            prepararTabs();
        }
    }

    @Override
    public void setVehiculos(Object informacion) {
        ArrayList<Vehiculo> getVehic = (ArrayList<Vehiculo>) informacion;

        if (listaVehiculos == null) {
            Log.i(TAG, "set vehiculos new");
            listaVehiculos = getVehic;
        } else {
            Log.i(TAG, "set vehiculos refresh");
            listaVehiculos = null;
            listaVehiculos = getVehic;
        }
        prepararTabs();

    }

    @Override
    public void setPasajeros(Object informacion) {


        ArrayList<Usuario> getPasaj = (ArrayList<Usuario>) informacion;
        if (listaUsuarios == null) {
            Log.i(TAG, "set pasajeros new");
            listaUsuarios = getPasaj;
        } else {
            Log.i(TAG, "set pasajeros refresh");
            listaUsuarios = null;
            listaUsuarios = getPasaj;
        }
        prepararTabs();

    }

    public void obtenerUsuarios() {
        fragmentoPrincipal.setListaPasajeros(listaUsuarios);
    }

    public void obtenerVehiculos() {
        fragmentoPrincipal.setListaConductores(listaUsuarios, listaVehiculos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.botonPerfil:
                Log.i(TAG, "Perfil");
                Object[] informacion = new Object[]{0, ""};
                appMediador.getPresentadorPrincipal().tratarConfiguracion(informacion);
                break;
            case R.id.botonSalir:
                Log.i(TAG, "Salir");
                Object[] datos = new Object[]{1, ""};
                appMediador.getPresentadorPrincipal().tratarConfiguracion(datos);
                break;
            case R.id.floatPrincipal:
                if (rol) {
                    //Editar fecha y hora
                    Log.i(TAG, "Fecha y hora");
                    mostrarDialogo(1);
                } else if (!rol) {
                    //Editar Destino
                    Log.i(TAG, "Destino");
                    mostrarDialogo(0);
                }
                break;
            case R.id.botonGuardarDestino:
                Spinner destinos = dialogo.findViewById(R.id.spinnerDestinoPrincipal);
                if (destinos.getSelectedItem().toString().equals("DESTINOS")) {
                    Toast.makeText(getApplicationContext(),
                            "Por favor, seleccione un destino", Toast.LENGTH_SHORT).show();
                } else if (destinos.getSelectedItem().toString().equals(user.getDestino())) {
                    Toast.makeText(getApplicationContext(),
                            "Ha seleccionado el mismo destino", Toast.LENGTH_SHORT).show();
                } else {
                    String destino = destinos.getSelectedItem().toString();
                    Object[] respuesta = new Object[]{2, destino};
                    appMediador.getPresentadorPrincipal().tratarConfiguracion(respuesta);

                }
            case R.id.obtener_hora:
                if (rol) {
                    TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String horaformateada = (hourOfDay < 10) ? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                            String minutoFormateado = (minute < 10) ? String.valueOf(CERO + minute) : String.valueOf(minute);
                            String AM_PM;
                            if (hourOfDay < 12) {
                                AM_PM = "a.m.";
                            } else {
                                AM_PM = "p.m.";
                            }
                            eHora.setText(horaformateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                        }
                    }, hora, minuto, false);
                    recogerHora.show();
                    Log.i(TAG, "Muestra time picker");
                }
                break;

            case R.id.obtener_fecha:
                if (rol) {
                    DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            final int mesActual = month + 1;
                            String diaFormateado = (dayOfMonth < 10) ? CERO + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                            String mesFormateado = (mesActual < 10) ? CERO + String.valueOf(mesActual) : String.valueOf(mesActual);
                            eFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                        }
                    }, anio, mes, dia);
                    recogerFecha.show();
                    Log.i(TAG, "Muestra date picker");
                }
                break;

            case R.id.guardarFechaHora:
                if (eFecha.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Por favor, seleccione una fecha", Toast.LENGTH_SHORT).show();
                } else if (eHora.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),
                            "Por favor, seleccione una hora", Toast.LENGTH_SHORT).show();
                } else if (!compararFechas()) {
                    Toast.makeText(getApplicationContext(),
                            "Seleccione una fecha y una hora posteriores a la actual.", Toast.LENGTH_SHORT).show();
                } else {
                    Object[] config = new Object[2];
                    config[0] = 3;
                    Log.i(TAG, eFecha.getText().toString());
                    Log.i(TAG, eHora.getText().toString());
                    String[] fechaHora = {eFecha.getText().toString(), eHora.getText().toString()};
                    config[1] = fechaHora;
                    appMediador.getPresentadorPrincipal().tratarConfiguracion(config);
                }
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (pausada) {
            fragmentoPrincipal = null;
            tabLayout = null;
            viewPager = null;
            Log.i(TAG, "On resume " + idUser);
            appMediador.getPresentadorPrincipal().iniciar(idUser);
        }
        pausada = false;

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausada = true;
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
    public void onBackPressed() {
        if (deshabilitoBack) {

        } else {
            super.onBackPressed();
        }
    }

    private boolean compararFechas() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date fechaCogida = sdf.parse(eFecha.getText().toString());
            Log.i(TAG, "Fecha cogida: " + fechaCogida.toString());
            Date actual = c.getTime();
            Log.i(TAG, "Fecha sistema: " + actual);

            if (actual.after(fechaCogida) || actual.equals(fechaCogida)) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void prepararTabs() {

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        viewPager = (ViewPager) findViewById(R.id.viewPagerPrincipal);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Log.i(TAG, "TABS");
        if (fragmentoPrincipal == null) {
            fragmentoPrincipal = new FragmentoPrincipalLista();
        } else {
            if (rol) {
                Log.i(TAG, "refresh fragment conductor");
                fragmentoPrincipal.setListaPasajeros(listaUsuarios);
            } else if (!rol) {
                Log.i(TAG, "refresh fragment pasajero");
                fragmentoPrincipal.setListaConductores(listaUsuarios, listaVehiculos);
            }
        }
        setAdaptador(adapter);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        cerrarProgreso();
    }

    private void setAdaptador(ViewPagerAdapter adaptador) {

        if (rol) {
            adaptador.addFragment(fragmentoPrincipal, "Pick Up Conductor");
            adaptador.addFragment(new VistaOTGConductor(), "On The Go Conductor");
            Log.i(TAG, "Vista principal - Modo conductor");

        } else {
            adaptador.addFragment(fragmentoPrincipal, "Pick Up Pasajero");
            adaptador.addFragment(new VistaOTGPasajero(), "On The Go Pasajero");
            Log.i(TAG, "Vista principal - Modo pasajero");
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
