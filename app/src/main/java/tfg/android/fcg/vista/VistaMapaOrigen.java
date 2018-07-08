package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.presentador.IPresentadorMapaOrigen;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaMapaOrigen extends FragmentActivity implements OnMapReadyCallback, IVistaMapaOrigen, GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private Marker miUbicacion;
    private AppMediador appMediador;
    private IPresentadorMapaOrigen presentadorMapaOrigen;
    private LatLng miLatLng;
    private String miOrigen;
    private int anchoPantalla, altoPantalla;
    private boolean porDefecto;
    private boolean deshabilitoBack = true;
    private final static String TAG = "depurador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_mapaorigen);
        appMediador = (AppMediador) this.getApplication();
        appMediador.setVistaMapaOrigen(this);
        presentadorMapaOrigen = appMediador.getPresentadorMapaOrigen();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;
        miLatLng = null;
        miOrigen = null;
        porDefecto = false;
        mostrarDialogo(0);
        Log.i(TAG, "Mapa Origen");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    miLatLng = latLng;
                    presentadorMapaOrigen.tratarSeleccionarOrigen(1);
                }
            });
        }
        Log.i(TAG, "onMapReady");
        presentadorMapaOrigen.iniciar();
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
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        final View dialogoOrigen = getLayoutInflater().inflate(R.layout.layout_origen_destino, null);
        switch (tarea) {
            //Inicio
            case 0:
                Log.i(TAG, "DIALOGO INICIO");
                dialogBuild.setView(dialogoOrigen);
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 1:
                dialogBuild.setTitle("Origen obtenido");
                dialogBuild.setMessage("¿Esta seguro de la ubicacion?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object[] posicion = new Object[4];
                        posicion[1] = miLatLng.latitude;
                        posicion[2] = miLatLng.longitude;
                        posicion[3] = 0;
                        presentadorMapaOrigen.tratarOrigen(posicion);
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 2:
                Log.i(TAG, "DIALOGO FINAL");
                dialogBuild.setView(dialogoOrigen);
                dialogo = dialogBuild.create();
                dialogo.show();
                Button buttonOrigen = dialogo.findViewById(R.id.buttonOrigenD);
                buttonOrigen.setEnabled(false);
                Button buttonDefault = dialogo.findViewById(R.id.botonPorDefecto);
                buttonDefault.setEnabled(false);
                break;
            case 3:
                dialogBuild.setTitle("Origen y destino");
                dialogBuild.setMessage("¿Esta seguro de su elección?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presentadorMapaOrigen.tratarSalirMapa();
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonOrigenD:
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.mapOrigen);
                mapFragment.getMapAsync(this);
                break;
            case R.id.botonGuardarOyD:
                if (miLatLng == null && !porDefecto) {
                    Toast.makeText(getApplicationContext(),
                            "Por favor, seleccione un origen", Toast.LENGTH_SHORT).show();
                } else {
                    Spinner destinos = dialogo.findViewById(R.id.spinnerDestino);
                    if (destinos.getSelectedItem().toString().equals("DESTINOS")) {
                        Toast.makeText(getApplicationContext(),
                                "Por favor, seleccione un destino", Toast.LENGTH_SHORT).show();
                    } else if(!porDefecto){
                        String d = destinos.getSelectedItem().toString();
                        Object[] origenDestino = new Object[2];
                        origenDestino[1] = d;
                        presentadorMapaOrigen.tratarOrigenYDestino(origenDestino);
                    } else {
                        Log.i(TAG,"POR DEFECTO");
                        String d = destinos.getSelectedItem().toString();
                        Object[] origenDestino = new Object[2];
                        origenDestino[0] = "default";
                        origenDestino[1] = d;
                        presentadorMapaOrigen.tratarOrigenYDestino(origenDestino);
                    }
                }
                break;
            case R.id.botonPorDefecto:
                Toast.makeText(getApplicationContext(),
                        "Se ha tomado como origen su dirección", Toast.LENGTH_LONG).show();
                Button buttonOrigen = dialogo.findViewById(R.id.buttonOrigenD);
                buttonOrigen.setEnabled(false);
                Button buttonDefault = dialogo.findViewById(R.id.botonPorDefecto);
                buttonDefault.setEnabled(false);
                porDefecto = true;
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

        LatLng lugar = new LatLng(latitud, longitud);
        if (titulo.equals("Mi Ubicación")) {
            if (miUbicacion != null) {
                miUbicacion.remove();
            }
            miUbicacion = mMap.addMarker(new MarkerOptions().position(lugar).title(titulo).
                    icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lugar, AppMediador.ZOOM));
        }
    }

    @Override
    public void mostrarOrigen(Object informacion) {

    }

    @Override
    public void onBackPressed(){
        if(deshabilitoBack){

        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appMediador.removePresentadorMapaOrigen();
    }
}
