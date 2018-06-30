package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.vista.VistaHistorial;
import tfg.android.fcg.vista.VistaLogin;
import tfg.android.fcg.vista.VistaPerfil;
import tfg.android.fcg.vista.VistaVehiculo;

public class PresentadorPerfil implements IPresentadorPerfil {

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaPerfil vistaPerfil;
    private Usuario usuario;
    private Vehiculo vehiculo;
    private Object[] perfil;
    private Object[] datoVehiculo;
    private SharedPreferences sharedPreferences;
    private final static String TAG = "depurador";

    public PresentadorPerfil() {
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaPerfil = (VistaPerfil) appMediador.getVistaPerfil();
        perfil = new Object[13];
        datoVehiculo = new Object[13];
        sharedPreferences = appMediador.getSharedPreferences("Login", 0);
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
                        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_OBTENER_VEHICULO);
                        modelo.obtenerVehiculoUsuario(usuario.getDatoVehiculo());
                    } else {
                        Log.i(TAG, "Usuario sin datos de vehiculo");
                        info[0] = usuario;
                        info[1] = "";
                        vistaPerfil.prepararVista(info);
                    }
                }
            }
            if (intent.getAction().equals(AppMediador.AVISO_OBTENER_VEHICULO)) {
                appMediador.unRegisterReceiver(this);
                info[0] = usuario;
                Log.i(TAG, usuario.getNombre());
                vehiculo = (Vehiculo) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_VEHICULO);
                info[1] = vehiculo;
                Log.i(TAG, vehiculo.getMarca());
                vistaPerfil.prepararVista(info);

            }
            if (intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)) {
                appMediador.unRegisterReceiver(this);
                Log.i(TAG, "Usuario actualizado con exito");
                appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_ACTUALIZACION_VEHICULO);
                modelo.guardarVehiculo(datoVehiculo);
            }
            if (intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_VEHICULO)) {
                appMediador.unRegisterReceiver(this);
                Log.i(TAG,"Datos vehiculo actualizado con exito");
                vistaPerfil.cerrarProgreso();
            }
            if (intent.getAction().equals(AppMediador.AVISO_ELIMINAR_USUARIO)){
                appMediador.unRegisterReceiver(this);
                Log.i(TAG, "Usuario eliminado con exito");
                vistaPerfil.cerrarProgreso();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Log.i(TAG, "Limpiado de Shared Preferences");
                appMediador.launchActivity(VistaLogin.class,this,null);
                vistaPerfil.finish();
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
        vistaPerfil.mostrarDialogo(informacion);
    }

    @Override
    public void tratarOk(Object informacion) {
        int tarea = (int) informacion;
        switch (tarea) {
            case 0:
                appMediador.launchActivity(VistaVehiculo.class, this, null);
                break;
            case 1:
                vistaPerfil.mostrarProgreso();
                vistaPerfil.prepararEdicion();
                break;
            case 2:
                vistaPerfil.mostrarProgreso();
                vistaPerfil.salirEdicion();
                break;
        }
    }

    @Override
    public void tratarGuardarPerfil(Object informacion) {
        vistaPerfil.mostrarProgreso();
        Object[] datos = (Object[]) informacion;
        //Tarea guardar Perfil
        perfil[0] = 2;
        perfil[1] = datos[0];
        perfil[2] = datos[1];
        perfil[3] = datos[2];
        perfil[4] = datos[3];
        perfil[7] = datos[7];
        datoVehiculo[0] = datos[8];
        datoVehiculo[1] = datos[4];
        datoVehiculo[2] = datos[5];
        datoVehiculo[3] = datos[6];
        datoVehiculo[4] = 1;
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_ACTUALIZACION_USUARIO);
        modelo.guardarPerfil(perfil);
    }

    @Override
    public void tratarCancelar(Object informacion) {

    }

    @Override
    public void tratarEditar(Object informacion) {
        vistaPerfil.mostrarDialogo(informacion);

    }

    //TODO: SIN DATOS, REDACCION
    @Override
    public void tratarHistorial() {
        appMediador.launchActivity(VistaHistorial.class, this, null);

    }

    @Override
    public void tratarAyuda(Object informacion) {

    }

    //TODO: NUEVO, REDACCION
    @Override
    public void tratarPapelera(Object informacion) {
        vistaPerfil.mostrarDialogo(informacion);
    }


    @Override
    public void tratarEliminarPerfil(Object informacion) {
        vistaPerfil.mostrarProgreso();
        appMediador.registerReceiver(receptorDeAvisos, AppMediador.AVISO_ELIMINAR_USUARIO);
        modelo.eliminarPerfil((Object[])informacion);
    }


    @Override
    public void tratarConductor(Object informacion) {

    }
}
