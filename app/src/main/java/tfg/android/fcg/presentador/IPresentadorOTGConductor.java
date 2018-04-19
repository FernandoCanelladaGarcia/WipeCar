package tfg.android.fcg.presentador;

public interface IPresentadorOTGConductor {

    /**
     * Encargado de obtener la localizacion del conductor
     * y las peticiones de los pasajeros que quieren compartir ruta con el.
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);

    /**
     * Informa al modelo que el pasajero ha sido aceptado.
     * @param informacion contendra:
     */
    public void tratarAceptar(Object informacion);

    /**
     * Informa al modelo que el pasajero ha sido rechazado.
     * @param informacion contendra:
     */
    public void tratarRechazar(Object informacion);

    /**
     * Informa al modelo de parar el proceso
     * de aceptacion o rechazo del pasajero.
     * @param informacion contendra:
     */
    public void tratarParar(Object informacion);
}
