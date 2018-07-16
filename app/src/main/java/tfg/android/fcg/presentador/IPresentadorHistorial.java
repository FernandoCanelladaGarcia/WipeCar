package tfg.android.fcg.presentador;

public interface IPresentadorHistorial {

    /**
     * Realiza los operaciones oportunas para obtener la información necesaria para cargar la lista
     * de conductores y pasajeros que han realizado un viaje con el usuario.
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);


    /**
     * Comprueba la valoración del usuario sobre
     * un acompañante e informar al usuario si esta correcto o no.
     * @param informacion contendra:
     */
    public void tratarValorar(Object informacion);

    /**
     * Realiza la valoración del usuario sobre los acompañantes.
     * @param informacion contendra:
     */
    public void tratarValoracion(Object informacion);

    /**
     * Comprueba la eliminacion del usuario sobre la referencia del vinculo del historial
     * @param informacion contendra:
     */
    public void tratarEliminar(Object informacion);

    /**
     * Elimina de la tabla Historial la referencia de un vinculo
     * realizado por el usuario de la aplicación
     */
    //TODO: EDITADO, REDACCION
    public void eliminarHistorial();

    /**
     * Realiza los operaciones oportunas para obtener
     * la información necesaria para cargar más datos en la lista
     * de conductores y pasajeros que han realizado un viaje con el usuario.
     * @param informacion contendra:
     */
    //TODO: NO SE USA, REDACCION
    public void tratarCarga(Object informacion);

    /**
     * Presenta la vista de perfil.
     */
    public void tratarVolver();

    /**
     * Realiza el logout del usuario conectado en la aplicación
     */
    //TODO: NUEVO, REDACCION
    public void tratarSalir();
}
