package tfg.android.fcg.presentador;

public interface IPresentadorPrincipal {

    public void iniciar(Object informacion);
    public void tratarSeleccion(Object informacion);
    public void tratarOk(Object informacion);
    public void tratarCancelar(Object informacion);
    public void tratarChat(Object informacion);
    public void tratarMapa(Object informacion);
    public void tratarBorrarSeleccion(Object informacion);
    public void tratarConfiguracion(Object informacion);
    public void tratarOnTheGo(Object informacion);
}
