package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.Historial;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaHistorial;
import tfg.android.fcg.vista.VistaLogin;
import tfg.android.fcg.vista.VistaPerfil;

public class PresentadorHistorial implements IPresentadorHistorial{

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaHistorial vistaHistorial;
    private String idUserValorar;

    private final static String TAG = "depurador";

    public PresentadorHistorial(){
     appMediador = AppMediador.getInstance();
     modelo = Modelo.getInstance();
     vistaHistorial = (VistaHistorial) appMediador.getVistaHistorial();
    }

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_HISTORIAL)){
                appMediador.unRegisterReceiver(this);
                List<Historial> listaHistorial = (List<Historial>) intent.getSerializableExtra(AppMediador.CLAVE_HISTORIAL);
                Object[] respuesta = new Object[2];
                if(listaHistorial != null){
                    //Hay lista
                    if(listaHistorial.size() > 0){
                        //No Vacia
                        respuesta[0] = 1;
                        respuesta[1] = listaHistorial;
                        vistaHistorial.mostrarHistorial(respuesta);
                    }else{
                        //Vacia
                        respuesta[0] = 0;
                        respuesta[1] = listaHistorial;
                        vistaHistorial.mostrarHistorial(respuesta);
                    }
                }else{
                    //ERROR
                    vistaHistorial.cerrarProgreso();
                    vistaHistorial.mostrarDialogo(0);
                }

            }
            if(intent.getAction().equals(AppMediador.AVISO_DESLOGIN)){
                appMediador.unRegisterReceiver(this);
                vistaHistorial.cerrarProgreso();
                Log.i(TAG,"Salir");
                appMediador.launchActivity(VistaLogin.class,this,null);
            }
        }
    };

    @Override
    public void iniciar(Object informacion) {
        vistaHistorial.mostrarProgreso();
        String idUser = (String) informacion;
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_HISTORIAL);
        modelo.obtenerHistorial(idUser);
    }

    @Override
    public void tratarValorar(Object informacion) {
        idUserValorar = (String)informacion;
        vistaHistorial.mostrarDialogo(1);
    }

    @Override
    public void tratarValoracion(Object informacion) {

    }

    @Override
    public void tratarCarga(Object informacion) {

    }

    @Override
    public void tratarVolver() {
        Log.i(TAG,"Volver");
        vistaHistorial.finish();
    }
    //TODO: NUEVO METODO, REDACCION
    @Override
    public void tratarSalir(){
        vistaHistorial.mostrarProgreso();
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_DESLOGIN);
        modelo.deslogearUsuario();
    }
}
