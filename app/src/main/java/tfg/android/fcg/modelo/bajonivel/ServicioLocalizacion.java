package tfg.android.fcg.modelo.bajonivel;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.Posicion;

public class ServicioLocalizacion extends Service implements LocationListener {

    private static final long DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES = 0;
    private static final long TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES = 0;

    boolean gpsEstaHabilitado = false;
    protected LocationManager locationManager;
    private AppMediador appMediador;

    private final static String TAG = "depurador";

    @Override
    public void onCreate() {
        appMediador = AppMediador.getInstance();
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        gpsEstaHabilitado = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsEstaHabilitado) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.i(TAG, "Error Localizacion");
                return;
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    TIEMPO_MINIMO_ENTRE_ACTUALIZACIONES,
                    DISTANCIA_MINIMA_ENTRE_ACTUALIZACIONES, this);
        }
        Log.i(TAG, "Servicio Localizacion iniciado");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            Log.i(TAG, "Location Changed");
            Bundle extras = new Bundle();
            extras.putDouble(AppMediador.CLAVE_LATITUD, location.getLatitude());
            extras.putDouble(AppMediador.CLAVE_LONGITUD, location.getLongitude());
            appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_GPS, extras);
        }
        if(location == null){
            Bundle extras = null;
            appMediador.sendBroadcast(AppMediador.AVISO_LOCALIZACION_GPS, extras);
        }
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
}
