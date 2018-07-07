package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.presentador.IPresentadorOTGPasajero;

public class VistaOTGPasajero extends Fragment implements IVistaOTGPasajero, OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Marker miUbicacion;
    private List<Marker> ubicacionConductores;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorOTGPasajero presentadorOTGPasajero;
    private AlertDialog.Builder dialogBuild;
    private Button botonBuscar;
    private int anchoPantalla, altoPantalla;

    private final static String TAG = "depurador";


    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.layout_vista_otgpasajero,container,false);
        mMap = null;
        mapFragment = null;
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        appMediador = AppMediador.getInstance();
        appMediador.setVistaOTGPasajero(this);
        presentadorOTGPasajero = appMediador.getPresentadorOTGPasajero();
        mapFragment = null;
        mMap = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        mMap = null;
        botonBuscar = (Button) view.findViewById(R.id.BuscarVehiculos);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if(mapFragment != null){
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mMap != null){
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    presentadorOTGPasajero.tratarVehiculo(marker);
                    return true;
                }
            });
        }
        Log.i(TAG, "onMapReady");
        presentadorOTGPasajero.iniciar();
    }


    @Override
    public void mostrarProgreso() {
        Log.i(TAG, " mostrar Progreso");
        dialogoProgreso = new ProgressDialog(getActivity());
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
        dialogBuild = new AlertDialog.Builder(appMediador.getApplicationContext());
        dialogo = dialogBuild.create();
        dialogo.show();
    }

    @Override
    public void cerrarDialogo() {
        Log.i(TAG, "cerrar Dialogo");
        dialogo.cancel();
    }

    @Override
    public void mostrarMapaConPosicion(Object informacion) {
        Object[] posicion = (Object[]) informacion;
        Double latitud = (Double) posicion[0];
        Double longitud = (Double) posicion[1];
        String titulo = (String) posicion[2];

        LatLng lugar = new LatLng(latitud,longitud);

        if (miUbicacion != null) {
            miUbicacion.remove();
        }
        miUbicacion = mMap.addMarker(new MarkerOptions().position(lugar).title(titulo).
                icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lugar, AppMediador.ZOOM));
    }

    @Override
    public void mostrarVehiculos(Object informacion) {

        if(!ubicacionConductores.isEmpty()){
            ubicacionConductores.clear();
        }

        Object[] vehiculos = (Object[])informacion;
        for(Object vehiculo: vehiculos){

            Marker ubicacionVehiculo;
            Object[] markVehiculo = (Object[])vehiculo;
            Double latitud = (Double) markVehiculo[0];
            Double longitud = (Double) markVehiculo[1];
            String nombre = (String) markVehiculo[2];
            LatLng lugar = new LatLng(latitud,longitud);

            ubicacionVehiculo = mMap.addMarker(new MarkerOptions().position(lugar).title(nombre)
                    .icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)));
            ubicacionConductores.add(ubicacionVehiculo);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.BuscarVehiculos:
                break;
        }
    }

    @Override
    public void onDestroy() {
        mMap = null;
        mapFragment = null;
        miUbicacion = null;
        super.onDestroy();
        appMediador.removePresentadorOTGPasajero();
    }
}
