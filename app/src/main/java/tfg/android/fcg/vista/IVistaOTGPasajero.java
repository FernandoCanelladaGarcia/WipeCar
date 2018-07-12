package tfg.android.fcg.vista;

public interface IVistaOTGPasajero {

    /**
     * Muestra una barra de progreso. Existe una acción en background.
     */
    public void mostrarProgreso();

    /**
     * Elimina la barra de progreso. Acción en background finalizada.
     */
    public void cerrarProgreso();

    /**
     * Muestra un diálogo al usuario para realizar una determinada acción.
     * @param informacion contendra:
     */
    public void mostrarDialogo(Object informacion);

    /**
     * Cierra el diálogo mostrado al usuario tras su uso útil.
     */
    public void cerrarDialogo();

    /**
     * Muestra el mapa en la pantalla centrado en la ubicación del usuario.
     * @param informacion contendra:
     */
    public void mostrarMapaConPosicion(Object informacion);

    //TODO: EDITADO, REDACCION
    /**
     * Muestra en el mapa los vehículos encontrados.
     */
    public void mostrarVehiculos();

    public void mostrarVehiculoVinculo();

}
