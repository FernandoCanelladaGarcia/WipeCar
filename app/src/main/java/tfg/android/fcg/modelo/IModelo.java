package tfg.android.fcg.modelo;

public interface IModelo {

    /**
     * Comprueba la validez de los datos de identificacion
     * al entrar en la aplicacion
     * @param informacion contendra:
     */
    public void comprobarLogin(Object informacion);


    /**
     * Recupera el password relacionado
     * con los datos pasados por parametros
     * @param informacion contendra:
     */
    public void recuperarPassword(Object informacion);

    /**
     * Modifica el password relacionado
     * con los datos pasados por parametros
     * @param informacion contendra:
     */
    public void cambiarPassword(Object informacion);

    /**
     * Añdade un nuevo usuario a la aplicacion
     * @param informacion contendra:
     */
    public void registrarUsuario(Object informacion);

    /**
     * Obtiene un mapa centrado en la ubicacion del usuario
     * usando el servidor de Google
     * @param informacion contendra:
     */
    public void obtenerMapa(Object informacion);

    /**
     * Obtiene la posicion del usuario a traves del GPS
     * @param informacion contendra:
     */
    public void obtenerPosicionUsuario(Object informacion);

    /**
     * Almacena la localizacion origen que ha seleccionado
     * un usuario determinado y el destino del mismo
     * @param informacion contendra:
     */
    public void guardarOrigenYDestino(Object informacion);

    /**
     * Recupera la lista de pasajero o conductores de la base de datos.
     * @param informacion contendra:
     */
    public void obtenerUsuariosPickUp(Object informacion);

    /**
     * Almacena la informacion de un usuario
     * seleccionado en la opcion PickUp
     * @param informacion contendra:
     */
    public void guardarUsuarioPickup(Object informacion);

    /**
     * Elimina de la base de datos la informacion de un usuario
     * seleccionado en la opcion PickUp
     * @param informacion contendra:
     */
    public void eliminarUsuarioPickup(Object informacion);

    /**
     * Recupera la lista de vehiculos en la opcion On The Go
     * @param informacion contendra:
     */
    public void buscarVehiculos(Object informacion);

    /**
     * Recupera la informacion de un vehiculo en la opcion On The Go
     * @param informacion contendra:
     */
    public void seleccionarVehiculoOnTheGo(Object informacion);

    /**
     * Almacena en la base de datos la informacion de un usuario.
     * @param informacion contendra:
     */
    public void guardarPerfil(Object informacion);

    /**
     * Elimina de la base de datos la informacion de un usuario.
     * @param informacion contendra:
     */
    public void eliminarPerfil(Object informacion);

    /**
     * Almacena en la base de datos la informacion del vehiculo de un usuario
     * @param informacion contendra:
     */
    public void guardarVehiculo(Object informacion);

    /**
     * Elimina de la base de datos la informacion del vehiculo de un usuario.
     * @param informacion contendra:
     */
    public void eliminarVehiculo(Object informacion);

    /**
     * Recupera de la base de datos la informacion
     * de los viajes realizados por un usuario
     * @param informacion contendra:
     */
    public void obtenerHistorial(Object informacion);

    /**
     * Almacena en la base de datos una valoracion a un usuario determinado
     * @param informacion contendra:
     */
    public void asignarValoracion(Object informacion);

    /**
     * Almacena en la base de datos la localizacion de un usuario
     * @param informacion contendra:
     */
    public void guardarLocalizacion(Object informacion);

    /**
     * Recupera de la base de datos la informacion sobre los pasajeros
     * que quieren viajar con un conductor dado en la opcion On the Go
     * @param informacion contendra:
     */
    public void obtenerPeticionesDePasajeros(Object informacion);

    /**
     * Almacena la informacion del pasajero aceptado para viajar con un conductor
     * @param informacion contendra:
     */
    public void aceptarPasajero(Object informacion);

    /**
     * Almacena en la base de datos la informacion del pasajero
     * que se ha rechazado para viajar con un conductor dado en la opcion On the go.
     * @param informacion contendra:
     */
    public void rechazarPasajero(Object informacion);

    /**
     * Detiene el proceso de busqueda de la localizacion del usuario
     * y almacenamiento de la misma en la opcion On the Go
     * @param informacion contendra:
     */
    public void pararRuta(Object informacion);
}
