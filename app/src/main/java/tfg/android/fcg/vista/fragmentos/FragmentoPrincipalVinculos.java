package tfg.android.fcg.vista.fragmentos;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.vista.VistaPrincipal;
import tfg.android.fcg.vista.adaptadores.AdapterPrincipalVinculos;

public class FragmentoPrincipalVinculos extends Fragment{
    private View rootView;
    private ListView listView;
    private VistaPrincipal vistaPrincipal;
    private AppMediador appMediador;
    private ArrayList<Usuario> listaVinculos;
    private ArrayList<Vehiculo> listaVehiculosVinculo;
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

    public void setListaVinculos(ArrayList<Usuario> vinculos, ArrayList<Vehiculo> vehiculosVinculo){
        if(listaVinculos == null){
            this.listaVinculos = vinculos;
            this.listaVehiculosVinculo = vehiculosVinculo;
        }else{
            this.listaVinculos = null;
            this.listaVehiculosVinculo = null;

            this.listaVinculos = vinculos;
            this.listaVehiculosVinculo = vehiculosVinculo;
        }

        if(rootView != null){
            adapterPrincipalVinculos = new AdapterPrincipalVinculos(rootView.getContext(),listaVinculos,appMediador,listaVehiculosVinculo);
            listView.setAdapter(adapterPrincipalVinculos);

            if(listaVinculos.isEmpty() || listaVehiculosVinculo.isEmpty()){
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
