package tfg.android.fcg.modelo;

import java.io.Serializable;

public class Posicion implements Serializable {

    //Variables Globales

    private String idUser;
    private String latitud;
    private String longitud;

    //Constructor
    public Posicion(){

    }

    public Posicion(String idUser, String latitud, String longitud){
        this.idUser = idUser;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    //Getters & Setters

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
