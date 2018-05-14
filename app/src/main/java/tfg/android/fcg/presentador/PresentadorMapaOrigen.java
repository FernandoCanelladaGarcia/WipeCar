package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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

        }
    };

    public PresentadorMapaOrigen(){
        appMediador = AppMediador.getInstance();
        vistaMapaOrigen = (VistaMapaOrigen) appMediador.getVistaMapaOrigen();
        modelo = Modelo.getInstance();
    }
    @Override
    public void iniciar() {

    }

    @Override
    public void tratarOrigen(Object informacion) {

    }

    @Override
    public void tratarSeleccionarOrigen(Object informacion) {

    }

    @Override
    public void tratarSalirMapa(Object informacion) {

    }

    @Override
    public void tratarOrigenYDestino(Object informacion) {

    }
}
