package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.vista.VistaLogin;
import tfg.android.fcg.vista.VistaPerfil;
import tfg.android.fcg.vista.VistaPrincipal;

public class PresentadorPrincipal implements IPresentadorPrincipal {

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaPrincipal vistaPrincipal;
    private Usuario usuario;
    private ArrayList<Usuario> conductores;
    private ArrayList<Usuario> usuariosVinculo;
    private ArrayList<Vehiculo> vehiculos;
    private ArrayList<Vehiculo> vehiculosVinculo;
    private ArrayList<Vinculo> vinculos;
    private ArrayList<Vinculo> vinculosConductor;
    private final static String TAG = "depurador";
    private boolean vinculosPasajero = false;
    private boolean vinculoConductor = false;

    public PresentadorPrincipal() {
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaPrincipal = (VistaPrincipal) appMediador.getVistaPrincipal();
        vehiculos = new ArrayList<>();
        conductores = new ArrayList<>();
        usuariosVinculo = new ArrayList<>();
        vinculos = new ArrayList<>();
        vehiculosVinculo = new ArrayList<>();
        vinculosConductor = new ArrayList<>();
    }

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(AppMediador.AVISO_OBTENER_USUARIO)) {
                if(!vinculosPasajero && !vinculoConductor) {
                    appMediador.unRegisterReceiver(this);
                    Usuario user = (Usuario) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                    if (user == null) {
                        vistaPrincipal.cerrarProgreso();
                    } else {
                        usuario = user;
                        vistaPrincipal.setUsuario(usuario);
                    }

                }else if(vinculosPasajero){
                    Usuario usuarioVinculo = (Usuario)intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                    usuariosVinculo.add(usuarioVinculo);
                    if(vinculos.size() == usuariosVinculo.size()){
                        appMediador.unRegisterReceiver(this);
                        Log.i(TAG, "vinculos total = " + usuariosVinculo.size());
                        vistaPrincipal.setVinculos(usuariosVinculo);
                    }

                }else if(vinculoConductor){
                    Usuario usuarioVinculo = (Usuario)intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                    usuariosVinculo.add(usuarioVinculo);
                    if(vinculosConductor.size() == usuariosVinculo.size()){
                        appMediador.unRegisterReceiver(this);
                        Log.i(TAG, "vinculos conductor total = " + usuariosVinculo.size());
                        vistaPrincipal.setPasajeros(usuariosVinculo);
                    }
                }
            }

            if (intent.getAction().equals(AppMediador.AVISO_LISTA_CONDUCTORES)) {
                appMediador.unRegisterReceiver(this);
                ArrayList<Usuario> conduct = (ArrayList<Usuario>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_CONDUCTORES);
                if (conduct.isEmpty() || conduct == null) {
                    //Elemento vacio
                    Log.i(TAG, "No Conductores al destino");
                    conductores = null;
                    conductores = conduct;
                    vistaPrincipal.setConductores(conductores);
                } else {
                    Log.i(TAG, "Hay Conductores al destino");
                    conductores = null;
                    conductores = conduct;
                    vistaPrincipal.setConductores(conductores);
                }
            }

            if (intent.getAction().equals(AppMediador.AVISO_OBTENER_VEHICULO)) {
                if (vinculosPasajero) {
                    Vehiculo vehiculoVinculo = (Vehiculo) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_VEHICULO);
                    vehiculosVinculo.add(vehiculoVinculo);
                    if(vehiculosVinculo.size() == usuariosVinculo.size()){
                        appMediador.unRegisterReceiver(this);
                        Log.i(TAG, "vehiculos de vinculo total = " + vehiculos.size());
                        vistaPrincipal.setVehiculosVinculo(vehiculosVinculo);
                        vehiculosVinculo = new ArrayList<>();
                    }
                }else{
                    Vehiculo vehiculo = (Vehiculo) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_VEHICULO);
                    vehiculos.add(vehiculo);
                    if (vehiculos.size() == conductores.size()) {
                        appMediador.unRegisterReceiver(this);
                        Log.i(TAG, "vehiculos total = " + vehiculos.size());
                        vistaPrincipal.setVehiculos(vehiculos);
                        vehiculos = new ArrayList<>();
                    }
                }
            }

            if(intent.getAction().equals(AppMediador.AVISO_LISTA_CONDUCTORES_VINCULO)){
                appMediador.unRegisterReceiver(this);
                ArrayList<Vinculo> vinculosConductores = (ArrayList<Vinculo>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_CONDUCTORES_VINCULO);
                //Vinculos de pasajero
                if (vinculosConductores.isEmpty() || vinculosConductores == null) {
                    Log.i(TAG, "No vinculos pasajero");
                    usuariosVinculo = new ArrayList<>();
                    vinculos = new ArrayList<>();
                    vistaPrincipal.setListaVinculos(vinculos);
                    vistaPrincipal.setVinculos(usuariosVinculo);
                }else{
                    Log.i(TAG, "Existen vinculos pasajero " + vinculosConductores.size());
                    vinculosPasajero = true;
                    vinculos = null;
                    vinculos = vinculosConductores;
                    vistaPrincipal.setListaVinculos(vinculos);
                    obtenerConductoresVinculo(vinculos);
                }
            }

            if (intent.getAction().equals(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO)) {
                appMediador.unRegisterReceiver(this);
                ArrayList<Vinculo> vinculosList = (ArrayList<Vinculo>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO);
                //Conductor
                if (vinculosList.isEmpty() || vinculosList == null) {
                    Log.i(TAG, "No Pasajeros con vinculo con el usuario actual");
                    usuariosVinculo = new ArrayList<>();
                    vinculos = new ArrayList<>();
                    vistaPrincipal.setListaVinculos(vinculos);
                    vistaPrincipal.setPasajeros(usuariosVinculo);
                } else {
                    //Mostrar Lista
                    Log.i(TAG, "Hay Pasajeros con vinculo para el usuario actual = " + vinculos.size());
                    vinculoConductor = true;
                    vinculosConductor = null;
                    vinculos = null;
                    vinculosConductor = vinculos;
                    vinculos = vinculosList;
                    vistaPrincipal.setListaVinculos(vinculos);
                    obtenerPasajerosVinculo(vinculosConductor);
                }
            }

            if (intent.getAction().equals(AppMediador.AVISO_DESLOGIN)) {
                appMediador.unRegisterReceiver(this);
                vistaPrincipal.cerrarProgreso();
                appMediador.launchActivity(VistaLogin.class, this, null);
            }

            if (intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)) {
                appMediador.unRegisterReceiver(this);
                Object[] datos = (Object[]) intent.getSerializableExtra(AppMediador.CLAVE_ACTUALIZACION_USUARIO);
                if ((boolean) datos[0]) {
                    if (datos[1].toString().equals("origenydestino")) {
                        Log.i(TAG, "Actualizacion Destino");
                        vinculosPasajero = false;
                        vistaPrincipal.cerrarProgreso();
                        refrescarListas();
                        vistaPrincipal.refrescarContenido();
                    } else if (datos[1].toString().equals("fechayhora")) {
                        Log.i(TAG, "Set fecha y hora");
                        vinculoConductor = false;
                        vistaPrincipal.cerrarProgreso();
                        refrescarListas();
                        vistaPrincipal.refrescarContenido();
                    }
                } else {
                    vistaPrincipal.cerrarProgreso();
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_CREACION_VINCULO)){
                appMediador.unRegisterReceiver(this);
                boolean respuesta = intent.getBooleanExtra(AppMediador.CLAVE_CREACION_VINCULO,false);
                if(respuesta){
                    vistaPrincipal.cerrarProgreso();
                    refrescarListas();
                    vistaPrincipal.refrescarContenido();
                }else{
                    vistaPrincipal.cerrarProgreso();
                }
            }
        }
    };

    @Override
    public void iniciar(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_OBTENER_USUARIO);
        vistaPrincipal.mostrarProgreso();
        modelo.obtenerUsuario(informacion);
    }

    @Override
    public void obtenerPeticionesPasajeros(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_LISTA_PASAJEROS_VINCULO);
        Object[] datos = new Object[]{0,(String)informacion};
        modelo.obtenerPeticionesDePasajeros(datos);
    }

    @Override
    public void obtenerConductores(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_LISTA_CONDUCTORES);
        Object[] datos = new Object[]{1, (String) informacion};
        Log.i(TAG, "Usuario Pasajero " + usuario.getDestino());
        modelo.obtenerUsuariosPickUp(datos);
    }

    @Override
    public void obtenerVehiculos(Object informacion) {
        ArrayList<Usuario> conduc = (ArrayList<Usuario>) informacion;
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_OBTENER_VEHICULO);
        for (Usuario conductor : conduc) {
            modelo.obtenerVehiculoUsuario(conductor.getDatoVehiculo());
        }
    }

    @Override
    public void obtenerVinculosPasajero(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_LISTA_CONDUCTORES_VINCULO);
        Object[] datos = new Object[]{1,(String)informacion};
        modelo.obtenerPeticionesDePasajeros(datos);
    }

    public void obtenerConductoresVinculo(Object informacion){
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_USUARIO);
        ArrayList<Vinculo> vinculos = (ArrayList<Vinculo>) informacion;
        for(Vinculo vinculo : vinculos){
            modelo.obtenerUsuario(vinculo.getIdConductor());
        }
    }

    public void obtenerPasajerosVinculo(Object informacion){
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_USUARIO);
        ArrayList<Vinculo> vinculos = (ArrayList<Vinculo>) informacion;
        for(Vinculo vinculo : vinculos){
            modelo.obtenerUsuario(vinculo.getIdPasajero());
        }
    }

    @Override
    public void obtenerVehiculosVinculo(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_OBTENER_VEHICULO);
        ArrayList<Usuario> usuarios = (ArrayList<Usuario>) informacion;
        for (Usuario conductor : usuarios) {
            modelo.obtenerVehiculoUsuario(conductor.getDatoVehiculo());
        }
    }

    @Override
    public void tratarSeleccion(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_CREACION_VINCULO);
        vistaPrincipal.mostrarProgreso();
        modelo.guardarUsuarioPickup((Object[])informacion);
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
        Object[] datos = (Object[]) informacion;
        Object[] respuesta = new Object[13];
        int tarea = (int) datos[0];
        switch (tarea) {
            case 0:
                appMediador.launchActivity(VistaPerfil.class, this, null);
                vistaPrincipal.finish();
                break;
            case 1:
                vistaPrincipal.mostrarProgreso();
                appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_DESLOGIN);
                modelo.deslogearUsuario();
                break;
            case 2:
                vistaPrincipal.mostrarProgreso();
                vistaPrincipal.cerrarDialogo();
                appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_ACTUALIZACION_USUARIO);

                respuesta[0] = 1;
                respuesta[5] = "1";
                respuesta[6] = datos[1];
                modelo.guardarPerfil(respuesta);
                break;
            case 3:
                vistaPrincipal.mostrarProgreso();
                vistaPrincipal.cerrarDialogo();
                String[] fechaHora = (String[]) datos[1];
                appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_ACTUALIZACION_USUARIO);
                respuesta[0] = 4;
                respuesta[9] = fechaHora[0];
                respuesta[10] = fechaHora[1];
                modelo.guardarPerfil(respuesta);
        }

    }

    @Override
    public void tratarOnTheGo(Object informacion) {

    }

    private void refrescarListas(){
        vehiculos = new ArrayList<>();
        conductores = new ArrayList<>();
        usuariosVinculo = new ArrayList<>();
        vinculos = new ArrayList<>();
        vehiculosVinculo = new ArrayList<>();
        vinculosConductor = new ArrayList<>();
    }
}
