package tfg.android.fcg.presentador;

public interface IPresentadorOTGPasajero {

    /**
     * Obtiene un mapa centrado en la localización del pasajero
     */
    public void iniciar();

    /**
     * Busca los vehículos que van al mismo destino que el pasajero.
     * @param informacion contendra:
     */
    public void tratarBuscar(Object informacion);

    /**
     * Presenta al pasajero la información relativa
     * al vehículo seleccionado.
     * @param informacion contendra:
     */
    public void tratarVehiculo(Object informacion);


    /**
     * Trata la selección del botón Ok.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Recupera la lista de conductores que se dirigen al mismo destino que el pasajero.
     * @param informacion
     */
    public void obtenerConductores(Object informacion);

    /**
     * Recupera la posición de los conductores que se desplazan al destino del pasajero.
     * @param informacion
     */
    public void obtenerPosicionConductores(Object informacion);

    /**
     * Recupera las respuestas a las solicitudes realizadas por el usuario.
     */
    public void esperarRespuesta();

    /**
     * Almacena en la base de datos la información necesaria para crear una entrada al historial del usuario.
     */
    public void generarHistorial();
}
