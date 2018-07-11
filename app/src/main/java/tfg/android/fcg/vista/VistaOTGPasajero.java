package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Posicion;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.presentador.IPresentadorOTGPasajero;

public class VistaOTGPasajero extends Fragment implements IVistaOTGPasajero, OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Marker miUbicacion;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorOTGPasajero presentadorOTGPasajero;
    private AlertDialog.Builder dialogBuild;
    private Button botonBuscar;
    private Usuario user;
    private int anchoPantalla, altoPantalla;

    private ArrayList<Marker> ubicacionConductores;
    private ArrayList<Vinculo> conductoresEnRuta;
    private ArrayList<Usuario> conductores;
    private ArrayList<Posicion> posiciones;
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
        ubicacionConductores = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        mMap = null;
        botonBuscar = (Button) view.findViewById(R.id.BuscarVehiculos);
        botonBuscar.setOnClickListener(this);
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
                    String idUser = marker.getTitle();
                    Toast.makeText(appMediador.getApplicationContext(),idUser, Toast.LENGTH_LONG).show();
//                    for(Usuario conductor : conductores){
//                        if(conductor.getIdUser().equals(idUser));
//                        presentadorOTGPasajero.tratarVehiculo(conductor);
//                    }
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
    public void mostrarVehiculos() {
        Log.i(TAG,"Markers: " + ubicacionConductores.size());

        if(!ubicacionConductores.isEmpty()){
            moverVehiculos();
        }else {
            Log.i(TAG, "Markers: " + ubicacionConductores.size());
            for (int i = 0; i < conductores.size(); i++) {
                Log.i(TAG, "Situando posicion");
                Marker ubicacionVehiculo;
                Posicion posicionConductor = posiciones.get(i);
                Usuario conductor = conductores.get(i);

                Double latitud = Double.parseDouble(posicionConductor.getLatitud());
                Double longitud = Double.parseDouble(posicionConductor.getLongitud());
                String titulo = conductor.getIdUser();
                LatLng lugar = new LatLng(latitud, longitud);

                ubicacionVehiculo = mMap.addMarker(new MarkerOptions().position(lugar).title(titulo)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car_user)));
                ubicacionConductores.add(ubicacionVehiculo);
            }
        }
        appMediador.getPresentadorOTGPasajero().obtenerPosicionConductores(conductores);
    }

    private void moverVehiculos(){

        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        for(int i = 0; i < conductores.size(); i++){

            final Marker vehiculo = ubicacionConductores.get(i);
            //Posicion Actual vehiculo
            final LatLng posicionVehiculo = vehiculo.getPosition();
            Location locatVehiculo = new Location("vehiculo");
            locatVehiculo.setLatitude(posicionVehiculo.latitude);
            locatVehiculo.setLongitude(posicionVehiculo.longitude);
            //Posicion Nueva vehiculo
            Posicion nuevaPosicion = posiciones.get(i);
            final LatLng posicionFinal = new LatLng(Double.parseDouble(nuevaPosicion.getLatitud()),Double.parseDouble(nuevaPosicion.getLongitud()));
            Location locatFinal = new Location("final");
            locatFinal.setLatitude(posicionFinal.latitude);
            locatFinal.setLongitude(posicionFinal.longitude);
            //Distancia entre posiciones
            float distance = locatVehiculo.distanceTo(locatFinal);

            if(distance > 100) {
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    long elapsed;
                    float t;
                    float v;

                    @Override
                    public void run() {
                        elapsed = SystemClock.uptimeMillis() - start;
                        t = elapsed / durationInMs;
                        v = interpolator.getInterpolation(t);

                        LatLng currentPosition = new LatLng(
                                posicionVehiculo.latitude * (1 - t) + (posicionFinal.latitude) * t,
                                posicionVehiculo.longitude * (1 - t) + (posicionFinal.longitude) * t
                        );

                        vehiculo.setPosition(currentPosition);

                        if (t < 1) {
                            handler.postDelayed(this, 16);
                        } else {
                            if (hideMarker) {
                                vehiculo.setVisible(false);
                            } else {
                                vehiculo.setVisible(true);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.BuscarVehiculos:
                user = appMediador.getVistaPrincipal().getUsuario();
                appMediador.getPresentadorOTGPasajero().tratarBuscar(user.getDestino());
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

    public void setConductoresEnRuta(Object informacion){
        ArrayList<Vinculo> getCondVinc = (ArrayList<Vinculo>) informacion;
        if(!getCondVinc.isEmpty()){
            if(conductoresEnRuta == null){
                Log.i(TAG, "set setConductoresEnRuta new");
                conductoresEnRuta = getCondVinc;
            }else{
                conductoresEnRuta = null;
                conductoresEnRuta = getCondVinc;
            }
            appMediador.getPresentadorOTGPasajero().obtenerConductores(conductoresEnRuta);
        }else{
            conductoresEnRuta = new ArrayList<>();

        }
    }

    public void setConductores(Object informacion){
        ArrayList<Usuario> getCond = (ArrayList<Usuario>)informacion;
        if(!getCond.isEmpty()){
            if(conductores == null){
                Log.i(TAG, "set setConductores new");
                conductores = getCond;
            }else{
                conductores = null;
                conductores = getCond;
            }
            appMediador.getPresentadorOTGPasajero().obtenerPosicionConductores(conductores);
        }else{
            conductores = new ArrayList<>();
        }
    }

    public void setPosiciones(Object informacion){
        ArrayList<Posicion> getPos = (ArrayList<Posicion>)informacion;
        if(!getPos.isEmpty()){
            if(posiciones == null){
                Log.i(TAG, "set setPosiciones new");
                posiciones = getPos;
            }else{
                posiciones = null;
                posiciones = getPos;
            }
            mostrarVehiculos();
        }else{
            posiciones = new ArrayList<>();
        }
    }
}
