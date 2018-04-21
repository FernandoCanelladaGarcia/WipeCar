package tfg.android.fcg.presentador;

public interface IPresentadorPerfil {

    /**
     * Comprueba la nueva información de perfil de un usuario
     * e informa si es correcto o no.
     * @param informacion contendra:
     */
    public void tratarGuardar(Object informacion);

    /**
     * Trata la selección del botón Ok.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Realiza los operaciones oportunas para modificar el perfil del usuario.
     * @param informacion contendra:
     */
    public void tratarGuardarPerfil(Object informacion);

    /**
     * Trata la selección del botón Cancelar.
     * @param informacion contendra:
     */
    public void tratarCancelar(Object informacion);

    /**
     * Prepara la interfaz de usuario para la edición.
     * @param informacion contendra:
     */
    public void tratarEditar(Object informacion);

    /**
     * Presenta la vista de historial para que el usuario
     * pueda comprobar el listado de su historial.
     * @param informacion contendra:
     */
    public void tratarHistorial(Object informacion);

    /**
     * Presenta uan ventana de diálogo con la información de ayuda.
     * @param informacion contendra:
     */
    public void tratarAyuda(Object informacion);

    /**
     * Presenta una ventana de diálogo
     * para que el usuario pueda confirmar la eliminación del perfil.
     * @param informacion contendra:
     */
    public void tratarPapelera(Object informacion);

    /**
     * Elimina el perfil del usuario.
     * @param informacion contendra:
     */
    public void tratarEliminarPerfil(Object informacion);

    /**
     * Presenta la vista de registro de vehículo,
     * para que el usuario introduzca la información de su vehículo.
     * @param informacion contendra:
     */
    public void tratarConductor(Object informacion);

}
