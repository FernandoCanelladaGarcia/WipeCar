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
     * Trata la selección del botón Cancelar.
     * @param informacion contendra:
     */
    public void tratarCancelar(Object informacion);

    //TODO: NUEVO, REDACCION
    public void obtenerConductores(Object informacion);

    public void obtenerPosicionConductores(Object informacion);
}
