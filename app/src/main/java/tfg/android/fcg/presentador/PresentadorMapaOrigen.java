package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
                Object[] resultado = (Object[])intent.getSerializableExtra(AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA);
                if (resultado != null) {
                    appMediador.unRegisterReceiver(this);
                    vistaMapaOrigen.cerrarProgreso();
                    vistaMapaOrigen.mostrarDialogo(2);
                    appMediador.registerReceiver(receptorDeTraduccion, AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION);
                    resultado[3] = 1;
                    modelo.guardarLocalizacion(resultado);
                }
            }
            if (intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)) {
                Object[] datos = (Object[]) intent.getExtras().getSerializable(AppMediador.CLAVE_ACTUALIZACION_USUARIO);
                if ((boolean) datos[0] && datos[1].equals("origenydestino")) {
                    appMediador.unRegisterReceiver(this);
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
                vistaMapaOrigen.cerrarProgreso();
                vistaMapaOrigen.mostrarMapaConPosicion(posicion);
            }
        }
    };

    private BroadcastReceiver receptorDeTraduccion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION)) {
                appMediador.unRegisterReceiver(this);
                Bundle extras = intent.getExtras();
                if (extras.getString(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION) != null){
                    miOrigen = extras.getString(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION);
                    Log.i(TAG, miOrigen);
                    appMediador.stopService(ServicioLocalizacion.class);
                }
            }else{
                Log.i(TAG,"No furrula");
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
        modelo.iniciarGps();
    }

    @Override
    public void tratarOrigen(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_LOCALIZACION_GUARDADA);
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
        vistaMapaOrigen.finish();
    }

    @Override
    public void tratarOrigenYDestino(Object informacion) {
        vistaMapaOrigen.cerrarDialogo();
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_ACTUALIZACION_USUARIO);
        Object[] origenDestino = (Object[]) informacion;
        if(origenDestino[0] == "default"){
            SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
            String origenDefault = sharedPreferences.getString("origendef",null);
            Log.i(TAG, origenDefault);
            origenDestino[0] = origenDefault;
            vistaMapaOrigen.mostrarProgreso();
            modelo.guardarOrigenYDestino(origenDestino);
        }else{
            origenDestino[0] = miOrigen;
            Log.i(TAG, origenDestino[0].toString() +" "+ origenDestino[1].toString());
            vistaMapaOrigen.mostrarProgreso();
            modelo.guardarOrigenYDestino(origenDestino);
        }
    }
}
