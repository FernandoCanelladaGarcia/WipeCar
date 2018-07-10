package tfg.android.fcg.vista.fragmentos;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.vista.VistaPrincipal;
import tfg.android.fcg.vista.adaptadores.AdapterPrincipalVinculos;

public class FragmentoPrincipalVinculos extends Fragment{
    private View rootView;
    private ListView listView;
    private VistaPrincipal vistaPrincipal;
    private AppMediador appMediador;
    private ArrayList<Usuario> listaUsuariosVinculos;
    private ArrayList<Vehiculo> listaVehiculosVinculo;
    private ArrayList<Vinculo> listaVinculos;
    private AdapterPrincipalVinculos adapterPrincipalVinculos;
    private boolean pausada = false;
    private final static String TAG = "depurador";

    public FragmentoPrincipalVinculos(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        appMediador = (AppMediador)getActivity().getApplication();
        vistaPrincipal = (VistaPrincipal)appMediador.getVistaPrincipal();

        rootView = inflater.inflate(R.layout.fragment_vinculo,container,false);
        listView = (ListView) rootView.findViewById(R.id.listaVinculos);

        if(!pausada){
            vistaPrincipal.obtenerListaVinculos();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstaceState) {
        super.onActivityCreated(savedInstaceState);
    }

    public void setListaVinculos(ArrayList<Usuario> vinculos, ArrayList<Vehiculo> vehiculosVinculo, ArrayList<Vinculo> listaVinculos){
        if(listaUsuariosVinculos == null || listaVinculos == null){
            this.listaUsuariosVinculos = vinculos;
            this.listaVehiculosVinculo = vehiculosVinculo;
            this.listaVinculos = listaVinculos;
        }else{
            this.listaUsuariosVinculos = null;
            this.listaVehiculosVinculo = null;
            this.listaVinculos = null;

            this.listaUsuariosVinculos = vinculos;
            this.listaVehiculosVinculo = vehiculosVinculo;
            this.listaVinculos = listaVinculos;
        }

        if(rootView != null){
            adapterPrincipalVinculos = new AdapterPrincipalVinculos(rootView.getContext(), listaUsuariosVinculos,appMediador,listaVehiculosVinculo,listaVinculos);
            listView.setAdapter(adapterPrincipalVinculos);

            if(listaUsuariosVinculos.isEmpty() || listaVehiculosVinculo.isEmpty()){
                rootView.findViewById(R.id.elementoListaVinculoVacia).setVisibility(View.VISIBLE);
            }else{
                rootView.findViewById(R.id.elementoListaVinculoVacia).setVisibility(View.GONE);
            }
        }
        Log.i(TAG, "FRAGMENTO VINCULO");
    }

    @Override
    public void onDestroy(){
        rootView = null;
        listView = null;
        super.onDestroy();
    }
}
