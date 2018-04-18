package tfg.android.fcg.presentador;

public interface IPresentadorRegistro {

    public void tratarRegistro(Object informacion);
    public void tratarOk(Object informacion);
    public void tratarAceptarRegistro(Object informacion);
    public void tratarCancelar(Object informacion);

}
