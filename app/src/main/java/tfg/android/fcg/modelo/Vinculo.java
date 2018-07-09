package tfg.android.fcg.modelo;

import java.io.Serializable;

public class Vinculo implements Serializable {

    //Variables Globales
    private String idPasajero;
    private String idConductor;
    private boolean vinculo;
    private String fecha;
    private String hora;
    private String origen;
    private String destino;

    //Constructor
    public Vinculo(){

    }

    public Vinculo(String idPasajero, String idConductor, boolean vinculo,
                   String fecha, String hora, String origen, String destino) {
        this.idPasajero = idPasajero;
        this.idConductor = idConductor;
        this.vinculo = vinculo;
        this.fecha = fecha;
        this.hora = hora;
        this.origen = origen;
        this.destino = destino;
    }

    //Getters & Setters
    public String getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(String idPasajero) {
        this.idPasajero = idPasajero;
    }

    public String getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(String idConductor) {
        this.idConductor = idConductor;
    }

    public boolean isVinculo() {
        return vinculo;
    }

    public void setVinculo(boolean vinculo) {
        this.vinculo = vinculo;
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
}
