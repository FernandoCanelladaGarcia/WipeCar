package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseUser;

import java.util.TimerTask;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Vinculo;
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
    private BroadcastReceiver receptorLocalizacion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GUARDADA)){
                if(intent.getExtras().getBoolean(
                        AppMediador.CLAVE_RESULTADO_LOCALIZACION_GUARDADA)){


                }else{
                    //TODO: PARAR APLICACION
                    //informe sonoro problemas
                }
            }
        }
    };
    //TODO Nota: Timer seteara 30seg donde el usuario debe aceptar/rechazar la peticion
    private BroadcastReceiver receptorPeticiones = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_PETICION_OTGCONDUCTOR)){
                if(intent.getExtras().getSerializable(
                        AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR) != null){
                    atendiendoPeticion = true;
                    Vinculo vinculo = (Vinculo)intent.getSerializableExtra(
                            AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR);
                    //informe sonoro datos del pasajero
                    //poner timer
                }else{
                    //TODO: NO HAY NADIE
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR)){
                if(intent.getExtras().getSerializable(
                        AppMediador.CLAVE_ACEPTAR_PETICION_OTGCONDUCTOR) != null){
                        //informe sonoro recoger pasajero
                    Vinculo vinculo = (Vinculo) intent.getSerializableExtra(
                            AppMediador.CLAVE_ACEPTAR_PETICION_OTGCONDUCTOR);
                    buscarPeticiones(vinculo.getIdConductor());
                }else{
                    //TODO: PARAR APLICACION
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR)){
                if(intent.getExtras().getString(
                        AppMediador.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR) != null){
                    //informe sonoro no recoger pasajero
                    String idConductor = intent.getStringExtra(
                            AppMediador.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR);
                    buscarPeticiones(idConductor);
                }else{
                    //TODO: PARAR APLICACION
                }
            }
        }
    };

    private BroadcastReceiver receptorGPS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)){
                if(intent.getExtras() != null){
                Object[] posicion= new Object[3];
                posicion[1] = intent.getSerializableExtra(AppMediador.CLAVE_LATITUD);
                posicion[2] = intent.getSerializableExtra(AppMediador.CLAVE_LONGITUD);
                guardarPosicion(posicion);
                }else{
                //TODO: PARAR APLICACION
                }
            }
        }
    };

    @Override
    public void iniciar(final Object informacion) {

        //1er hilo, busca posicion usuario

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run(){
                buscarPosicion(informacion);
            }
        });

        //2o hilo, busca peticiones

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run(){
                buscarPeticiones(informacion);
            }
        });
        thread1.start();
        thread2.start();
    }

    @Override
    public void tratarAceptar(Object informacion) {
        if(atendiendoPeticion){
//           Parar timer
            aceptarPeticion(informacion);
            atendiendoPeticion = false;
        }
    }

    @Override
    public void tratarRechazar(Object informacion) {
        if(atendiendoPeticion){
//            Parar timer
            rechazarPeticion(informacion);
            atendiendoPeticion = false;
        }
    }

    @Override
    public void tratarParar(Object informacion) {

    }

    private void guardarPosicion(Object[] nuevaLocalizacion){
        AppMediador.getInstance().registerReceiver(receptorLocalizacion,AppMediador.AVISO_LOCALIZACION_GUARDADA);
        modelo.guardarLocalizacion(nuevaLocalizacion);
    }

    private void buscarPeticiones(Object informacion){
        AppMediador.getInstance().registerReceiver(receptorPeticiones,AppMediador.AVISO_PETICION_OTGCONDUCTOR);
        modelo.obtenerPeticionesDePasajeros(informacion);
    }

    private void buscarPosicion(Object informacion){
        AppMediador.getInstance().registerReceiver(receptorGPS,AppMediador.AVISO_LOCALIZACION_GPS);
        modelo.obtenerPosicionUsuario(informacion);
    }

    private void aceptarPeticion(Object informacion){
        AppMediador.getInstance().registerReceiver(receptorPeticiones,AppMediador.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR);
        modelo.aceptarPasajero(informacion);
    }
    
    private void rechazarPeticion(Object informacion){
        AppMediador.getInstance().registerReceiver(receptorPeticiones,AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR);
        modelo.rechazarPasajero(informacion);
    }
}
