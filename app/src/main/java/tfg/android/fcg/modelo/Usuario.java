package tfg.android.fcg.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {

    //Variables globales

    private String idUser;
    private String nombre;
    private String valoracion;
    private String origen;
    private String destino;
    private String fecha;
    private String hora;
    private Object datoVehiculo;

    //Constructor

    public Usuario (String idUser, String nombre, String valoracion, String origen, String destino, String fecha, String hora, Object datoVehiculo){
        this.idUser = idUser;
        this.nombre = nombre;
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

    public void setDatoVehiculo(Object datoVehiculo) {
        this.datoVehiculo = datoVehiculo;
    }
}
