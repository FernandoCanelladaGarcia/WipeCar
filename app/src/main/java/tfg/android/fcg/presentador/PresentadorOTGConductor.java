package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.TimerTask;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaOTGConductor;

public class PresentadorOTGConductor implements IPresentadorOTGConductor{

    private AppMediador appMediador;
    private VistaOTGConductor vistaOTGConductor;
    private IModelo modelo;

    private TimerTask timer;
    private boolean atendiendoPeticion = false;

    public PresentadorOTGConductor(){
        appMediador = AppMediador.getInstance();
        vistaOTGConductor = (VistaOTGConductor) appMediador.getVistaOTGConductor();
        modelo = Modelo.getInstance();
    }
//TODO
    private BroadcastReceiver receptorLocalizacion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GUARDADA)){
//
//            }
        }
    };
//TODO
    private BroadcastReceiver receptorPeticiones = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if(intent.getAction().equals(AppMediador.AVISO_PETICION)){
//
//            }
//            if(intent.getAction().equals(AppMediador.AVISO_ACEPTAR)){
//
//            }
//            if(intent.getAction().equals(AppMediador.AVISO_RECHAZAR)){
//
//            }
        }
    };

    private BroadcastReceiver receptorGPS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)){

            }
        }
    };

    @Override
    public void iniciar(Object informacion) {
        //1er hilo, busca posicion usuario
//        Thread thread1 = new Thread(){
//            @Override
//            public void run(){
//                buscarPosicion();
//            }
//        }.start();
//        //2o hilo, busca peticiones
//        Thread thread2 = new Thread(){
//            @Override
//            public void run(){
//                buscarPeticiones();
//            }
//        }.start();

    }

    @Override
    public void tratarAceptar(Object informacion) {
        if(atendiendoPeticion){
//            pararTimer();
//            aceptarPeticion();
            atendiendoPeticion = false;
        }
    }

    @Override
    public void tratarRechazar(Object informacion) {
        if(atendiendoPeticion){
//            pararTimer();
//            rechazarPeticion();
            atendiendoPeticion = false;
        }
    }

    @Override
    public void tratarParar(Object informacion) {

    }

//    private void guardarPosicion(){
//        AppMediador.getInstance().registerReceiver(receptorLocalizacion,AppMediador.AVISO_LOCALIZACION_GUARDADA);
//        modelo.guardarLocalizacion(nuevaLocalizacion);
//    }
//
//    private void buscarPeticiones(){
//        AppMediador.getInstance().registerReceiver(receptorPeticiones,AppMediador.AVISO_PETICION);
//        modelo.obtenerPeticionesDePasajeros();
//    }
//
//    private void buscarPosicion(){
//        AppMediador.getInstance().registerReceiver(receptorGPS,AppMediador.AVISO_LOCALIZACION_GPS);
//        modelo.obtenerPosicionUsuario();
//    }
//
//    private void aceptarPeticion(){
//        AppMediador.getInstance().registerReceiver(receptorPeticiones,AppMediador.AVISO_ACEPTAR);
//        modelo.aceptarPasajero();
//    }
//    private void rechazarPeticion(){
//        AppMediador.getInstance().registerReceiver(receptorPeticiones,AppMediador.AVISO_RECHAZAR);
//        modelo.rechazarPasajero();
//    }
}
