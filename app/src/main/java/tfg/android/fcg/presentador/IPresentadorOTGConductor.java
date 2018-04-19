package tfg.android.fcg.presentador;

public interface IPresentadorOTGConductor {

    public void iniciar(Object informacion);
    public void tratarAceptar(Object informacion);
    public void tratarRechazar(Object informacion);
    public void tratarParar(Object informacion);
}
