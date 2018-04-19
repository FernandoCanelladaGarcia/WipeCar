package tfg.android.fcg.presentador;

public interface IPresentadorVehiculo {

    public void tratarGuardar(Object informacion);
    public void tratarGuardarVehiculo(Object informacion);
    public void tratarCancelar(Object informacion);
    public void tratarEditar(Object informacion);
    public void tratarPapelera(Object informacion);
    public void tratarEliminarVehiculo(Object informacion);
}
