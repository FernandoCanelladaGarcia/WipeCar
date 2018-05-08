package tfg.android.fcg.vista;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.Modelo;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ModeloTest extends ActivityInstrumentationTestCase2 {

    private AppMediador appMediador;
    private CountDownLatch contador;
    private Modelo modelo;

    private final String TAG = "depurador";

    public ModeloTest(){
        super(VistaLogin.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(false);
        appMediador = (AppMediador)this.getActivity().getApplication();
    }

    @Test
    public void testComprobarRegistroUsuarioCorrecto() throws Exception{
        Log.i(TAG, "metodo testComprobarRegistroUsuarioCorrecto");
        String[] registro = new String[] {"fernando.canellada101@alu.ulpgc.es","123456","Fernando Canellada","673347971","Leon y Castillo nº39"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaRegistroUsuarioCorrecto();
        modelo.registrarUsuario(registro);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testComprobarLoginCorrecto() throws Exception{
        Log.i(TAG, "metodo testCOmprobarLoginCorrecto");
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es", "123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaLoginCorrecto();
        modelo.comprobarLogin(login);
        contador.await(10000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testComprobarLoginIncorrecto() throws Exception{
        Log.i(TAG, "metodo testCOmprobarLoginIncorrecto");
        String[] login = new String[] {"fernando.canellada101@ulpgc.es", "123456"}; //Email no registrado
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaLoginIncorrecto();
        modelo.comprobarLogin(login);
        contador.await(10000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testComprobarLoginEmailCorrectoPasswordIncorrecto() throws Exception{
        Log.i(TAG, "metodo testComprobarLoginEmailCorrectoPasswordIncorrecto");
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","111111"}; //Email registrado contraseña no correcta.
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaLoginEmailCorrectoPasswordIncorrecto();
        modelo.comprobarLogin(login);
        contador.await(10000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testComprobarELiminarUsuario() throws Exception{
        Log.i(TAG, "metodo testComprobarELiminarUsuario");
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es", "123456"};
//        String perfil = "";
        modelo = Modelo.getInstance();
        modelo.comprobarLogin(login);
        contador = new CountDownLatch(1);
        esperarRespuestaEliminarUsuarioCorrecto();
        modelo.eliminarPerfil("vhyuQd4tPgRObCZ28TKvXPU8gPr1");
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testActualizarLoginCorrecto() throws Exception{
        //INFORMACION USUARIO = nombre, telefono, email, password, origen, destino, rol, datovehiculo, fecha, hora, valoracion
        Log.i(TAG, "metodo testActualizarUsuarioCorrecto");
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        String[] actualizacion = new String[]
                {"2","Fernando Canellada","673347971","fernando.canellada101@ulpgc.es","111111","","","true","","","",""};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaActualizacionCorrecto();
        modelo.comprobarLogin(login);
        modelo.guardarPerfil(actualizacion);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testActualizarOrigenYDestinoCorrecto() throws Exception{
        Log.i(TAG, "metodo testActualizarOrigenYDestinoCorrecto");
        String[] origenYDestino = new String []{"1","","","","","Plaza de San Telmo","Facultad de Ingenieria","","","","",""};
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaActualizacionCorrecto();
        modelo.comprobarLogin(login);
        modelo.guardarPerfil(origenYDestino);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testActualizarValoracionCorrecto() throws Exception{
        Log.i(TAG, "metodo testActualizarValoracionCorrecto");
        String[] valoracion = new String []{"3","","","","","","","","","","","5"};
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaActualizacionCorrecto();
        modelo.comprobarLogin(login);
        modelo.guardarPerfil(valoracion);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testActualizarFechaYHoraCorrecto() throws Exception{
        Log.i(TAG, "metodo testActualizarFechaYHoraCorrecto");
        String[] fechaYHora = new String []{"4","","","","","","","","","20/12/14","22:50",""};
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaActualizacionCorrecto();
        modelo.comprobarLogin(login);
        modelo.guardarPerfil(fechaYHora);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testActualizarDatosVehiculoUsuario() throws Exception{
        Log.i(TAG, "metodo testActualizarDatosVehiculo");
        String[] datoVehiculo = new String []{"5","","","","","","","","eray7261gebe82e7012e2y8","","",""};
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaActualizacionCorrecto();
        modelo.comprobarLogin(login);
        modelo.guardarPerfil(datoVehiculo);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testAgregarVehiculoCorrecto() throws Exception{
        Log.i(TAG, "metodo testAgregarVehiculoCorrecto");
        String[] datosVehiculo = new String[] {"Marca","Modelo","0000ABC"};
        //String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaAgregarVehiculo();
        //modelo.comprobarLogin(login);
        modelo.guardarVehiculo(datosVehiculo);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testAgregarPosicion() throws Exception{
        Log.i(TAG, "metodo testAgregarPosicion");
        String[] posicion = new String[] {"idUser","latitud","longitud"};
        //String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaAgregarPosicion();
        //modelo.comprobarLogin(login);
        modelo.guardarLocalizacion(posicion);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testAgregarVinculo() throws Exception{
        Log.i(TAG, "metodo testAgregarVinculo");
        Object[] vinculo = new Object[] {"idUserPasajero","idUserConductor",false,"fecha","hora","origen","destino"};
        //String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaAgregarVinculo();
        //modelo.comprobarLogin(login);
        modelo.guardarUsuarioPickup(vinculo);
        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    @Test
    public void testAgregarHistorial() throws Exception{
        Log.i(TAG, "metodo testAgregarHistorial");
        Object[] vinculo = new Object[] {"idUserPasajero","idUserConductor","fecha","hora","origen","destino","valoracionPasajero","valoracionConductor"};
        //String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","123456"};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaAgregarHistorial();
        //modelo.comprobarLogin(login);

        contador.await(15000,TimeUnit.MILLISECONDS);
    }

    private void esperarRespuestaLoginCorrecto() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_USER_LOGIN)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_LOGIN,false);
                    if(resultado){
                        //Se pudo hacer login, TEST OK, se hace logout.
                        Log.i(TAG,"Login realizado con exito, TEST OK");
                        modelo.getAuth().signOut();
                    }else{
                        Log.i(TAG, "No se pudo realizar el login. TEST FALLIDO");
                        fail();
                    }
                    contador.countDown();
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_USER_LOGIN);
    }

    private void esperarRespuestaLoginIncorrecto() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_USER_LOGIN)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_LOGIN,false);
                    if(resultado){
                        //No se debia hacer login, TEST OK, se hace logout.
                        Log.i(TAG,"Login realizado con exito, TEST FALLIDO");
                        modelo.getAuth().signOut();
                        fail();
                    }else{
                        Log.i(TAG, "No se pudo realizar el login. TEST OK");
                    }
                    contador.countDown();
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_USER_LOGIN);
    }

    private void esperarRespuestaLoginEmailCorrectoPasswordIncorrecto() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_USER_LOGIN)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_LOGIN,false);
                    if(resultado){
                        //Se pudo loguear, sign-out ya que es un test. TEST FALLIDO.
                        modelo.getAuth().signOut();
                        fail();
                    }else{
                        Log.i(TAG, "No se pudo loguear porque el password es incorrecto. TEST OK");
                    }
                    contador.countDown();
                }
                appMediador.unRegisterReceiver(this);
            }

        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_USER_LOGIN);
    }

    private void esperarRespuestaRegistroUsuarioCorrecto() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppMediador.AVISO_REGISTRO_USUARIO)){
                boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_REGISTRO_USUARIO,false);
                if(resultado){
                    //Se pudo registrar el usuario. TEST OK, sign-out.
                    modelo.getAuth().signOut();
                }else{
                    Log.i(TAG,"No se pudo registrar el usuario. TEST FALLIDO");
                    fail();
                }
                contador.countDown();;
            }
            appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_REGISTRO_USUARIO);
    }

    private void esperarRespuestaActualizacionCorrecto() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_ACTUALIZACION_USUARIO)){
                    Object[] respuesta = (Object[])intent.getSerializableExtra(AppMediador.CLAVE_ACTUALIZACION_USUARIO);
                    boolean resultado = (Boolean) respuesta[0];
                    String error = (String)respuesta[1];
                    if(resultado){
                        //Se pudo actualizar el usuario. TEST OK, sign-out.
                        modelo.getAuth().signOut();
                    }else{
                        Log.i(TAG,"No se pudo actualizar el usuario. TEST FALLIDO");
                        Log.i(TAG, error);
                        fail();
                    }
                    contador.countDown();;
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_ACTUALIZACION_USUARIO);

    }

    private void esperarRespuestaAgregarVehiculo() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_REGISTRO_VEHICULO)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_NUEVO_VEHICULO,false);
                    if(resultado){
                        //Se pudo Agregar vehiculo. TEST OK, sign-out.
                        modelo.getAuth().signOut();
                    }else{
                        Log.i(TAG,"No se pudo Agregar vehiculo. TEST FALLIDO");
                        fail();
                    }
                    contador.countDown();;
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_REGISTRO_VEHICULO);
    }

    private void esperarRespuestaEliminarUsuarioCorrecto() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_ELIMINAR_USUARIO)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_ELIMINAR_USUARIO,false);
                    if(resultado){
                        //Se pudo Eliminar perfil. TEST OK, sign-out.

                    }else{
                        Log.i(TAG,"No se pudo eliminar usuario. TEST FALLIDO");
                        fail();
                    }
                    contador.countDown();;
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_ELIMINAR_USUARIO);
    }

    private void esperarRespuestaAgregarPosicion() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_REGISTRO_POSICION)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_RESULTADO_REGISTRO_POSICION,false);
                    if(resultado){
                        //Se pudo agregar posicion. TEST OK, sign-out.

                    }else{
                        Log.i(TAG,"No se pudo agregar posicion. TEST FALLIDO");
                        fail();
                    }
                    contador.countDown();;
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_REGISTRO_POSICION);
    }

    private void esperarRespuestaAgregarVinculo() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_CREACION_VINCULO)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_CREACION_VINCULO,false);
                    if(resultado){
                        //Se pudo agregar vinculo. TEST OK, sign-out.

                    }else{
                        Log.i(TAG,"No se pudo agregar vinculo. TEST FALLIDO");
                        fail();
                    }
                    contador.countDown();;
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_CREACION_VINCULO);
    }

    private void esperarRespuestaAgregarHistorial() throws Exception{
        BroadcastReceiver receptor = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(AppMediador.AVISO_CREACION_HISTORIAL)){
                    boolean resultado = intent.getBooleanExtra(AppMediador.CLAVE_CREACION_HISTORIAL,false);
                    if(resultado){
                        //Se pudo agregar historial. TEST OK, sign-out.

                    }else{
                        Log.i(TAG,"No se pudo agregar historial. TEST FALLIDO");
                        fail();
                    }
                    contador.countDown();;
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_CREACION_HISTORIAL);
    }

}
