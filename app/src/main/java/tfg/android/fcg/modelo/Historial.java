package tfg.android.fcg.modelo;

public class Historial {

    //Variables globales

    private String idPasajero;
    private String idConductor;
    private String fecha;
    private String hora;
    private String origen;
    private String destino;
    //TODO: NO NECESARIOS, REDACCION
    //private String valoracionPasajero;
    //private String valoracionConductor;
    //TODO: NUEVOS VALORES, REDACCION
    private String nombreConductor;
    private String nombrePasajero;

    //Constructor


    public Historial(){

    }

    public Historial(String idPasajero, String idConductor, String fecha, String hora, String origen,
                     String destino, String nombreConductor, String nombrePasajero) {
        this.idPasajero = idPasajero;
        this.idConductor = idConductor;
        this.fecha = fecha;
        this.hora = hora;
        this.origen = origen;
        this.destino = destino;
        this.nombreConductor = nombreConductor;
        this.nombrePasajero = nombrePasajero;
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

    public String getNombreConductor() {
        return nombreConductor;
    }

    public void setNombreConductor(String nombreConductor) {
        this.nombreConductor = nombreConductor;
    }

    public String getNombrePasajero() {
        return nombrePasajero;
    }

    public void setNombrePasajero(String nombrePasajero) {
        this.nombrePasajero = nombrePasajero;
    }
}
