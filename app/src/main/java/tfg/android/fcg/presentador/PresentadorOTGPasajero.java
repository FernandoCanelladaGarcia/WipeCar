package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaOTGPasajero;

public class PresentadorOTGPasajero implements IPresentadorOTGPasajero{

    private AppMediador appMediador;
    private VistaOTGPasajero vistaOTGPasajero;
    private Marker miUbicacion;
    private Marker marcaSeleccionada;
    private IModelo modelo;

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_VEHICULOS_OTGPASAJERO)){
                ArrayList<Object> vehiculos = (ArrayList<Object>) intent.getSerializableExtra(AppMediador.CLAVE_VEHICULOS_OTGPASAJERO);
                AppMediador.getInstance().getVistaOTGPasajero().mostrarVehiculos(vehiculos);
            }
        }
    };

    private BroadcastReceiver receptorGps = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)){
                Object[] posicion = new Object[3];
                posicion[0] = intent.getSerializableExtra(AppMediador.CLAVE_LATITUD);
                posicion[1] = intent.getSerializableExtra(AppMediador.CLAVE_LONGITUD);
                posicion[3] = "Mi posicion";
                AppMediador.getInstance().getVistaOTGPasajero().mostrarMapaConPosicion(posicion);
            }
        }
    };

    public PresentadorOTGPasajero(){
        appMediador = AppMediador.getInstance();
        vistaOTGPasajero = (VistaOTGPasajero) appMediador.getVistaOTGPasajero();
        modelo = Modelo.getInstance();
    }

    @Override
    public void iniciar() {
        appMediador.registerReceiver(receptorGps,AppMediador.AVISO_LOCALIZACION_GPS);
        modelo.iniciarGps();
    }

    @Override
    public void tratarBuscar(Object informacion) {

    }

    @Override
    public void tratarVehiculo(Object informacion) {

    }

    @Override
    public void tratarOk(Object informacion) {

    }

    @Override
    public void tratarCancelar(Object informacion) {

    }
}
