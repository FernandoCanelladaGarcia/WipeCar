package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.vista.VistaOTGConductor;

public class PresentadorOTGConductor implements IPresentadorOTGConductor {

    private AppMediador appMediador;
    private VistaOTGConductor vistaOTGConductor;
    private IModelo modelo;
    private Usuario user;
    private Vinculo vinculo;
    private Handler timer = new Handler();
    private boolean atendiendoPeticion = false;
    private String pasajero = "";

    private final String TAG = "depurador";

    public PresentadorOTGConductor() {
        appMediador = AppMediador.getInstance();
        vistaOTGConductor = (VistaOTGConductor) appMediador.getVistaOTGConductor();
        modelo = Modelo.getInstance();
    }

    private BroadcastReceiver receptorLocalizacion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_POSICION)) {
                if (intent.getExtras().getBoolean(
                        AppMediador.CLAVE_RESULTADO_ACTUALIZACION_POSICION)) {
                    //Log.i(TAG, "receptorLocalizacion localizacion guardada");
                } else {
                    //TODO: PARAR APLICACION
                    //informe sonoro problemas
                }
            }
        }
    };

    private BroadcastReceiver receptorPeticiones = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(AppMediador.AVISO_PETICION_OTGCONDUCTOR)) {
                Vinculo peticion = (Vinculo) intent.getSerializableExtra(AppMediador.CLAVE_AVISO_PETICION_OTGCONDUCTOR);
                if(peticion != null){
                    vinculo = peticion;
                    atendiendoPeticion = true;
                    pasajero = vinculo.getIdConductor();
                    vistaOTGConductor.indicarPeticionPasajero(vinculo);
                    timer.postDelayed(limiteDeTiempo, 30000);
                    appMediador.unRegisterReceiver(this);
                } else {
                    //TODO: NO HAY NADIE
                    vistaOTGConductor.indicarPeticionPasajero(null);
                }
            }
            if (intent.getAction().equals(AppMediador.AVISO_CONCRETAR_VINCULO)) {
                if (intent.getExtras().getSerializable(
                        AppMediador.CLAVE_CONCRETAR_VINCULO) != null) {
                    //informe sonoro recoger pasajero
                    boolean respuesta = (boolean) intent.getSerializableExtra(AppMediador.CLAVE_CONCRETAR_VINCULO);
                    if(respuesta){
                        vistaOTGConductor.indicarPasajeroAceptado(vinculo);
                        appMediador.unRegisterReceiver(this);
                    }
                    //buscarPeticiones(user.getIdUser());
                } else {
                    //TODO: PARAR APLICACION
                }
            }
            if (intent.getAction().equals(AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR)) {
                boolean respuesta = (boolean) intent.getSerializableExtra(AppMediador.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR);
                if(respuesta){
                    vistaOTGConductor.indicarPasajeroRechazado(vinculo);
                    appMediador.unRegisterReceiver(this);
                } else {
                    //TODO: PARAR APLICACION
                }
                buscarPeticiones(user.getIdUser());
            }
            if(intent.getAction().equals(AppMediador.AVISO_TERMINAR_RUTA)){
                boolean respuesta = intent.getBooleanExtra(AppMediador.CLAVE_TERMINAR_RUTA,false);
                if(respuesta){
                    //appMediador.getVistaPrincipal().cerrarProgreso();
                    Log.i(TAG,"Ha finalizado su ruta correctamente");
                    appMediador.unRegisterReceiver(this);
                }else{
                    //TODO: PARAR APLICACION
                }
            }
        }
    };

    private BroadcastReceiver receptor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_CREACION_VINCULO)) ;
            boolean respuesta = (boolean) intent.getBooleanExtra(AppMediador.CLAVE_CREACION_VINCULO, false);
            if (respuesta) {
                Log.i(TAG, "receptor Creacion vinculo");
                appMediador.getVistaPrincipal().cerrarProgreso();
                iniciarThreds();
            } else {
                appMediador.getVistaPrincipal().cerrarProgreso();
            }
        }
    };

    private BroadcastReceiver receptorGPS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)) {
                if (intent.getExtras() != null) {
                    //Log.i(TAG, "receptorGPS localizacion recibida");
                    Object[] posicion = new Object[3];
                    posicion[0] = user.getIdUser();
                    posicion[1] = intent.getSerializableExtra(AppMediador.CLAVE_LATITUD);
                    posicion[2] = intent.getSerializableExtra(AppMediador.CLAVE_LONGITUD);
                    guardarPosicion(posicion);
                } else {
                    //TODO: PARAR APLICACION
                }
            }
        }
    };

    @Override
    public void iniciar(final Object informacion) {
        user = (Usuario) informacion;
        appMediador.registerReceiver(receptor, AppMediador.AVISO_CREACION_VINCULO);
        Object[] iniciar = new Object[7];
        iniciar[0] = "";
        iniciar[1] = user.getIdUser();
        iniciar[2] = "";
        iniciar[3] = "";
        iniciar[4] = user.getOrigenDef();
        iniciar[5] = user.getDestino();
        iniciar[6] = 1;
        modelo.guardarUsuarioPickup(iniciar);
    }

    private void iniciarThreds() {
        //1er hilo, busca posicion usuario
        Log.i(TAG, "iniciarThreds");
        Thread thread1 = new Thread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "buscarPosicion");
                buscarPosicion();
            }
        });
