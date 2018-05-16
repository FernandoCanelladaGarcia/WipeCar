package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaMapaOrigen;

public class PresentadorMapaOrigen implements IPresentadorMapaOrigen{

    private AppMediador appMediador;
    private VistaMapaOrigen vistaMapaOrigen;
    private Marker marcaMapa;
    private IModelo modelo;

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GUARDADA)){
                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA,false);
                if(resultado){
                    vistaMapaOrigen.cerrarProgreso();
                    vistaMapaOrigen.mostrarDialogo(0);
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

    }
}
