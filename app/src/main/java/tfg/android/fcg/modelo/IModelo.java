package tfg.android.fcg.modelo;

public interface IModelo {

    /**
     * Comprueba la validez de los datos de identificación
     * al entrar en la aplicación.
     * @param informacion contendra:
     */
    public void comprobarLogin(Object[] informacion);


    /**
     * Recupera el password relacionado
     * con los datos pasados por parámetros.
     * @param informacion contendra:
     */
    public void recuperarPassword(Object informacion);

    /**
     * Modifica el password relacionado
     * con los datos pasados por parámetros.
     * @param informacion contendra:
     */
    public void cambiarPassword(Object[] informacion);

    /**
     * Añdade un nuevo usuario a la aplicación.
     * @param informacion contendra:
     */
    public void registrarUsuario(Object[] informacion);

    /**
     * Obtiene un mapa centrado en la ubicación del usuario
     * usando el servidor de Google.
     * @param informacion contendra:
     */
    public void obtenerMapa(Object informacion);

    /**
     * Obtiene la posición del usuario a través del GPS.

     */
    public void obtenerPosicionUsuario();

    /**
     * Almacena la localización origen que ha seleccionado
     * un usuario determinado y el destino del mismo.
     * @param informacion contendra:
     */
    public void guardarOrigenYDestino(Object[] informacion);

    /**
     * Recupera la lista de pasajeros o conductores de la base de datos.
     * @param informacion contendra:
     */
    public void obtenerUsuariosPickUp(Object[] informacion);

    /**
     * Almacena la información de un usuario
     * seleccionado en la opción PickUp.
     * @param informacion contendra:
     */
    public void guardarUsuarioPickup(Object[] informacion);

    /**
     * Elimina de la base de datos la información de un usuario
     * seleccionado en la opción PickUp.
     * @param informacion contendra:
     */
    public void eliminarUsuarioPickup(Object[] informacion);

    /**
     * Recupera la lista de vehículos en la opción On The Go.
     * @param informacion contendra:
     */
    public void buscarVehiculos(Object informacion);

    /**
     * Recupera la información de un vehículo en la opción On The Go.
     * @param informacion contendra:
     */
    public void seleccionarVehiculoOnTheGo(Object informacion);

    /**
     * Almacena en la base de datos la información de un usuario.
     * @param informacion contendra:
     */
    public void guardarPerfil(Object[] informacion);

    /**
     * Elimina de la base de datos la información de un usuario.
     * @param informacion contendra:
     */
    public void eliminarPerfil(Object informacion);

    /**
     * Almacena en la base de datos la información del vehículo de un usuario.
     * @param informacion contendra:
     */
    public void guardarVehiculo(Object[] informacion);

    /**
     * Elimina de la base de datos la información del vehículo de un usuario.
     * @param informacion contendra:
     */
    public void eliminarVehiculo(Object informacion);

    /**
     * Recupera de la base de datos la información
     * de los viajes realizados por un usuario.
     * @param informacion contendra:
     */
    public void obtenerHistorial(Object informacion);

    /**
     * Almacena en la base de datos una valoración a un usuario determinado.
     * @param informacion contendra:
     */
    public void asignarValoracion(Object[] informacion);

    /**
     * Almacena en la base de datos la localización de un usuario.
     * @param informacion contendra:
     */
    public void guardarLocalizacion(Object[] informacion);

    /**
     * Recupera de la base de datos la información sobre los pasajeros
     * que quieren viajar con un conductor dado en la opción On the Go.
     * @param informacion contendra:
     */
    public void obtenerPeticionesDePasajeros(Object informacion);

    /**
     * Almacena la información del pasajero aceptado para viajar con un conductor.
     * @param informacion contendra:
     */
    public void aceptarPasajero(Object informacion);

    /**
     * Almacena en la base de datos la información del pasajero
     * que se ha rechazado para viajar con un conductor dado en la opción On the go.
     * @param informacion contendra:
     */
    public void rechazarPasajero(Object informacion);

    /**
     * Detiene el proceso de búsqueda de la localización del usuario
     * y almacenamiento de la misma en la opción On the Go.
     * @param informacion contendra:
     */
    public void pararRuta(Object informacion);
}
