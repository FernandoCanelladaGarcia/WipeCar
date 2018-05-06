package tfg.android.fcg.modelo;

import java.io.Serializable;

public class Vehiculo implements Serializable {

    //Variables globales

    private String datoVehiculo;
    private String marca;
    private String modelo;
    private String matricula;

    //Constructor

    public Vehiculo(String datoVehiculo, String marca, String modelo, String matricula) {
        this.datoVehiculo = datoVehiculo;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
    }

    //Getters & Setters
    public String getDatoVehiculo() {
        return datoVehiculo;
    }

    public void setDatoVehiculo(String datoVehiculo) {
        this.datoVehiculo = datoVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
