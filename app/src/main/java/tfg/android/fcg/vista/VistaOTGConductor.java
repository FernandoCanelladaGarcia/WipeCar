package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.presentador.IPresentadorOTGConductor;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaOTGConductor extends Fragment implements IVistaOTGConductor, View.OnClickListener{

    private AppMediador appMediador;
    private IPresentadorOTGConductor presentadorOTGConductor;
    private Button botonIniciarRuta, botonAceptarPasajero, botonRechazarPasajero, botonFinalizarRuta;
    private Usuario user;
    private Vinculo peticion;
    @Override
    public View onCreateView (LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = layoutInflater.inflate(R.layout.layout_vista_otgconductor,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        appMediador = AppMediador.getInstance();
        appMediador.setVistaOTGConductor(this);
        presentadorOTGConductor = appMediador.getPresentadorOTGConductor();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        botonIniciarRuta = (Button) view.findViewById(R.id.botonIniciarRuta);
        botonIniciarRuta.setOnClickListener(this);
        botonAceptarPasajero = (Button) view.findViewById(R.id.botonAceptarPasajero);
        botonAceptarPasajero.setOnClickListener(this);
        botonRechazarPasajero = (Button) view.findViewById(R.id.botonRechazarPasajero);
        botonRechazarPasajero.setOnClickListener(this);
        botonFinalizarRuta = (Button) view.findViewById(R.id.botonFinalizarRuta);
        botonFinalizarRuta.setOnClickListener(this);
    }

    @Override
    public void indicarPeticionPasajero(Object informacion) {
        peticion = (Vinculo)informacion;
        Toast.makeText(getActivity().getApplicationContext(),
                "Peticion recibida de usuario " + peticion.getIdPasajero(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void indicarPasajeroAceptado(Object informacion) {

    }

    @Override
    public void indicarPasajeroRechazado(Object informacion) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.botonIniciarRuta:
                appMediador.getVistaPrincipal().mostrarProgreso();
                user = appMediador.getVistaPrincipal().getUsuario();
                appMediador.getPresentadorOTGConductor().iniciar(user);
                break;
            case R.id.botonAceptarPasajero:
                break;
            case R.id.botonRechazarPasajero:
                break;
            case R.id.botonFinalizarRuta:
                //appMediador.getVistaPrincipal().mostrarProgreso();
                //appMediador.getPresentadorOTGConductor().tratarParar(user);
                break;

        }
    }
}
