package tfg.android.fcg.presentador;

public interface IPresentadorVehiculo {

    /**
     * Comprueba la nueva informacion de registro de vehiculo de un usuario
     * e informar al mismo si esta correcto o no.
     * @param informacion contendra:
     */
    public void tratarGuardar(Object informacion);

    /**
     * Comunica al modelo que realice el registro del vehiculo del usuario.
     * @param informacion contendra:
     */
    public void tratarGuardarVehiculo(Object informacion);

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
     * Presenta una ventana de dialogo para que
     * el usuario pueda confirmar la eliminacion de su vehiculo
     * @param informacion contendra:
     */
    public void tratarPapelera(Object informacion);

    /**
     * Comunica al modelo de eliminar de la base de datos
     * el vehiculo del usuario.
     * @param informacion contendra:
     */
    public void tratarEliminarVehiculo(Object informacion);
}
