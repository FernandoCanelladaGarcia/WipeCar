package tfg.android.fcg.presentador;

public interface IPresentadorRegistro {


    /**
     * Comprueba la información de registro de un usuario,
     * informa si esta correcta o no.
     * @param informacion contendra:
     */
    public void tratarRegistro(Object informacion);

    /**
     * Trata la selección del botón Ok.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Realiza los operaciones oportunas para que se realice el registro de un nuevo usuario.
     * @param informacion contendra:
     */
    public void tratarAceptarRegistro(Object informacion);

    /**
     * Trata la selección del botón Cancelar.
     */
    public void tratarCancelar();

}
