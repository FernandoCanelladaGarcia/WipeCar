package tfg.android.fcg.presentador;

public interface IPresentadorLogin {

    /**
     * Realiza los operaciones oportunas para comprobar
     * la información introducida por el usuario.
     * @param informacion contendra:
     */
    public void tratarLogin(Object informacion);

    /**
     * Trata la selección del botón Ok.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Realiza las operaciones necesarias para recuperar la contraseña
     * del usuario.
     * @param informacion contendra:
     */
    public void tratarRecuperarPassword(Object informacion);

    /**
     * Realiza las operaciones oportunas para cambiar la contraseña
     * del usuario.
     * @param informacion contendra:
     */
    public void tratarCambiarPassword(Object informacion);

    /**
     * Realiza las operaciones oportunas para registrar un nuevo
     * usuario en la aplicación.
     */
    public void tratarNuevo();
}
