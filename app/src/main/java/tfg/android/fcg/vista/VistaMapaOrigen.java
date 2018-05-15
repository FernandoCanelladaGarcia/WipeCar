package tfg.android.fcg.vista;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.presentador.IPresentadorMapaOrigen;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaMapaOrigen extends FragmentActivity implements OnMapReadyCallback ,IVistaMapaOrigen, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Marker miUbicacion;
    private AppMediador appMediador;
    private IPresentadorMapaOrigen presentadorMapaOrigen;
    private int anchoPantalla, altoPantalla;

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

        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        Log.i(TAG,"Mapa Origen");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(mMap != null){
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    presentadorMapaOrigen.tratarOrigen(latLng);
                }
            });
        }
        presentadorMapaOrigen.iniciar();
        Log.i(TAG,"onMapReady");
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

    }
    
    @Override
    public void mostrarOrigen(Object informacion) {

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
