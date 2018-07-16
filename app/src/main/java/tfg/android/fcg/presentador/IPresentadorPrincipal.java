package tfg.android.fcg.presentador;

public interface IPresentadorPrincipal {

    /**
     * Realiza los operaciones oportunas para obtener la información necesaria para cargar
     * la lista de conductores o pasajeros que van a realizar un viaje al destino del usuario.
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);

    /**
     * Recupera la selección del usuario y la confirmación de la selección.
     * @param informacion contendra:
     */
    public void tratarSeleccion(Object informacion);

    /**
     * Trata la selección del botón Ok.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Elimina la selección del usuario (de su acompañante).
     * @param informacion contendra:
     */
    public void tratarBorrarSeleccion(Object informacion);

    /**
     * Presenta la vista de perfil,
     * para que el usuario pueda cambiar la información de su perfil.
     * @param informacion contendra:
     */
    public void tratarConfiguracion(Object informacion);

    /**
     * Trata la selección del botón Cancelar.
     * @param informacion contendra:
     */
    //TODO: NO SE USAN
    public void tratarCancelar(Object informacion);

    /**
     * Trata la selección del botón Chat, para poner en contacto a los usuarios acompañantes.
     * @param informacion contendra:
     */
    public void tratarChat(Object informacion);

    /**
     * Presenta la vista de mapa, para que el pasajero pueda cambiar de origen y/o destino.
     * @param informacion contendra:
     */
    public void tratarMapa(Object informacion);

    /**
     * Presenta la vista On the Go
     * para que el usuario pueda elegir acompañantes en tiempo real.
     * @param informacion
     */
    public void tratarOnTheGo(Object informacion);

    //TODO: NUEVOS, REDACCION
    public void obtenerVehiculos(Object informacion);

    public void obtenerConductores(Object informacion);

    public void obtenerPeticionesPasajeros(Object informacion);

    public void obtenerVinculosPasajero(Object informacion);

    public void obtenerVehiculosVinculo(Object informacion);
}
