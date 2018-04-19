package tfg.android.fcg.presentador;

public interface IPresentadorMapaOrigen {

    public void iniciar(Object informacion);
    public void tratarOrigen(Object informacion);
    public void tratarSeleccionarOrigen(Object informacion);
    public void tratarSalirMapa(Object informacion);
    public void tratarOrigenYDestino(Object informacion);
}
