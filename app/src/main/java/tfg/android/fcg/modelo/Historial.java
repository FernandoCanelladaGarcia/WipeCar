package tfg.android.fcg.modelo;

public class Historial {

    //Variables globales

    private String idPasajero;
    private String idConductor;
    private String fecha;
    private String hora;
    private String origen;
    private String destino;
    private String valoracionPasajero;
    private String valoracionConductor;

    //Constructor

    public Historial(String idPasajero, String idConductor, String fecha, String hora, String origen,
                     String destino, String valoracionPasajero, String valoracionConductor) {
        this.idPasajero = idPasajero;
        this.idConductor = idConductor;
        this.fecha = fecha;
        this.hora = hora;
        this.origen = origen;
        this.destino = destino;
        this.valoracionPasajero = valoracionPasajero;
        this.valoracionConductor = valoracionConductor;
    }

    public Historial(){

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

    public String getValoracionPasajero() {
        return valoracionPasajero;
    }

    public void setValoracionPasajero(String valoracionPasajero) {
        this.valoracionPasajero = valoracionPasajero;
    }

    public String getValoracionConductor() {
        return valoracionConductor;
    }

    public void setValoracionConductor(String valoracionConductor) {
        this.valoracionConductor = valoracionConductor;
    }
}
