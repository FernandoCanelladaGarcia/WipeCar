package tfg.android.fcg.presentador;

public interface IPresentadorLogin {

    public void tratarLogin(Object data);
    public void tratarOk();
    public void tratarNuevo();
    public void tratarRecuperarPassword();
    public void tratarCambiarPassword(Object data);
}
