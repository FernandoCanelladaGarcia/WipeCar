package tfg.android.fcg.presentador;

public interface IPresentadorMapaOrigen {

    /**
     * Arregla la interfaz de usuario para introducir el origen y el destino.
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);

    /**
     * Obtiene un mapa centrado en la localización del pasajero.
     * @param informacion contendra:
     */
    public void tratarOrigen(Object informacion);

    /**
     * Coloca en el mapa la marca en el origen seleccionado por el pasajero.
     * @param informacion contendra:
     */
    public void tratarSeleccionarOrigen(Object informacion);

    /**
     * Presentar el diálogo a traves del cual se introducen el origen y el destino
     * cuando se sale de la pantalla de mapa.
     * @param informacion contendra:
     */
    public void tratarSalirMapa(Object informacion);

    /**
     * Realiza los operaciones oportunas para almacenar
     * la información de origen y destino del pasajero.
     * @param informacion contendra:
     */
    public void tratarOrigenYDestino(Object informacion);
}
