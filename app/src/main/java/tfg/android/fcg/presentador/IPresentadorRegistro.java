package tfg.android.fcg.presentador;

public interface IPresentadorRegistro {


    /**
     * Comprueba la informacion de registro de un usuario,
     * informa si todo esta correcto o no.
     * @param informacion contendra:
     */
    public void tratarRegistro(Object informacion);

    /**
     * Trata la selección del boton OK.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Comunica al modelo para que realice el registro de un nuevo usuario.
     * @param informacion contendra:
     */
    public void tratarAceptarRegistro(Object informacion);


    /**
     * Trata la seleccion del boton cancelar.
     * @param informacion contendra:
     */
    public void tratarCancelar(Object informacion);

}
