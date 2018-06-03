package tfg.android.fcg.presentador;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.modelo.IModelo;
import tfg.android.fcg.modelo.Modelo;
import tfg.android.fcg.vista.VistaLogin;
import tfg.android.fcg.vista.VistaPerfil;
import tfg.android.fcg.vista.VistaPrincipal;

public class PresentadorPrincipal implements IPresentadorPrincipal{

    private IModelo modelo;
    private AppMediador appMediador;
    private VistaPrincipal vistaPrincipal;

    public PresentadorPrincipal(){
        appMediador = AppMediador.getInstance();
        modelo = Modelo.getInstance();
        vistaPrincipal = (VistaPrincipal) appMediador.getVistaPrincipal();
    }

    @Override
    public void iniciar(Object informacion) {

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
                appMediador.launchActivity(VistaLogin.class, this, null);
                break;
        }

    }

    @Override
    public void tratarOnTheGo(Object informacion) {

    }
}
