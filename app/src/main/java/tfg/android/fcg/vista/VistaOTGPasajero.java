package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.graphics.Point;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Posicion;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
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

    private Posicion posicionConductorVinculo;
    private Vinculo vinculoConductor;
    private Usuario conductorVinculo;
    private Marker marcadorConductor;
    private Vehiculo vehiculoConductor;

    private boolean vinculoExiste = false;
    private final static String TAG = "depurador";

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View v = layoutInflater.inflate(R.layout.layout_vista_otgpasajero,container,false);
        mMap = null;
        mapFragment = null;
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;
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
        if (mMap != null) {
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    String idUser = marker.getTitle();
                    Toast.makeText(appMediador.getApplicationContext(), idUser, Toast.LENGTH_LONG).show();
                    for (int i = 0; i < conductores.size(); i++) {
                        if (conductores.get(i).getIdUser().equals(idUser)) {
                            marcadorConductor = marker;
                            conductorVinculo = conductores.get(i);
                            posicionConductorVinculo = posiciones.get(i);
                            //presentadorOTGPasajero.tratarVehiculo(conductorVinculo);
                            //mostrarVehiculoVinculo();
                        }
                    }
                    //presentadorOTGPasajero.tratarVehiculo(conductorVinculo);
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
        int tarea = (int)informacion;
        switch(tarea) {
            case 0:
                Projection projection = mMap.getProjection();
                Point punto = projection.toScreenLocation(marcadorConductor.getPosition());
                dialogBuild = new AlertDialog.Builder(appMediador.getApplicationContext());
                View vistaDialogo = getLayoutInflater().inflate(R.layout.menu_vehiculo, null);
                dialogBuild.setView(vistaDialogo);

                Button botonSeleccionarVehiculo = (Button) vistaDialogo.findViewById(R.id.botonSeleccionarVehiculo);

                TextView nombreConductor = (TextView) vistaDialogo.findViewById(R.id.nombreConductor);
                nombreConductor.setText(conductorVinculo.getNombre());

                TextView cocheConductor = (TextView) vistaDialogo.findViewById(R.id.cocheConductor);
                cocheConductor.setText(vehiculoConductor.getMarca() + "-" + vehiculoConductor.getModelo() + " / "+ vehiculoConductor.getMatricula());
                TextView valoracionConductor = (TextView) vistaDialogo.findViewById(R.id.valoracionMenuConductor);
                if (conductorVinculo.getValoracion().isEmpty()) {
                    valoracionConductor.setText("Sin Valorar");
                } else {
                    valoracionConductor.setText(conductorVinculo.getValoracion());
                }


                botonSeleccionarVehiculo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mostrarVehiculoVinculo();
                        //AGREGAR VINCULO -- PASAR A OTGCONDUCTOR Y RECIBIR NOTIFICACION
                        //presentadorOTGPasajero.tratarOk();
                    }
                });

                dialogo = dialogBuild.create();
                dialogo.show();

                WindowManager.LayoutParams wmlp = dialogo.getWindow().getAttributes();
                wmlp.copyFrom(dialogo.getWindow().getAttributes());
                wmlp.width = 180;
                wmlp.x = punto.x - anchoPantalla / 2;
                wmlp.y = punto.y - altoPantalla / 2;
                if (punto.x > anchoPantalla / 2)
                    wmlp.x -= wmlp.width / 2;
                else
                    wmlp.x += wmlp.width / 2;
                dialogo.getWindow().setAttributes(wmlp);
                break;
        }
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
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
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
                builder.include(ubicacionVehiculo.getPosition());
            }

            builder.include(miUbicacion.getPosition());
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,200);
            mMap.moveCamera(cu);
            //mMap.animateCamera(CameraUpdateFactory.zoomBy(AppMediador.ZOOM));
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i(TAG,"Aplicamos un delay de 5 seg");
                appMediador.getPresentadorOTGPasajero().obtenerPosicionConductores(conductores);
            }
        }, 5000);

    }

    private void mostrarVehiculoVinculo(){

        Log.i(TAG,"mostrarVehiculoVinculo");

        for(Marker ubicacion: ubicacionConductores){
            ubicacion.remove();
        }
        ubicacionConductores.clear();

        for(Usuario conductor: conductores){
            conductores.remove(conductor);
        }
        conductores.clear();

        for(Vinculo vinculo: conductoresEnRuta){
            conductoresEnRuta.remove(vinculo);
        }
        conductoresEnRuta.clear();

        for(Posicion posicion: posiciones){
            posiciones.remove(posicion);
        }
        posiciones.clear();

        Log.i(TAG, "Listas ubicaciones y conductores limpiadas");

        ubicacionConductores.add(marcadorConductor);
        conductores.add(conductorVinculo);
        posiciones.add(posicionConductorVinculo);
        //OBTENER VINCULO CREADO

        Double latitud = Double.parseDouble(posicionConductorVinculo.getLatitud());
        Double longitud = Double.parseDouble(posicionConductorVinculo.getLongitud());
        String titulo = conductorVinculo.getIdUser();
        LatLng lugar = new LatLng(latitud, longitud);

        marcadorConductor = mMap.addMarker(new MarkerOptions().position(lugar).title(titulo)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car_user)));

        Log.i(TAG,"Sigo obteniendo la posicion del conductor elegido");

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

            if(distance > 200) {
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
//        final LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(vehiculo.getPosition());
//        builder.include(miUbicacion.getPosition());
//        LatLngBounds bounds = builder.build();
//
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,0);
//
//        mMap.moveCamera(cu);
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

    public void setVehiculoConductor(Object informacion){
        Vehiculo getVehic = (Vehiculo)informacion;
        if(getVehic == null){
            //ERROR
        }else{
            vehiculoConductor = null;
            vehiculoConductor = getVehic;
            mostrarDialogo(0);
        }
    }
}
