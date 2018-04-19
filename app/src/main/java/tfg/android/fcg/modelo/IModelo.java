package tfg.android.fcg.modelo;

public interface IModelo {

    public void comprobarLogin(Object informacion);
    public void recuperarPassword(Object informacion);
    public void cambiarPassword(Object informacion);
    public void registrarUsuario(Object informacion);
    public void obtenerMapa(Object informacion);
    public void obtenerPosicionUsuario(Object informacion);
    public void guardarOrigenYDestino(Object informacion);
    public void obtenerUsuariosPickUp(Object informacion);
    public void guardarUsuarioPickup(Object informacion);
    public void eliminarUsuarioPickup(Object informacion);
    public void buscarVehiculos(Object informacion);
    public void seleccionarVehiculoOnTheGo(Object informacion);
    public void guardarPerfil(Object informacion);
    public void eliminarPerfil(Object informacion);
    public void guardarVehiculo(Object informacion);
    public void eliminarVehiculo(Object informacion);
    public void obtenerHistorial(Object informacion);
    public void asignarValoracion(Object informacion);
    public void guardarLocalizacion(Object informacion);
    public void obtenerPeticionesDePasajeros(Object informacion);
    public void aceptarPasajero(Object informacion);
    public void rechazarPasajero(Object informacion);
    public void pararRuta(Object informacion);
}
