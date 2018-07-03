package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.vista.VistaLogin;
import tfg.android.fcg.vista.VistaPerfil;
import tfg.android.fcg.vista.VistaPrincipal;

public class PresentadorPrincipal implements IPresentadorPrincipal{

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaPrincipal vistaPrincipal;
    private Usuario usuario;
    public PresentadorPrincipal(){
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaPrincipal = (VistaPrincipal) appMediador.getVistaPrincipal();
    }

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_DESLOGIN)){
                appMediador.unRegisterReceiver(this);
                vistaPrincipal.cerrarProgreso();
                appMediador.launchActivity(VistaLogin.class, this, null);
                vistaPrincipal.finish();
            }
            if(intent.getAction().equals(AppMediador.AVISO_OBTENER_USUARIO)){
                appMediador.unRegisterReceiver(this);
                usuario = (Usuario) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                if(usuario.isRol()){
                    //MODO - CONDUCTOR
                    appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_LISTA_PASAJEROS_VINCULO);
                    modelo.obtenerPeticionesDePasajeros(usuario.getIdUser());
                }else{
                    //MODO - PASAJERO
                    appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_LISTA_CONDUCTORES);
                    Object[] datos = new Object[]{1,usuario.getDestino()};
                    modelo.obtenerUsuariosPickUp(datos);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_LISTA_CONDUCTORES)){
                appMediador.unRegisterReceiver(this);
                Object[] respuesta = new Object[2];
                List<Usuario> conductores = (List<Usuario>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_CONDUCTORES);
                if(conductores != null){
                    if(!conductores.isEmpty()){
                        //Mostrar Lista
                        respuesta[0] = 1;
                        respuesta[1] = conductores;
                        vistaPrincipal.mostrarUsuarios(respuesta);
                    }else{
                        //Elemento vacio
                        respuesta[0] = 0;
                        respuesta[1] = conductores;
                        vistaPrincipal.mostrarUsuarios(respuesta);
                    }
                }else{
                    //Error
                    vistaPrincipal.cerrarProgreso();
                    vistaPrincipal.mostrarDialogo(0);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO)){
                appMediador.unRegisterReceiver(this);
                Object[] respuesta = new Object[2];
                List<Usuario> pasajeros = (List<Usuario>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO);
                if(pasajeros != null){
                    if(!pasajeros.isEmpty()){
                        //Mostrar Lista
                        respuesta[0] = 1;
                        respuesta[1] = pasajeros;
                        vistaPrincipal.mostrarUsuarios(respuesta);
                    }else{
                        //Mostrar elemento vacio
                        respuesta[0] = 0;
                        respuesta[1] = pasajeros;
                        vistaPrincipal.mostrarUsuarios(respuesta);
                    }
                }else{
                    //ERROR
                    vistaPrincipal.cerrarProgreso();
                    vistaPrincipal.mostrarDialogo(0);
                }
            }
        }
    };
    @Override
    public void iniciar(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_USUARIO);
        modelo.obtenerUsuario(informacion);
    }

    @Override
    public void tratarSeleccion(Object informacion) {

    }


    @Override
    public void tratarOk(Object informacion) {

    }

    @Override
    public void tratarCancelar(Object informacion) {

    }

    @Override
    public void tratarChat(Object informacion) {

    }

    @Override
    public void tratarMapa(Object informacion) {

    }

    @Override
    public void tratarBorrarSeleccion(Object informacion) {

    }

    @Override
    public void tratarConfiguracion(Object informacion) {
        int tarea = (int)informacion;
        switch (tarea){
            case 0:
                appMediador.launchActivity(VistaPerfil.class, this, null);
                break;
            case 1:
                vistaPrincipal.mostrarProgreso();
                appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_DESLOGIN);
                modelo.deslogearUsuario();
                break;
        }

    }

    @Override
    public void tratarOnTheGo(Object informacion) {

    }
}
