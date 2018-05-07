package tfg.android.fcg.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    //Variables globales

    private String idUser;
    private String nombre;
    private String telefono;
    private boolean rol;
    private String valoracion;
    private String origen;
    private String destino;
    private String fecha;
    private String hora;
    private String datoVehiculo;

    //Constructor

    public Usuario (String idUser, String nombre, String telefono, boolean rol, String valoracion, String origen, String destino, String fecha, String hora, String datoVehiculo){
        this.idUser = idUser;
        this.nombre = nombre;
        this.telefono = telefono;
        this.rol = rol;
        this.valoracion = valoracion;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.hora = hora;
        this.datoVehiculo = datoVehiculo;
    }

    public Usuario(){

    }

    //Getters & Setters

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isRol() {
        return rol;
    }

    public void setRol(boolean rol) {
        this.rol = rol;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Object getDatoVehiculo() {
        return datoVehiculo;
    }

    public void setDatoVehiculo(String datoVehiculo) {
        this.datoVehiculo = datoVehiculo;
    }
}
