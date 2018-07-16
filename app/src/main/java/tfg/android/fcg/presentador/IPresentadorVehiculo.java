package tfg.android.fcg.presentador;

public interface IPresentadorVehiculo {

    /**
     * Comprueba la nueva información de registro de vehículo de un usuario
     * e informar al mismo si esta correcto o no.
     * @param informacion contendra:
     */
    public void tratarGuardar(Object informacion);

    /**
     * Realiza los operaciones oportunas para que se realice el registro del vehículo del usuario.
     * @param informacion contendra:
     */
    public void tratarGuardarVehiculo(Object informacion);

    //TODO: NO SE USAN, SE EDITA EN PERFIL.
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
     * Presenta una ventana de diálogo para que
     * el usuario pueda confirmar la eliminación de su vehículo.
     * @param informacion contendra:
     */
    public void tratarPapelera(Object informacion);

    /**
     * Realiza los operaciones oportunas para que se elimine de la base de datos
     * el vehículo del usuario.
     * @param informacion contendra:
     */
    public void tratarEliminarVehiculo(Object informacion);
}
