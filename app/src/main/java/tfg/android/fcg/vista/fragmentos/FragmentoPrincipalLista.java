package tfg.android.fcg.vista.fragmentos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.vista.VistaPrincipal;
import tfg.android.fcg.vista.adaptadores.AdapterPrincipalLista;

public class FragmentoPrincipalLista extends Fragment{

    private View rootView;
    private FloatingActionButton floatPrincipal;
    private Button botonRefrescar;
    private ListView listView;
    private VistaPrincipal vistaPrincipal;
    private AppMediador appMediador;
    private ArrayList<Usuario> listaPasajeros;
    private ArrayList<Usuario> listaConductores;
    private ArrayList<Vehiculo> listaVehiculos;
    private ArrayList<Vinculo> listaVinculos;
    private AdapterPrincipalLista adapterPrincipalLista;
    private boolean pausada = false;
    private final static String TAG = "depurador";
    private boolean rol;

    public FragmentoPrincipalLista(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appMediador = (AppMediador)getActivity().getApplication();
        vistaPrincipal = (VistaPrincipal) appMediador.getVistaPrincipal();

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        rol = sharedPreferences.getBoolean("rol", false);

        rootView = inflater.inflate(R.layout.fragment_pickup, container, false);
        listView = (ListView) rootView.findViewById(R.id.listaPrincipal);
        floatPrincipal = (FloatingActionButton) rootView.findViewById(R.id.floatPrincipal);
        botonRefrescar = (Button) rootView.findViewById(R.id.botonRefrescar);
        if(!pausada){
            if(!rol){
                vistaPrincipal.obtenerVehiculos();
                floatPrincipal.setImageResource(R.drawable.icon_edit_destino);
            }else{
                vistaPrincipal.obtenerUsuarios();
                floatPrincipal.setImageResource(R.drawable.icon_edit_salida);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
    }

    public void setListaPasajeros(ArrayList<Usuario> pasajeros, ArrayList<Vinculo> vinculos){

        if(listaPasajeros == null){
            this.listaPasajeros = pasajeros;
            this.listaVinculos = vinculos;
        }else{
            this.listaPasajeros = null;
            this.listaVinculos = null;

            this.listaPasajeros = pasajeros;
            this.listaVinculos = vinculos;
        }
        if(rootView != null){
            adapterPrincipalLista = new AdapterPrincipalLista(rootView.getContext(),listaPasajeros,listaVinculos,appMediador);
            listView.setAdapter(adapterPrincipalLista);
            if(listaPasajeros.isEmpty() || listaVinculos.isEmpty()){
                rootView.findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.VISIBLE);
                    TextView mensajeListaVacia = (TextView) rootView.findViewById(R.id.mensajeListaPrincipalVacia);
                    mensajeListaVacia.setText("No existen pasajeros que le hayan escogido para ir a su destino");
            }else{
                rootView.findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.GONE);
            }
        }
        Log.i(TAG, "FragmentoPrincipal - MODO CONDUCTOR");
    }

    public void setListaConductores(ArrayList<Usuario> conductores, ArrayList<Vehiculo> vehiculos){

        if(listaConductores == null){
            this.listaConductores = conductores;
            this.listaVehiculos = vehiculos;
        }else{

            this.listaConductores = null;
            this.listaVehiculos = null;

            this.listaConductores = conductores;
            this.listaVehiculos = vehiculos;
        }

        if(rootView != null){
            adapterPrincipalLista = new AdapterPrincipalLista(rootView.getContext(),listaConductores,appMediador,listaVehiculos);
            listView.setAdapter(adapterPrincipalLista);
            if(listaConductores.isEmpty() || listaVehiculos.isEmpty()){
                rootView.findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.VISIBLE);
                ImageView iconoListaVacia = (ImageView)rootView.findViewById(R.id.imagenListaVacia);
                iconoListaVacia.setImageResource(R.drawable.icon_car_user);
            }else{
                rootView.findViewById(R.id.elementoListaPrincipalVacia).setVisibility(View.GONE);
            }
        }
        Log.i(TAG, "FragmentoPrincipal - MODO PASAJERO");
    }

    @Override
    public void onDestroy() {
        rootView = null;
        listView = null;
        super.onDestroy();
    }
}
