package tfg.android.fcg.presentador;

public class PresentadorPrincipal implements IPresentadorPrincipal{

    /**
     * Solicita al modelo la informacion necesaria para cargar
     * la lista de conductores o pasajeros que van a realizar un viaje al destino del usuario.
     * @param informacion contendra:
     */
    @Override
    public void iniciar(Object informacion) {

    }

    /**
     * Encargado de conocer la seleccion del usuario y la confirmacion de la seleccion.
     * @param informacion contendra:
     */
    @Override
    public void tratarSeleccion(Object informacion) {

    }


    /**
     * Trata la selección del boton OK.
     * @param informacion contendra:
     */
    @Override
    public void tratarOk(Object informacion) {

    }

    /**
     * Trata la seleccion del boton cancelar.
     * @param informacion contendra:
     */
    @Override
    public void tratarCancelar(Object informacion) {

    }

    /**
     * Trata la seleccion del boton chat, para poner en contacto a los usuarios acompañantes.
     * @param informacion contendra:
     */
    @Override
    public void tratarChat(Object informacion) {

    }

    /**
     * Presenta la vista de mapa, para que el pasajero pueda cambiar de origen y/o destino
     * @param informacion contendra:
     */
    @Override
    public void tratarMapa(Object informacion) {

    }

    /**
     * Elimina la seleccion del usuario (de su acompañante)
     * @param informacion contendra:
     */
    @Override
    public void tratarBorrarSeleccion(Object informacion) {

    }

    /**
     * Presenta la vista de perfil,
     * para que el usuario pueda cambiar la informacion de su perfi.
     * @param informacion contendra:
     */
    @Override
    public void tratarConfiguracion(Object informacion) {

    }

    /**
     * Presenta la vista On the Go
     * para que el usuario pueda elegir acompañantes en tiempo real.
     * @param informacion
     */
    @Override
    public void tratarOnTheGo(Object informacion) {

    }
}
