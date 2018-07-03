package tfg.android.fcg.vista;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.presentador.IPresentadorOTGPasajero;

public class VistaOTGPasajero extends Fragment implements IVistaOTGPasajero, OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker miUbicacion;
    private List<Marker> ubicacionConductores;
    private AppMediador appMediador;
    private IPresentadorOTGPasajero presentadorOTGPasajero;
    private int anchoPantalla, altoPantalla;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_vista_otgpasajero);
//        appMediador = (AppMediador)this.getApplication();
//        appMediador.setVistaOTGPasajero(this);
//        presentadorOTGPasajero = appMediador.getPresentadorOTGPasajero();
//        miUbicacion = null;
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        anchoPantalla = size.x;
//        altoPantalla = size.y;
//        //TODO
//        //SupportMapFragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
//        //mapFragment.getMapAsync(this);
        return layoutInflater.inflate(R.layout.fragment_otg, container, false);
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
        presentadorOTGPasajero.iniciar();
    }

    @Override
    public void mostrarProgreso() {

    }

    @Override
    public void cerrarProgreso() {

    }

    @Override
    public void mostrarDialogo(Object informacion) {

    }

    @Override
    public void cerrarDialogo() {

    }

    @Override
    public void mostrarMapaConPosicion(Object informacion) {
        Object[] posicion = (Object[]) informacion;
        Double latitud = (Double) posicion[0];
        Double longitud = (Double) posicion[1];
        String titulo = (String) posicion[2];
        LatLng lugar = new LatLng(latitud,longitud);

        if(titulo.equals("Mi posicion")){
            if(miUbicacion != null){
                miUbicacion.remove();
            }
            miUbicacion = mMap.addMarker(new MarkerOptions().position(lugar).title(titulo).
                    icon(BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lugar,AppMediador.ZOOM));
        }else{
            mMap.addMarker(new MarkerOptions().position(lugar).title(titulo));
        }

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

}
