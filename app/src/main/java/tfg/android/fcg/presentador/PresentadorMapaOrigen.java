package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaMapaOrigen;

public class PresentadorMapaOrigen implements IPresentadorMapaOrigen{

    private AppMediador appMediador;
    private VistaMapaOrigen vistaMapaOrigen;
    private String miOrigen;
    private IModelo modelo;

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GUARDADA)){
                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA,false);
                if(resultado){
                    vistaMapaOrigen.cerrarProgreso();
                    vistaMapaOrigen.mostrarDialogo(2);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION)){
                Bundle extras = intent.getExtras();
                if(extras.getInt(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION) == 0){
                    //Fallo
                }if(!extras.getBoolean(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION,false)){
                    //Fallo
                }else{
                    miOrigen = extras.getString(AppMediador.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION);
                }
            }
        }
    };

    private BroadcastReceiver receptorGPS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)){
                if(intent.getExtras() != null){
                    Object[] posicion = new Object[3];
                    posicion[0] = intent.getSerializableExtra(AppMediador.CLAVE_LATITUD);
                    posicion[1] = intent.getSerializableExtra(AppMediador.CLAVE_LONGITUD);
                    posicion[2] = "Mi Ubicación";
                    vistaMapaOrigen.cerrarProgreso();
                    vistaMapaOrigen.mostrarMapaConPosicion(posicion);
                }
            }
        }
    };

    public PresentadorMapaOrigen(){
        appMediador = AppMediador.getInstance();
        vistaMapaOrigen = (VistaMapaOrigen) appMediador.getVistaMapaOrigen();
        modelo = Modelo.getInstance();
    }
    @Override
    public void iniciar() {
        AppMediador.getInstance().registerReceiver(receptorGPS,AppMediador.AVISO_LOCALIZACION_GPS);
        vistaMapaOrigen.cerrarDialogo();
        vistaMapaOrigen.mostrarProgreso();
        modelo.obtenerPosicionUsuario();
    }

    @Override
    public void tratarOrigen(Object informacion) {
        AppMediador.getInstance().registerReceiver(receptorDeAvisos,AppMediador.AVISO_LOCALIZACION_GUARDADA);
        AppMediador.getInstance().registerReceiver(receptorDeAvisos,AppMediador.AVISO_RESULTADO_TRADUCIR_LOCALIZACION);
        vistaMapaOrigen.cerrarDialogo();
        vistaMapaOrigen.mostrarProgreso();
        modelo.guardarLocalizacion((Object[])informacion);
    }

    @Override
    public void tratarSeleccionarOrigen(Object informacion) {
        vistaMapaOrigen.mostrarDialogo(1);
    }

    @Override
    public void tratarSalirMapa(Object informacion) {

    }

    @Override
    public void tratarOrigenYDestino(Object informacion) {
        Object[] origenDestino = (Object[])informacion;
        origenDestino[0] = miOrigen;
        vistaMapaOrigen.mostrarProgreso();
        modelo.guardarOrigenYDestino(origenDestino);
    }
}
