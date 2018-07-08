package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

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

public class PresentadorPrincipal implements IPresentadorPrincipal {

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaPrincipal vistaPrincipal;
    private Usuario usuario;
    private ArrayList<Usuario> conductores;
    private ArrayList<Usuario> pasajeros;
    private ArrayList<Vehiculo> vehiculos;
    private final static String TAG = "depurador";

    public PresentadorPrincipal() {
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaPrincipal = (VistaPrincipal) appMediador.getVistaPrincipal();
        vehiculos = new ArrayList<>();
        conductores = new ArrayList<>();
        pasajeros = new ArrayList<>();
    }

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppMediador.AVISO_DESLOGIN)) {
                appMediador.unRegisterReceiver(this);
                vistaPrincipal.cerrarProgreso();
                appMediador.launchActivity(VistaLogin.class, this, null);
            }
            if (intent.getAction().equals(AppMediador.AVISO_OBTENER_USUARIO)) {
                appMediador.unRegisterReceiver(this);
                Usuario user = (Usuario) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                if (user == null) {
                    vistaPrincipal.cerrarProgreso();
                } else {
                    usuario = user;
                    vistaPrincipal.setUsuario(usuario);
                }
                Log.i(TAG, usuario.getNombre());
            }
            if (intent.getAction().equals(AppMediador.AVISO_LISTA_CONDUCTORES)) {
                appMediador.unRegisterReceiver(this);
                ArrayList<Usuario> conduct = (ArrayList<Usuario>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_CONDUCTORES);
                if (conduct.isEmpty() || conduct == null) {
                    //Elemento vacio
                    Log.i(TAG, "No Conductores");
//                    vistaPrincipal.cerrarProgreso();
                    conductores = null;
                    conductores = conduct;
                    vistaPrincipal.setConductores(conductores);
                } else {
                    conductores = null;
                    conductores = conduct;
                    vistaPrincipal.setConductores(conductores);
                }

            }
            if (intent.getAction().equals(AppMediador.AVISO_LISTA_PASAJEROS_VINCULO)) {
                appMediador.unRegisterReceiver(this);
                ArrayList<Usuario> pasajer = (ArrayList<Usuario>) intent.getSerializableExtra(AppMediador.CLAVE_LISTA_PASAJEROS_VINCULO);
                if (pasajer.isEmpty() || pasajer == null) {
                    Log.i(TAG, "No Pasajeros");
//                    vistaPrincipal.cerrarProgreso();
                    pasajeros = null;
                    pasajeros = pasajer;
                    vistaPrincipal.setPasajeros(pasajeros);
                } else {
                    //Mostrar Lista
                    pasajeros = null;
                    pasajeros = pasajer;
//                    vistaPrincipal.cerrarProgreso();
                    vistaPrincipal.setPasajeros(pasajeros);
                }

            }
            if (intent.getAction().equals(AppMediador.AVISO_OBTENER_VEHICULO)) {
                Vehiculo vehiculo = (Vehiculo) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_VEHICULO);
                vehiculos.add(vehiculo);
                if (vehiculos.size() == conductores.size()) {
                    appMediador.unRegisterReceiver(this);
                    Log.i(TAG, "vehiculos total = " + vehiculos.size());
                    vistaPrincipal.setVehiculos(vehiculos);
                    vehiculos = new ArrayList<>();
                }
            }
            if (intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)) {
                appMediador.unRegisterReceiver(this);
                Object[] datos = (Object[]) intent.getSerializableExtra(AppMediador.CLAVE_ACTUALIZACION_USUARIO);
                if ((boolean) datos[0]) {
                    if (datos[1].toString().equals("origenydestino")) {
                        Log.i(TAG, "Actualizacion Destino");
                        vistaPrincipal.cerrarProgreso();
                        iniciar(usuario.getIdUser());
                    } else if (datos[1].toString().equals("fechayhora")) {
                        Log.i(TAG, "Set fecha y hora");
                        vistaPrincipal.cerrarProgreso();
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
                }else{

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

    @Override
    public void obtenerVehiculos(Object informacion) {
        ArrayList<Usuario> conduc = (ArrayList<Usuario>) informacion;
        Log.i(TAG, "Existen Conductores " + conduc.size());
        for (Usuario conductor : conduc) {
            appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_OBTENER_VEHICULO);
            modelo.obtenerVehiculoUsuario(conductor.getDatoVehiculo());
        }
    }

    @Override
    public void obtenerConductores(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_LISTA_CONDUCTORES);
        Object[] datos = new Object[]{1, (String) informacion};
        Log.i(TAG, "Usuario Pasajero " + usuario.getDestino());
        modelo.obtenerUsuariosPickUp(datos);
    }

    @Override
    public void obtenerPeticionesPasajeros(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_LISTA_PASAJEROS_VINCULO);
        modelo.obtenerPeticionesDePasajeros((String) informacion);
    }
}