//
        //2o hilo, busca peticiones

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "buscarPeticiones");
                buscarPeticiones(user.getIdUser());
            }
        });
//
        thread1.start();
        thread2.start();
    }

    @Override
    public void tratarAceptar(Object informacion) {
        if (atendiendoPeticion) {
            timer.removeCallbacksAndMessages(null);
            aceptarPeticion(informacion);
            atendiendoPeticion = false;
        }
    }

    @Override
    public void tratarRechazar(Object informacion) {
        if (atendiendoPeticion) {
            timer.removeCallbacksAndMessages(null);
            rechazarPeticion(informacion);
            atendiendoPeticion = false;
        }
    }

    @Override
    public void tratarParar(Object informacion) {
        appMediador.unRegisterReceiver(receptorGPS);
        appMediador.unRegisterReceiver(receptorLocalizacion);
        modelo.pararRuta(informacion);
    }

    private void guardarPosicion(Object[] nuevaLocalizacion) {
        //Log.i(TAG, "guardarPosicion");
        appMediador.registerReceiver(receptorLocalizacion, AppMediador.AVISO_LOCALIZACION_GUARDADA);
        modelo.actualizarLocalizacion(nuevaLocalizacion);
    }

    private void buscarPeticiones(Object informacion) {
        appMediador.registerReceiver(receptorPeticiones, AppMediador.AVISO_PETICION_OTGCONDUCTOR);
        Object[] datos = new Object[]{2,informacion};
        modelo.obtenerPeticionesDePasajeros(datos);
    }

    private void buscarPosicion() {
        appMediador.registerReceiver(receptorGPS, AppMediador.AVISO_LOCALIZACION_GPS);
        modelo.iniciarGps();
    }

    private void aceptarPeticion(Object informacion) {
        appMediador.registerReceiver(receptorPeticiones, AppMediador.AVISO_CONCRETAR_VINCULO);
        Vinculo v = (Vinculo)informacion;
        Object[] datos = new Object[]{0,v.getIdPasajero(),v.getIdConductor()};
        modelo.aceptarPasajero(datos);
    }

    private void rechazarPeticion(Object informacion) {
        appMediador.registerReceiver(receptorPeticiones, AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR);
        Vinculo v = (Vinculo)informacion;
        Object[] datos = new Object[]{1,v.getIdPasajero(),v.getIdConductor()};
        modelo.rechazarPasajero(datos);
    }

    private Runnable limiteDeTiempo = new Runnable() {
        @Override
        public void run() {
            atendiendoPeticion = false;
            buscarPeticiones(user.getIdUser());
        }
    };
}
