package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Posicion;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.modelo.bajonivel.ServicioLocalizacion;
import tfg.android.fcg.vista.VistaOTGPasajero;

public class PresentadorOTGPasajero implements IPresentadorOTGPasajero{

    private AppMediador appMediador;
    private VistaOTGPasajero vistaOTGPasajero;
    private Marker miUbicacion;
    private Marker marcaSeleccionada;
    private IModelo modelo;
    private SharedPreferences sharedPreferences;
    private ArrayList<Vinculo> conductoresEnRuta;
    private ArrayList<Usuario> conductores;
    private ArrayList<Posicion> posiciones;
    private final static String TAG = "depurador";

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_CONDUCTORES_OTG)){
                ArrayList<Vinculo> vehiculos = (ArrayList<Vinculo>) intent.getSerializableExtra(AppMediador.CLAVE_CONDUCTORES_OTG);
                if(vehiculos.isEmpty() || vehiculos == null){
                    //vistaOTGPasajero.cerrarProgreso();
                }else{
                    Log.i(TAG, "Existen conductores en ruta " + vehiculos.size());
                    conductoresEnRuta = new ArrayList<>();
                    conductoresEnRuta = vehiculos;
                    vistaOTGPasajero.setConductoresEnRuta(conductoresEnRuta);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_OBTENER_USUARIO)){
                Usuario conductor = (Usuario)intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_USUARIO);
                conductores.add(conductor);
                if(conductores.size() == conductoresEnRuta.size()){
                    appMediador.unRegisterReceiver(this);
                    Log.i(TAG, "Conductores en ruta " + conductores.size());
                    vistaOTGPasajero.setConductores(conductores);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_OBTENER_POSICION)){
                Posicion posConductor = (Posicion) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_POSICION);
                posiciones.add(posConductor);
                if(posiciones.size() == conductores.size()){
                    vistaOTGPasajero.setPosiciones(posiciones);
                    vistaOTGPasajero.mostrarVehiculos();
                    appMediador.unRegisterReceiver(this);
                    Log.i(TAG, "Posiciones " + posiciones.size());
                    vistaOTGPasajero.cerrarProgreso();
                }
            }
        }
    };

    private BroadcastReceiver receptorGps = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_LOCALIZACION_GPS)){
                appMediador.unRegisterReceiver(this);
                appMediador.stopService(ServicioLocalizacion.class);
                Object[] posicion = new Object[3];
                posicion[0] = intent.getDoubleExtra(AppMediador.CLAVE_LATITUD,0);
                posicion[1] = intent.getDoubleExtra(AppMediador.CLAVE_LONGITUD,0);
                posicion[2] = "Mi posicion";
                Log.i(TAG,posicion[0].toString() + " " + posicion[1].toString());
                vistaOTGPasajero.mostrarMapaConPosicion(posicion);
            }
        }
    };

    public PresentadorOTGPasajero(){
        appMediador = AppMediador.getInstance();
        vistaOTGPasajero = (VistaOTGPasajero) appMediador.getVistaOTGPasajero();
        sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        conductoresEnRuta = new ArrayList<>();
        conductores = new ArrayList<>();
        posiciones = new ArrayList<>();
        modelo = Modelo.getInstance();
    }

    @Override
    public void iniciar() {
        appMediador.registerReceiver(receptorGps,AppMediador.AVISO_LOCALIZACION_GPS);
        modelo.iniciarGps();
    }

    @Override
    public void tratarBuscar(Object informacion) {
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_CONDUCTORES_OTG);
        vistaOTGPasajero.mostrarProgreso();
        modelo.obtenerConductoresEnRuta(informacion);
    }

    @Override
    public void tratarVehiculo(Object informacion) {

    }

    @Override
    public void tratarOk(Object informacion) {

    }

    @Override
    public void tratarCancelar(Object informacion) {

    }

    @Override
    public void obtenerConductores(Object informacion){
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_USUARIO);
        ArrayList<Vinculo> vinculos = (ArrayList<Vinculo>) informacion;
        for(Vinculo vinculo : vinculos){
            modelo.obtenerUsuario(vinculo.getIdConductor());
        }
    }

    @Override
    public void obtenerPosicionConductores(Object informacion){
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_POSICION);
        ArrayList<Usuario> conduct = (ArrayList<Usuario>) informacion;
        for(Usuario conductor : conduct){
            modelo.obtenerPosicionUsuario(conductor.getIdUser());
        }

    }
}
