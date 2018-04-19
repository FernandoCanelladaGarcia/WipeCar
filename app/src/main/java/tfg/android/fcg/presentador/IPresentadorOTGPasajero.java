package tfg.android.fcg.presentador;

public interface IPresentadorOTGPasajero {

    public void iniciar(Object informacion);
    public void tratarBuscar(Object informacion);
    public void tratarVehiculo(Object informacion);
    public void tratarOk(Object informacion);
    public void tratarCancelar(Object informacion);
}
