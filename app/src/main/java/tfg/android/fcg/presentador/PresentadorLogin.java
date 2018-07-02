package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaLogin;
import tfg.android.fcg.vista.VistaMapaOrigen;
import tfg.android.fcg.vista.VistaRegistro;

public class PresentadorLogin implements IPresentadorLogin {

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaLogin vistaLogin;

    public PresentadorLogin(){
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaLogin = (VistaLogin) appMediador.getVistaLogin();
    }

    private BroadcastReceiver receptor = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_USER_LOGIN)){
                appMediador.unRegisterReceiver(this);

                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_LOGIN,false);
                if(resultado){
                    vistaLogin.cerrarProgreso();
                    appMediador.launchActivity(VistaMapaOrigen.class,this,null);
                    vistaLogin.finish();
                }else{
                    vistaLogin.cerrarProgreso();
                    vistaLogin.mostrarDialogo(0);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_CORREO_PASSWORD)){
                appMediador.unRegisterReceiver(this);

                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_RECUPERAR_PASSWORD,false);
                if(resultado){
                    vistaLogin.cerrarProgreso();
                    vistaLogin.mostrarDialogo(2);
                }
            }
        }
    };
    @Override
    public void tratarLogin(Object informacion) {
        vistaLogin.mostrarProgreso();
        appMediador.registerReceiver(receptor,AppMediador.AVISO_USER_LOGIN);
        modelo.comprobarLogin((Object[])informacion);
    }

    @Override
    public void tratarOk(Object informacion) {

    }

    @Override
    public void tratarRecuperarPassword(Object informacion) {
        vistaLogin.mostrarProgreso();
        appMediador.registerReceiver(receptor,AppMediador.AVISO_CORREO_PASSWORD);
        modelo.recuperarPassword(informacion);
    }

    //TODO: NO SE HA UTILIZADO, REDACCION
    @Override
    public void tratarCambiarPassword(Object informacion) {

    }

    @Override
    public void tratarNuevo() {
        appMediador.launchActivity(VistaRegistro.class, this,null);
    }
}
