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
}
