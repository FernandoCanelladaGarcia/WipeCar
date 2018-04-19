package tfg.android.fcg.vista;

public interface IVistaOTGConductor {

    /**
     * Indicacion sonora de peticion de compartir ruta de un pasajero
     * @param informacion contendra:
     */
    public void indicarPeticionPasajero(Object informacion);

    /**
     * Indicacion de que el pasajero ha sido aceptado.
     * @param informacion contendra:
     */
    public void indicarPasajeroAceptado(Object informacion);

    /**
     * Indicacion de que el pasajero ha sido rechazado.
     * @param informacion contendra:
     */
    public void indicarPasajeroRechazado(Object informacion);

}
