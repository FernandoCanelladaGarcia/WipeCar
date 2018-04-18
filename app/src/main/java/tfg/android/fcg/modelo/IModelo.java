package tfg.android.fcg.modelo;

public interface IModelo {

    public void comprobarLogin();
    public void recuperarPassword();
    public void cambiarPassword();
    public void registrarUsuario();
    public void obtenerMapa();
    public void obtenerPosicionUsuario();
    public void guardarOrigenYDestino();
    public void obtenerUsuariosPickUp();
    public void guardarUsuarioPickup();
    public void eliminarUsuarioPickup();
    public void buscarVehiculos();
    public void seleccionarVehiculosOnTheGo();
    public void guardarPerfil();
    public void eliminarPerfil();
    public void guardarVehiculo();
    public void eliminarVehiculo();
    public void obtenerHistorial();
    public void asignarValoracion();
    public void guardarLocalizacion();
    public void obtenerPeticionesDePasajeros();
    public void aceptarPasajero();
    public void rechazarPasajero();
    public void pararRuta();
}
