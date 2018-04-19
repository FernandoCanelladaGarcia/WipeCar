package tfg.android.fcg.presentador;

public interface IPresentadorPerfil {

    /**
     * Comprueba la nueva informacion de perfil de un usuario
     * e informa si es correcto o no.
     * @param informacion contendra:
     */
    public void tratarGuardar(Object informacion);

    /**
     * Trata la selección del boton OK.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Encargado de comunicarse con el modelo
     * para realizar la modificacion del perfil del usuario.
     * @param informacion contendra:
     */
    public void tratarGuardarPerfil(Object informacion);

    /**
     * Trata la seleccion del boton cancelar.
     * @param informacion contendra:
     */
    public void tratarCancelar(Object informacion);

    /**
     * Prepara la interfaz de usuario para la edicion
     * @param informacion contendra:
     */
    public void tratarEditar(Object informacion);

    /**
     * Presenta la vista de historial para que el usuario
     * pueda comprobar el listado de su historial
     * @param informacion contendra:
     */
    public void tratarHistorial(Object informacion);

    /**
     * Presenta uan ventana de dialogo con la informacion de ayuda
     * @param informacion contendra:
     */
    public void tratarAyuda(Object informacion);

    /**
     * Presenta una ventana de dialogo
     * para que el usuario pueda confirmar la eliminacion del perfil
     * @param informacion contendra:
     */
    public void tratarPapelera(Object informacion);

    /**
     * Encargado de eliminar el perfil del usuario.
     * @param informacion contendra:
     */
    public void tratarEliminarPerfil(Object informacion);

    /**
     * Presenta la vista de registro de vehiculo,
     * para que el usuario, introduzca la informacion de su vehiculo
     * @param informacion contendra:
     */
    public void tratarConductor(Object informacion);

}
