package tfg.android.fcg.vista;

public interface IVistaOTGConductor {

    /**
     * Indicación sonora de petición de compartir ruta de un pasajero.
     * @param informacion contendra:
     */
    public void indicarPeticionPasajero(Object informacion);

    /**
     * Indicación sonora de que el pasajero ha sido aceptado.
     * @param informacion contendra:
     */
    public void indicarPasajeroAceptado(Object informacion);

    /**
     * Indicación sonora de que el pasajero ha sido rechazado.
     * @param informacion contendra:
     */
    public void indicarPasajeroRechazado(Object informacion);

    /**
     * Indicación sonora de que ha sucedido algún problema
     */
    //TODO NUEVO, REDACCION
    public void indicarProblema();

}
