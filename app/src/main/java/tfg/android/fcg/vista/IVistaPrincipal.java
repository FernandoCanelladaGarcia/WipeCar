package tfg.android.fcg.vista;

import tfg.android.fcg.modelo.Usuario;

public interface IVistaPrincipal {

    /**
     * Muestra una barra de progreso. Existe una acción en background.
     */
    public void mostrarProgreso();

    /**
     * Elimina la barra de progreso. Acción en background finalizada.
     */
    public void cerrarProgreso();

    /**
     * Muestra un diálogo al usuario para realizar una determinada acción.
     * @param informacion contendra:
     */
    public void mostrarDialogo(Object informacion);

    /**
     * Cierra el diálogo mostrado al usuario tras su uso útil.
     */
    public void cerrarDialogo();

    /**
     * Obtiene los datos del usuario de la aplicación de la base de datos
     * @param informacion contiene
     */
    public void setUsuario(Object informacion);

    /**
     * Obtiene los datos de los conductores que se dirigen al mismo destino que el usuario de la base de datos.
     * @param informacion contiene
     */
    public void setConductores(Object informacion);

    /**
     * Obtiene los vehículos de los conductores recogidos de la base de datos.
     * @param informacion contiene
     */
    public void setVehiculos(Object informacion);

    /**
     * Obtiene los pasajeros que han escogido a un conductor.
     * @param informacion
     */
    public void setPasajeros(Object informacion);

    /**
     * Obtiene los objetos Usuario que poseen un vínculo con el usuario la base de datos.
     * @param informacion
     */
    public void setVinculos(Object informacion);

    /**
     * Obtiene los objetos Vinculo a tratar por un pasajero de la base de datos.
     * @param informacion
     */
    public void setListaVinculos(Object informacion);

    /**
     * Obtiene los objetos Vehiculo de los usuarios recogidos de la base de datos.
     * @param informacion
     */
    public void setVehiculosVinculo(Object informacion);

    /**
     * Metodo que devuelve el usuario logeado.
     * @return usuario logeado
     */
    public Usuario getUsuario();

    /**
     * Refresca el contenido de la pantalla.
     */
    public void refrescarContenido();
}
