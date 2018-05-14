package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaMapaOrigen;

public class PresentadorRegistro implements IPresentadorRegistro {

    private IModelo modelo;
    private AppMediador appMediador;

    public PresentadorRegistro(){
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
    }

    private BroadcastReceiver receptor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_REGISTRO_USUARIO)){
                appMediador.unRegisterReceiver(this);
                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO,false);
                if(resultado){
                    appMediador.getVistaRegistro().cerrarProgreso();
                    appMediador.getVistaRegistro().mostrarDialogo(2);
                    appMediador.launchActivity(VistaMapaOrigen.class,this,null);
                }else{
                    appMediador.getVistaRegistro().cerrarProgreso();
                    appMediador.getVistaRegistro().mostrarDialogo(0);
                }
            }
        }
    };

    @Override
    public void tratarRegistro(Object informacion) {
        appMediador.getVistaRegistro().mostrarDialogo(informacion);
    }

    @Override
    public void tratarOk(Object informacion) {
        appMediador.getVistaRegistro().cerrarDialogo();
        appMediador.getVistaRegistro().mostrarProgreso();
        tratarAceptarRegistro(informacion);
    }

    @Override
    public void tratarAceptarRegistro(Object informacion) {
        appMediador.registerReceiver(receptor,AppMediador.AVISO_REGISTRO_USUARIO);
        modelo.registrarUsuario((Object[])informacion);
    }

    @Override
    public void tratarCancelar() {
        appMediador.getVistaRegistro().cerrarDialogo();
    }
}
