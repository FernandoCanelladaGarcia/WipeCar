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
    public void testActualizarUsuarioCorrecto() throws Exception{
        //INFORMACION USUARIO = nombre, telefono, direccion, email, password, origen, destino, rol, datovehiculo, fecha, hora, valoracion
        Log.i(TAG, "metodo testActualizarUsuarioCorrecto");
        String[] actualizacion = new String[]
                {"Fernando Canellada","673347971","Leon y Castillo 39","fernando.canellada101@alu.ulpgc.es","123456","Origen","Destino","false","vacio","","",""};
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaActualizacionCorrecto();
        modelo.guardarPerfil(actualizacion);
        contador.await(10000,TimeUnit.MILLISECONDS);
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
                        Log.i(TAG,"Login realizado con exito, TEST OK");
                        modelo.getAuth().signOut();
                        fail();
                    }else{
                        Log.i(TAG, "No se pudo realizar el login. TEST FALLIDO");
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
                    }
                    contador.countDown();;
                }
                appMediador.unRegisterReceiver(this);
            }
        };
        appMediador.registerReceiver(receptor,AppMediador.AVISO_ACTUALIZACION_USUARIO);
    }
}
