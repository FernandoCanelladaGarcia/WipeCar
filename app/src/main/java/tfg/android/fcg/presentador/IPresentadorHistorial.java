package tfg.android.fcg.presentador;

public interface IPresentadorHistorial {

    /**
     * Solicita al modelo la informacion necesaria para cargar la lista
     * de conductores y pasajeros que han realizado un viaje con el usuario
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);


    /**
     * Comprueba la valoracion del usuario sobre
     * un acompañante e informar al usuario si esta correcto o no.
     * @param informacion contendra:
     */
    public void tratarValorar(Object informacion);

    /**
     * Comunica al modelo para realizar
     * la valoracion del usuario sobre los acompañantes.
     * @param informacion contendra:
     */
    public void tratarValoracion(Object informacion);

    /**
     * Comunica al modelo para solicitar
     * la informacion necesaria para cargar mas datos en la lista
     * de conductores y pasajeros que han realizado un viaje con el usuario.
     * @param informacion contendra:
     */
    public void tratarCarga(Object informacion);

    /**
     * Presenta la vista de perfil
     * @param informacion
     */
    public void tratarVolver(Object informacion);
}
