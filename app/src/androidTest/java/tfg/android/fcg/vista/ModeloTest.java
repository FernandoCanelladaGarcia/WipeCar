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
        String[] login = new String[] {"fernando.canellada101@alu.ulpgc.es","111111"}; //Email registrado contraseña no correcta.
        modelo = Modelo.getInstance();
        contador = new CountDownLatch(1);
        esperarRespuestaLoginEmailCorrectoPasswordIncorrecto();
        modelo.comprobarLogin(login);
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
}
