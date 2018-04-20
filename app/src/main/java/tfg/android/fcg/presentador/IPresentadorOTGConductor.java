package tfg.android.fcg.presentador;

public interface IPresentadorOTGConductor {

    /**
     * Encargado de obtener la localización del conductor
     * y las peticiones de los pasajeros que quieren compartir ruta con él.
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);

    /**
     * Realiza los operaciones oportunas para informar que el pasajero ha sido aceptado.
     * @param informacion contendra:
     */
    public void tratarAceptar(Object informacion);

    /**
     * Realiza los operaciones oportunas para informar que el pasajero ha sido rechazado.
     * @param informacion contendra:
     */
    public void tratarRechazar(Object informacion);

    /**
     * Realiza los operaciones oportunas para parar el proceso
     * de aceptación o rechazo del pasajero.
     * @param informacion contendra:
     */
    public void tratarParar(Object informacion);
}
