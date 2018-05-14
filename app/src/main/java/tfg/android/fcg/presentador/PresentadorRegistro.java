package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaMapaOrigen;
import tfg.android.fcg.vista.VistaRegistro;

public class PresentadorRegistro implements IPresentadorRegistro {

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaRegistro vistaRegistro;

    public PresentadorRegistro(){
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaRegistro = (VistaRegistro) appMediador.getVistaRegistro();
    }

    private BroadcastReceiver receptor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_REGISTRO_USUARIO)){
                appMediador.unRegisterReceiver(this);
                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO,false);
                if(resultado){
                    vistaRegistro.cerrarProgreso();
                    vistaRegistro.mostrarDialogo(2);
                    appMediador.launchActivity(VistaMapaOrigen.class,this,null);
                }else{
                    vistaRegistro.cerrarProgreso();
                    vistaRegistro.mostrarDialogo(0);
                }
            }
        }
    };

    @Override
    public void tratarRegistro(Object informacion) {
        vistaRegistro.mostrarDialogo(informacion);
    }

    @Override
    public void tratarOk(Object informacion) {
        vistaRegistro.cerrarDialogo();
        vistaRegistro.mostrarProgreso();
        tratarAceptarRegistro(informacion);
    }

    @Override
    public void tratarAceptarRegistro(Object informacion) {
        appMediador.registerReceiver(receptor,AppMediador.AVISO_REGISTRO_USUARIO);
        modelo.registrarUsuario((Object[])informacion);
    }

    @Override
    public void tratarCancelar() {
        vistaRegistro.cerrarDialogo();
    }
}
