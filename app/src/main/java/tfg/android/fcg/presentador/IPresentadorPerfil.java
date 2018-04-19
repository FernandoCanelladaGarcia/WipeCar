package tfg.android.fcg.presentador;

public interface IPresentadorPerfil {

    public void tratarGuardar(Object informacion);
    public void tratarOk(Object informacion);
    public void tratarGuardarPerfil(Object informacion);
    public void tratarCancelar(Object informacion);
    public void tratarEditar(Object informacion);
    public void tratarHistorial(Object informacion);
    public void tratarAyuda(Object informacion);
    public void tratarPapelera(Object informacion);
    public void tratarEliminarPerfil(Object informacion);
    public void tratarConductor(Object informacion);

}
