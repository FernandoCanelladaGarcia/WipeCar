package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.vista.VistaLogin;
import tfg.android.fcg.vista.VistaPerfil;
import tfg.android.fcg.vista.VistaPrincipal;

public class PresentadorPrincipal implements IPresentadorPrincipal{

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaPrincipal vistaPrincipal;
    private Usuario usuario;
    private ArrayList<Usuario> conductores;
    private ArrayList<Vehiculo> vehiculos;
    private final static String TAG = "depurador";

    public PresentadorPrincipal(){
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaPrincipal = (VistaPrincipal) appMediador.getVistaPrincipal();
        vehiculos = new ArrayList<>();
    }

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_DESLOGIN)){
                appMediador.unRegisterReceiver(this);
                vistaPrincipal.cerrarProgreso();
                appMediador.launchActivity(VistaLogin.class, this, null);
            }
            if(intent.getAction().equals(AppMediador.AVISO_OBTENER_USUARIO)){
                appMediador.unRegisterReceiver(this);
                usuario = (Usuario) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                vistaPrincipal.setUsuario(usuario);
                Log.i(TAG,usuario.getNombre());
            }
            if(intent.getAction().equals(AppMediador.AVISO_LISTA_CONDUCTORES)){
                appMediador.unRegisterReceiver(this);
                Object[] respuesta = new Object[3];
                conductores = (ArrayList<Usuario>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_CONDUCTORES);
                if(conductores == null){
                    //Error
                    vistaPrincipal.cerrarProgreso();
                    vistaPrincipal.mostrarDialogo(3);
                }else{
                    if(conductores.isEmpty() || conductores == null){
                        //Elemento vacio
                        Log.i(TAG,"No Conductores");
                        vistaPrincipal.cerrarProgreso();
                        vistaPrincipal.setConductores(conductores);
                    }else{
                        vistaPrincipal.setConductores(conductores);
                    }
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO)){
                appMediador.unRegisterReceiver(this);
                Object[] respuesta = new Object[3];
                ArrayList<Usuario> pasajeros = (ArrayList<Usuario>)  intent.getSerializableExtra(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO);
                if(pasajeros == null){
                    //ERROR
                    vistaPrincipal.cerrarProgreso();
                    vistaPrincipal.mostrarDialogo(0);
                }else{
                    if(pasajeros.isEmpty() || pasajeros == null){
                        Log.i(TAG,"No Pasajeros");
                        vistaPrincipal.cerrarProgreso();
                        pasajeros = new ArrayList<>();
                        vistaPrincipal.setPasajeros(pasajeros);
                    }else{
                        //Mostrar Lista
                        vistaPrincipal.cerrarProgreso();
                        vistaPrincipal.setPasajeros(pasajeros);
                    }
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_OBTENER_VEHICULO)){
                Vehiculo vehiculo = (Vehiculo)intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_VEHICULO);
                vehiculos.add(vehiculo);
                if(vehiculos.size() == conductores.size()){
                    appMediador.unRegisterReceiver(this);
                    Log.i(TAG,"vehiculos total = " + vehiculos.size());
                    vistaPrincipal.setVehiculos(vehiculos);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)){
                Object[] datos = (Object[])intent.getSerializableExtra(AppMediador.CLAVE_ACTUALIZACION_USUARIO);
                if((boolean)datos[0]){
                    if(datos[1].toString().equals("origenydestino")){
                        Log.i(TAG,"Actualizacion Destino");
                        vistaPrincipal.cerrarProgreso();
                        vistaPrincipal.recreate();
                    }else if(datos[1].toString().equals("fechayhora")) {
                        Log.i(TAG,"Set fecha y hora");
                        vistaPrincipal.cerrarProgreso();
                    }
                }else{
                    vistaPrincipal.cerrarProgreso();
                }
            }
        }
    };
    @Override
    public void iniciar(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_USUARIO);
        vistaPrincipal.mostrarProgreso();
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
        Object[] datos = (Object[])informacion;
        Object[] respuesta = new Object[13];
        int tarea = (int)datos[0];
        switch (tarea){
            case 0:
                appMediador.launchActivity(VistaPerfil.class, this, null);
                break;
            case 1:
                vistaPrincipal.mostrarProgreso();
                appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_DESLOGIN);
                modelo.deslogearUsuario();
                break;
            case 2:
                vistaPrincipal.mostrarProgreso();
                vistaPrincipal.cerrarDialogo();
                appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_ACTUALIZACION_USUARIO);

                respuesta[0] = 1;
                respuesta[5] = "1";
                respuesta[6] = datos[1];
                modelo.guardarPerfil(respuesta);
                break;
            case 3:
                vistaPrincipal.mostrarProgreso();
                vistaPrincipal.cerrarDialogo();
                String[] fechaHora = (String[])datos[1];
                appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_ACTUALIZACION_USUARIO);
                respuesta[0] = 4;
                respuesta[9] = fechaHora[0];
                respuesta[10] = fechaHora[1];
                modelo.guardarPerfil(respuesta);
        }

    }

    @Override
    public void tratarOnTheGo(Object informacion) {

    }

    @Override
    public void obtenerVehiculos(Object informacion) {
        ArrayList<Usuario> conduc = (ArrayList<Usuario>) informacion;
        Log.i(TAG,"Existen Conductores " + conduc.size());
        for (Usuario conductor: conduc) {
            appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_OBTENER_VEHICULO);
            modelo.obtenerVehiculoUsuario(conductor.getDatoVehiculo());
        }
    }

    @Override
    public void obtenerConductores(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_LISTA_CONDUCTORES);
        Object[] datos = new Object[]{1, (String) informacion};
        Log.i(TAG,"Usuario Pasajero "+ usuario.getDestino());
        modelo.obtenerUsuariosPickUp(datos);
    }

    @Override
    public void obtenerPeticionesPasajeros(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_LISTA_PASAJEROS_VINCULO);
        modelo.obtenerPeticionesDePasajeros((String) informacion);
    }
}
