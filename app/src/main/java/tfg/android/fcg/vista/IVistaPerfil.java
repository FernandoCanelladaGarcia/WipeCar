package tfg.android.fcg.vista;

public interface IVistaPerfil {

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
     * Habilita la edición de los datos personales del usuario.
     * @param informacion contendra:
     */
    public void prepararEdicion(Object informacion);

}
