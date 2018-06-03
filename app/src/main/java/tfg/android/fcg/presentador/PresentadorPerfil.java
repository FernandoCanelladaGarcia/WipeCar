package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.vista.VistaPerfil;
import tfg.android.fcg.vista.VistaPrincipal;
import tfg.android.fcg.vista.VistaVehiculo;

public class PresentadorPerfil implements IPresentadorPerfil {

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaPerfil vistaPerfil;
    private Usuario usuario;
    private Vehiculo vehiculo;
    private final static String TAG = "depurador";

    public PresentadorPerfil() {
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaPerfil = (VistaPerfil) appMediador.getVistaPerfil();
    }

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] info = new Object[2];
            if (intent.getAction().equals(AppMediador.AVISO_OBTENER_USUARIO)) {
                appMediador.unRegisterReceiver(this);
                usuario = (Usuario) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                if (usuario != null) {
                    if (usuario.getDatoVehiculo() != null) {
                        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_VEHICULO);
                        modelo.obtenerVehiculoUsuario(usuario.getDatoVehiculo());
                    } else {
                        Log.i(TAG, "Usuario sin datos de vehiculo");
                        info[0] = usuario;
                        info[1] = "";
                        vistaPerfil.prepararVista(info);
                    }
                }
            }if(intent.getAction().equals(AppMediador.AVISO_OBTENER_VEHICULO)){
                appMediador.unRegisterReceiver(this);
                info[0] = usuario;
                Log.i(TAG, usuario.getNombre());
                vehiculo = (Vehiculo)intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_VEHICULO);
                info[1] = vehiculo;
                Log.i(TAG, vehiculo.getMarca());
                vistaPerfil.prepararVista(info);
            }else {
                Log.i(TAG, "Error no se obtuvo usuario");
            }
        }
    };

    @Override
    public void iniciar(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_OBTENER_USUARIO);
        modelo.obtenerUsuario(informacion);
    }

    @Override
    public void tratarGuardar(Object informacion) {

    }

    @Override
    public void tratarOk(Object informacion) {
        int tarea = (int)informacion;
        switch (tarea){
            case 0:
                appMediador.launchActivity(VistaVehiculo.class,this,null);
                break;
        }
    }

    @Override
    public void tratarGuardarPerfil(Object informacion) {

    }

    @Override
    public void tratarCancelar(Object informacion) {

    }

    @Override
    public void tratarEditar(Object informacion) {

    }

    @Override
    public void tratarHistorial(Object informacion) {

    }

    @Override
    public void tratarAyuda(Object informacion) {

    }

    @Override
    public void tratarPapelera(Object informacion) {

    }


    @Override
    public void tratarEliminarPerfil(Object informacion) {

    }


    @Override
    public void tratarConductor(Object informacion) {

    }
}
