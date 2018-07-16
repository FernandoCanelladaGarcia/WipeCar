package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaPerfil;
import tfg.android.fcg.vista.VistaVehiculo;

public class PresentadorVehiculo implements IPresentadorVehiculo{

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaVehiculo vistaVehiculo;
    private String datoVehiculo;
    private SharedPreferences sharedPreferences;

    private final static String TAG = "depurador";

    public PresentadorVehiculo(){
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaVehiculo = (VistaVehiculo) appMediador.getVistaVehiculo();
        sharedPreferences = appMediador.getSharedPreferences("Login", 0);
    }

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_REGISTRO_VEHICULO)) {
                appMediador.unRegisterReceiver(this);
                datoVehiculo = intent.getStringExtra(AppMediador.CLAVE_RESULTADO_NUEVO_VEHICULO);
                if (datoVehiculo != null || !datoVehiculo.equals("")) {
                    Object[] dato = new Object[13];
                    dato[0] = 5;
                    dato[8] = datoVehiculo;
                    appMediador.registerReceiver(receptor, AppMediador.AVISO_ACTUALIZACION_USUARIO);
                    modelo.guardarPerfil(dato);
                }else{
                    Log.i(TAG,"ERROR AL REGISTRAR VEHICULO");
                }
            }
        }
    };

    private BroadcastReceiver receptor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)) {
                Object[] datos = (Object[]) intent.getSerializableExtra(AppMediador.CLAVE_ACTUALIZACION_USUARIO);
                if ((boolean) datos[0]) {
                    appMediador.unRegisterReceiver(this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("rol",true);
                    editor.apply();
                    vistaVehiculo.cerrarProgreso();
                    appMediador.launchActivity(VistaPerfil.class, this, null);
                    vistaVehiculo.finish();
                }else{
                    Log.i(TAG,"ERROR EN LA ACTUALIZACION DEL USUARIO");
                }
            }
        }
    };
    @Override
    public void tratarGuardar(Object informacion) {
        vistaVehiculo.mostrarDialogo(informacion);
    }

    @Override
    public void tratarGuardarVehiculo(Object informacion) {
        vistaVehiculo.mostrarProgreso();
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_REGISTRO_VEHICULO);
        modelo.guardarVehiculo((Object[]) informacion);
    }

    @Override
    public void tratarCancelar(Object informacion) {

    }

    @Override
    public void tratarEditar(Object informacion) {

    }

    @Override
    public void tratarPapelera(Object informacion) {

    }


    @Override
    public void tratarEliminarVehiculo(Object informacion) {

    }
}
