package tfg.android.fcg.presentador;

public interface IPresentadorMapaOrigen {

    /**
     * Arregla la interfaz de usuario para introducir el origen y el destino.
     */
    public void iniciar();

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
     * Presenta el diálogo a traves del cual se introducen el origen y el destino
     * cuando se sale de la pantalla de mapa.
     */
    public void tratarSalirMapa();

    /**
     * Realiza los operaciones oportunas para almacenar
     * la información de origen y destino del pasajero.
     * @param informacion contendra:
     */
    public void tratarOrigenYDestino(Object informacion);
}
