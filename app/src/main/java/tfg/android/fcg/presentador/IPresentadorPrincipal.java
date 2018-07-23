package tfg.android.fcg.presentador;

public interface IPresentadorPrincipal {

    /**
     * Realiza los operaciones oportunas para obtener la información necesaria para cargar
     * la lista de conductores o pasajeros que van a realizar un viaje al destino del usuario.
     * @param informacion contendra:
     */
    public void iniciar(Object informacion);

    /**
     * Recupera la selección del usuario y la confirmación de la selección.
     * @param informacion contendra:
     */
    public void tratarSeleccion(Object informacion);

    /**
     * Trata la selección del botón Ok.
     * @param informacion contendra:
     */
    public void tratarOk(Object informacion);

    /**
     * Elimina la selección del usuario (de su acompañante).
     * @param informacion contendra:
     */
    public void tratarBorrarSeleccion(Object informacion);

    /**
     * Presenta la vista de perfil,
     * para que el usuario pueda cambiar la información de su perfil.
     * @param informacion contendra:
     */
    public void tratarConfiguracion(Object informacion);

    /**
     * Recupera los vehículos de los conductores que se dirigen al destino del usuario.
     * @param informacion
     */
    public void obtenerVehiculos(Object informacion);

    /**
     * Recupera los conductores que se dirigen al destino del usuario.
     * @param informacion
     */
    public void obtenerConductores(Object informacion);

    /**
     * Recupera la lista de vínculos que posee un usuario como conductor.
     * @param informacion
     */
    public void obtenerPeticionesPasajeros(Object informacion);

    /**
     * Recupera la lista de vínculos que posee un usuario como pasajero.
     * @param informacion
     */
    public void obtenerVinculosPasajero(Object informacion);

    /**
     * Recupera la lista de vehículos de los conductores que poseen un vínculo con el usuario como pasajero.
     * @param informacion
     */
    public void obtenerVehiculosVinculo(Object informacion);
}
