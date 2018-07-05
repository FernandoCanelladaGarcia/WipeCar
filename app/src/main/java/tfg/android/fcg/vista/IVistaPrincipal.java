package tfg.android.fcg.vista;

public interface IVistaPrincipal {

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
     * Presenta la lista de conductores o pasajeros.
     * @param informacion contendra:
     */
    public void mostrarUsuarios(Object informacion);

    /**
     * Destaca un pasajero o conductor tras su selección.
     * @param informacion contendra:
     */
    public void mostrarSeleccion(Object informacion);

    /**
     * Desmarca un pasajero o conductor y muestra el resultado.
     * @param informacion contendra:
     */
    public void desmarcarUsuario(Object informacion);

    public void setUsuario(Object informacion);

    public void setConductores(Object informacion);

    public void setVehiculos(Object informacion);

    public void setPasajeros(Object informacion);

}
