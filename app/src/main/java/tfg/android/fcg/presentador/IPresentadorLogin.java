package tfg.android.fcg.presentador;

public interface IPresentadorLogin {

    public void tratarLogin(Object informacion);
    public void tratarOk(Object informacion);
    public void tratarRecuperarPassword(Object informacion);
    public void tratarCambiarPassword(Object informacion);
    public void tratarNuevo(Object informacion);
}
