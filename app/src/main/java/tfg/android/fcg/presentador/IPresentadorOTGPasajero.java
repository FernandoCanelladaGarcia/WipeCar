package tfg.android.fcg.presentador;

public interface IPresentadorOTGPasajero {

    /**
     * Encargado de obtener un mapa centrado en la localizacion del pasajero
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);

    /**
     * Encargado de buscar los vehiculos que van al mismo destino que el pasajero.
     * @param informacion contendra:
     */
    public void tratarBuscar(Object informacion);

    /**
     * Encargado de presentar al pasajero la informacion relativa
     * al vehiculo seleccionado.
     * @param informacion contendra:
     */
    public void tratarVehiculo(Object informacion);


    /**
     * Trata la selección del boton OK.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Trata la seleccion del boton cancelar.
     * @param informacion contendra:
     */
    public void tratarCancelar(Object informacion);
}
