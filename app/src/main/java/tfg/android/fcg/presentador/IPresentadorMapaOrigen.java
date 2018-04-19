package tfg.android.fcg.presentador;

public interface IPresentadorMapaOrigen {

    /**
     * Arregla la interfaz de usuario para introducir el origen y el destino
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);

    /**
     * Obtiene un mapa centrado en la localizacion del pasajero
     * @param informacion contendra:
     */
    public void tratarOrigen(Object informacion);

    /**
     * Coloca en el mapa la marca en el origen seleccionado por el pasajero
     * @param informacion contendra:
     */
    public void tratarSeleccionarOrigen(Object informacion);

    /**
     * Encargado de saber cuando se sale del mapa para presentar
     * el dialogo a travez del cual se introducen el origen y el destino
     * @param informacion contendra:
     */
    public void tratarSalirMapa(Object informacion);

    /**
     * Se comunica con el modelo para almacenar
     * la informacion de origen y destino del pasajero
     * @param informacion contendra:
     */
    public void tratarOrigenYDestino(Object informacion);
}
