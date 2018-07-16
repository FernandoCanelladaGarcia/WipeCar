package tfg.android.fcg.modelo.bajonivel;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.Posicion;

public class ServicioLocalizacion extends Service implements LocationListener {

    private static final long DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES = 0;
    private static final long TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES = 0;
    boolean gpsEstaHabilitado = false;
    protected LocationManager locationManager;
    private AppMediador appMediador;
    private String mprovider;
    private final static String TAG = "depurador";

    @Override
    public void onCreate() {
        appMediador = AppMediador.getInstance();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mprovider = locationManager.getBestProvider(criteria, false);
        gpsEstaHabilitado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.i("depurador", "1" + gpsEstaHabilitado);
        if (gpsEstaHabilitado) {
                Log.i("depurador", "3");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.i(TAG, "Error Localizacion");
                    return;
                }
                Location location = locationManager.getLastKnownLocation(mprovider);
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES,
                        DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES, this);

                Log.i("depurador", "4");
                if (location != null)
                    onLocationChanged(location);
                else
                    Toast.makeText(getBaseContext(), "No Location Provider Found Check Your Code", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "Location Changed");
        Bundle extras = new Bundle();
        extras.putDouble(AppMediador.CLAVE_LATITUD, location.getLatitude());
        extras.putDouble(AppMediador.CLAVE_LONGITUD, location.getLongitude());
        appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_GPS, extras);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopSelf();
        locationManager.removeUpdates(this);
        locationManager = null;
        Log.i(TAG,"Servicio finalizado");
    }
}
