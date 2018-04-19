package tfg.android.fcg.vista;

public interface IVistaLogin {

    /**
     * Muestra una barra de progreso. Existe una acción en background.
     */
    public void mostrarProgreso();

    /**
     * Elimina la barra de progreso. Acción en background finalizada.
     */
    public void eliminarProgreso();

    /**
     * Muestra un diálogo al usuario para realizar una determinada acción.
     * @param informacion contendra:
     */
    public void mostrarDialogo(Object informacion);

    /**
     * Cierra el dialogo mostrado al usuario tras su uso util.
     */
    public void cerrarDialogo();

}
