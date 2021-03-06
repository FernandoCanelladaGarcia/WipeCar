package tfg.android.fcg.presentador;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.Marker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.modelo.Posicion;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.modelo.bajonivel.ServicioLocalizacion;
import tfg.android.fcg.vista.VistaOTGPasajero;

public class PresentadorOTGPasajero implements IPresentadorOTGPasajero{

    private AppMediador appMediador;
    private VistaOTGPasajero vistaOTGPasajero;
    private IModelo modelo;
    private SharedPreferences sharedPreferences;
    private ArrayList<Vinculo> conductoresEnRuta;
    private ArrayList<Usuario> conductores;
    private ArrayList<Posicion> posiciones;
    private Vehiculo vehiculo;
    private Usuario conductor;
    private Usuario user;
    private Vinculo vinculo;
    private final static String TAG = "depurador";
    private boolean aceptado;

    private BroadcastReceiver receptorDeAvisos = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_CONDUCTORES_OTG)){
                ArrayList<Vinculo> vehiculos = (ArrayList<Vinculo>) intent.getSerializableExtra(AppMediador.CLAVE_CONDUCTORES_OTG);
                if(vehiculos.isEmpty() || vehiculos == null){
                    appMediador.unRegisterReceiver(this);
                    vistaOTGPasajero.cerrarProgreso();
                    vistaOTGPasajero.mostrarDialogo(1);
                }else{
                    Log.i(TAG, "Existen conductores en ruta " + vehiculos.size());
                    appMediador.unRegisterReceiver(this);
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
                    vistaOTGPasajero.cerrarProgreso();
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_OBTENER_POSICION)){
                Posicion posConductor = (Posicion) intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_POSICION);
                posiciones.add(posConductor);
                if(posiciones.size() == conductores.size()){
                    appMediador.unRegisterReceiver(this);
                    vistaOTGPasajero.setPosiciones(posiciones);
                    Log.i(TAG, "Posiciones " + posiciones.size());
                    posiciones = new ArrayList<>();
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_OBTENER_VEHICULO)){
                Vehiculo vehiculoConductor = (Vehiculo)intent.getSerializableExtra(AppMediador.CLAVE_OBTENER_VEHICULO);
                if(vehiculoConductor == null){
                    Log.i(TAG,"ERROR no se encuentra vehiculo");
                }else{
                    appMediador.unRegisterReceiver(this);
                    vehiculo = vehiculoConductor;
                    vistaOTGPasajero.setVehiculoConductor(vehiculo);
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_CREACION_VINCULO)){
                boolean respuesta = intent.getBooleanExtra(AppMediador.CLAVE_CREACION_VINCULO,false);
                if(respuesta){
                    Log.i(TAG,"Se ha creado vinculo");
                    appMediador.unRegisterReceiver(this);
                    vistaOTGPasajero.mostrarVehiculoVinculo();
                    //esperarRespuesta();
                }else{
                    Log.i(TAG,"ERROR agregando vinculo");
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

    private BroadcastReceiver receptorDeRespuestas = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR)){
                Log.i(TAG,"Aceptada la peticion");
                aceptado = true;
                vistaOTGPasajero.mostrarDialogo(2);
            }
            if(intent.getAction().equals(AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR)){
                boolean respuesta = intent.getBooleanExtra(AppMediador.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR,false);
                if(respuesta) {
                    if (!aceptado) {
                        Log.i(TAG, "Rechazada la peticion");
                        appMediador.unRegisterReceiver(this);
                        appMediador.unRegisterReceiver(receptorDeAvisos);
                        vinculo = null;
                        //Refresh con todos los coches
                        vistaOTGPasajero.mostrarDialogo(4);
                        vistaOTGPasajero.refrescarPantalla();
                    } else {
                        Log.i(TAG, "Finalizada la ruta, reiniciar pantalla y set historial");
                        appMediador.unRegisterReceiver(this);
                        appMediador.unRegisterReceiver(receptorDeAvisos);
                        //refresh pantalla con mi ubicacion
                        vistaOTGPasajero.mostrarDialogo(3);
                        vistaOTGPasajero.refrescarPantalla();
                    }
                }
            }
            if(intent.getAction().equals(AppMediador.AVISO_CREACION_HISTORIAL)){
                boolean respuesta = intent.getBooleanExtra(AppMediador.CLAVE_CREACION_HISTORIAL,false);
                if(respuesta){
                    Log.i(TAG, "Todo okey");
                }else{
                    Log.i(TAG,"Error en la creacion del historial");
                }
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
        aceptado = false;
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
        Log.i(TAG, "tratarVehiculo");
        conductor = (Usuario)informacion;
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_OBTENER_VEHICULO);
        modelo.obtenerVehiculoUsuario(conductor.getDatoVehiculo());
    }

    @Override
    public void tratarOk(Object informacion) {
        user = (Usuario)informacion;
        appMediador.registerReceiver(receptorDeAvisos,AppMediador.AVISO_CREACION_VINCULO);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String hora = tf.format(Calendar.getInstance().getTime());
        String fecha = df.format(Calendar.getInstance().getTime());
        Object[] datos = new Object[7];
        datos[0] = user.getIdUser();
        datos[1] = conductor.getIdUser();
        datos[2] = fecha;
        datos[3] = hora;
        datos[4] = user.getOrigenDef();
        datos[5] = user.getDestino();
        datos[6] = 1;
        vinculo = new Vinculo(user.getIdUser(),conductor.getIdUser(),false,fecha,hora,user.getOrigenDef(),user.getDestino());
        modelo.guardarUsuarioPickup(datos);
    }

    @Override
    public void esperarRespuesta(){
        Log.i(TAG, "Esperar respuesta");
        appMediador.registerReceiver(receptorDeRespuestas,AppMediador.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR);
        appMediador.registerReceiver(receptorDeRespuestas,AppMediador.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR);
        String[] vinculo = new String[]{user.getIdUser(),conductor.getIdUser()};
        modelo.obtenerRespuestaConductor(vinculo);

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

    @Override
    public void generarHistorial(){
        appMediador.registerReceiver(receptorDeRespuestas,AppMediador.AVISO_CREACION_HISTORIAL);
        Object[] info = new Object[]{vinculo,conductor.getNombre(),user.getNombre()};
        modelo.agregarHistorial(info);
    }

}
