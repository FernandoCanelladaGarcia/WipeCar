package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.bajonivel.ServicioLocalizacion;
import tfg.android.fcg.vista.VistaMapaOrigen;
import tfg.android.fcg.vista.VistaPrincipal;

public class PresentadorMapaOrigen implements IPresentadorMapaOrigen {

    private AppMediador appMediador;
    private VistaMapaOrigen vistaMapaOrigen;
    private String miOrigen;
    private IModelo modelo;

    private final static String TAG = "depurador";

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GUARDADA)) {
                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA, false);
                if (resultado) {
                    vistaMapaOrigen.cerrarProgreso();
                    vistaMapaOrigen.mostrarDialogo(2);
                }
            }
            if (intent.getAction().equals(AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION)) {
                Bundle extras = intent.getExtras();
                if (extras.getInt(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION) == 0) {
                    //Fallo
                }
                if (!extras.getBoolean(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION, false)) {
                    //Fallo
                } else {
                    miOrigen = extras.getString(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION);
                    Log.i(TAG, miOrigen);
                }
            }
            if (intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)) {
                Object[] datos = (Object[]) intent.getExtras().getSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO);
                if ((boolean) datos[0] && datos[1].equals("origenydestino")) {
                    vistaMapaOrigen.cerrarProgreso();
                    vistaMapaOrigen.mostrarDialogo(3);
                }
            }
        }
    };

    private BroadcastReceiver receptorGPS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)) {
                appMediador.unRegisterReceiver(this);
                Object[] posicion = new Object[3];
                posicion[0] = intent.getSerializableExtra(AppMediador.CLAVE_LATITUD);
                posicion[1] = intent.getSerializableExtra(AppMediador.CLAVE_LONGITUD);
                posicion[2] = "Mi Ubicación";
                Log.i(TAG, "latitud: " + posicion[0] + " longitud: " + posicion[1]);
                vistaMapaOrigen.mostrarMapaConPosicion(posicion);
            }
        }
    };

    public PresentadorMapaOrigen() {
        appMediador = AppMediador.getInstance();
        vistaMapaOrigen = (VistaMapaOrigen) appMediador.getVistaMapaOrigen();
        modelo = Modelo.getInstance();
    }

    @Override
    public void iniciar() {
        appMediador.registerReceiver(receptorGPS, AppMediador.AVISO_LOCALIZACION_GPS);
        vistaMapaOrigen.cerrarDialogo();
        vistaMapaOrigen.mostrarProgreso();
        modelo.obtenerPosicionUsuario();
    }

    @Override
    public void tratarOrigen(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_LOCALIZACION_GUARDADA);
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION);
        vistaMapaOrigen.cerrarDialogo();
        vistaMapaOrigen.mostrarProgreso();
        modelo.guardarLocalizacion((Object[]) informacion);
    }

    @Override
    public void tratarSeleccionarOrigen(Object informacion) {
        vistaMapaOrigen.mostrarDialogo(1);
    }

    @Override
    public void tratarSalirMapa() {
        appMediador.launchActivity(VistaPrincipal.class, this, null);
    }

    @Override
    public void tratarOrigenYDestino(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_ACTUALIZACION_USUARIO);
        Object[] origenDestino = (Object[]) informacion;
        origenDestino[0] = miOrigen;
        vistaMapaOrigen.mostrarProgreso();
        modelo.guardarOrigenYDestino(origenDestino);
    }
}
