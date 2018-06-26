package tfg.android.fcg;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import tfg.android.fcg.modelo.IModelo;

import tfg.android.fcg.presentador.IPresentadorHistorial;
import tfg.android.fcg.presentador.IPresentadorLogin;
import tfg.android.fcg.presentador.IPresentadorMapaOrigen;
import tfg.android.fcg.presentador.IPresentadorOTGConductor;
import tfg.android.fcg.presentador.IPresentadorOTGPasajero;
import tfg.android.fcg.presentador.IPresentadorPerfil;
import tfg.android.fcg.presentador.IPresentadorPrincipal;
import tfg.android.fcg.presentador.IPresentadorRegistro;
import tfg.android.fcg.presentador.IPresentadorVehiculo;
import tfg.android.fcg.presentador.PresentadorHistorial;
import tfg.android.fcg.presentador.PresentadorLogin;
import tfg.android.fcg.presentador.PresentadorMapaOrigen;
import tfg.android.fcg.presentador.PresentadorOTGConductor;
import tfg.android.fcg.presentador.PresentadorOTGPasajero;
import tfg.android.fcg.presentador.PresentadorPerfil;
import tfg.android.fcg.presentador.PresentadorPrincipal;
import tfg.android.fcg.presentador.PresentadorRegistro;
import tfg.android.fcg.presentador.PresentadorVehiculo;
import tfg.android.fcg.vista.IVistaHistorial;
import tfg.android.fcg.vista.IVistaLogin;
import tfg.android.fcg.vista.IVistaMapaOrigen;
import tfg.android.fcg.vista.IVistaOTGConductor;
import tfg.android.fcg.vista.IVistaOTGPasajero;
import tfg.android.fcg.vista.IVistaPerfil;
import tfg.android.fcg.vista.IVistaPrincipal;
import tfg.android.fcg.vista.IVistaRegistro;
import tfg.android.fcg.vista.IVistaVehiculo;


@SuppressWarnings("rawtypes")
public class AppMediador extends Application {

	//variables correspondientes a los presentadores, vistas y modelo.

	private IVistaLogin vistaLogin;
	private IPresentadorLogin presentadorLogin;

	private IVistaRegistro vistaRegistro;
	private IPresentadorRegistro presentadorRegistro;

	private IVistaMapaOrigen vistaMapaOrigen;
	private IPresentadorMapaOrigen presentadorMapaOrigen;

	private IVistaPrincipal vistaPrincipal;
	private IPresentadorPrincipal presentadorPrincipal;

	private IVistaOTGPasajero vistaOTGPasajero;
	private IPresentadorOTGPasajero presentadorOTGPasajero;

	private IVistaOTGConductor vistaOTGConductor;
	private IPresentadorOTGConductor presentadorOTGConductor;

	private IVistaPerfil vistaPerfil;
	private IPresentadorPerfil presentadorPerfil;

	private IVistaVehiculo vistaVehiculo;
	private IPresentadorVehiculo presentadorVehiculo;

	private IVistaHistorial vistaHistorial;
	private IPresentadorHistorial presentadorHistorial;

	private static AppMediador singleton;

    private IModelo modelo;

	//Constantes de comunicacion
	public static final float ZOOM = 17f;
	public static final String GEOCODE_APIKEY = "AIzaSyCd5F6PDHPtEu_nTWWxyeoKfp3VfoWdePY";
	public static final String GEOCODE_URL = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";

	public static final String AVISO_USER_LOGIN = "tfg.android.fcg.AVISO_USER_LOGIN";
	public static final String AVISO_CAMBIO_PASSWORD = "tfg.android.fcg.AVISO_CAMBIO_PASSWORD";
	public static final String AVISO_CORREO_PASSWORD = "tfg.android.fcg.AVISO_CORREO_PASSWORD";
	public static final String AVISO_REGISTRO_USUARIO = "tfg.android.fcg.AVISO_REGISTRO_USUARIO";
	public static final String AVISO_ACTUALIZACION_USUARIO = "tfg.android.fcg.AVISO_ACTUALIZACION_USUARIO";
	public static final String AVISO_REGISTRO_VEHICULO = "tfg.android.fcg.AVISO_REGISTRO_VEHICULO";
	public static final String AVISO_REGISTRO_POSICION = "tfg.android.fcg.AVISO_REGISTRO_POSICION";
	public static final String AVISO_ACTUALIZACION_POSICION = "tfg.android.fcg.AVISO_ACTUALIZACION_POSICION";
	public static final String AVISO_ELIMINAR_USUARIO = "tfg.android.fcg.AVISO_ELIMINAR_USUARIO";
	public static final String AVISO_VEHICULOS_OTGPASAJERO = "tfg.android.fcg.AVISO_VEHICULOS_OTGPASAJERO";
	public static final String AVISO_LISTA_CONDUCTORES = "tfg.android.fcg.AVISO_LISTA_CONDUCTORES";
	public static final String AVISO_LISTA_PASAJEROS = "tfg.android.fcg.AVISO_LISTA_PASAJEROS";
	public static final String AVISO_CREACION_VINCULO = "tfg.android.fcg.AVISO_CREACION_VINCULO";
	public static final String AVISO_CONCRETAR_VINCULO = "tfg.android.fcg.AVISO_CONCRETAR_VINCULO";
	public static final String AVISO_ELIMINAR_VINCULO = "tfg.android.fcg.AVISO_ELIMINAR_VINCULO";
	public static final String AVISO_LISTA_PASAJEROS_VINCULO = "tfg.android.fcg.AVISO_LISTA_PASAJEROS_VINCULO";
	public static final String AVISO_CREACION_HISTORIAL = "tfg.android.fcg.AVISO_CREACION_HISTORIAL";
	public static final String AVISO_OBTENER_VEHICULO = "tfg.android.fcg.AVISO_OBTENER_VEHICULO";
	public static final String AVISO_ACTUALIZACION_VEHICULO = "tfg.android.fcg.AVISO_ACTUALIZACION_VEHICULO";
	public static final String AVISO_ELIMINACION_VEHICULO = "tfg.android.fcg.AVISO_ELIMINACION_VEHICULO";
	public static final String AVISO_OBTENER_CONDUCTOR = "tfg.android.fcg.AVISO_OBTENER_CONDUCTOR";
	public static final String AVISO_OBTENER_PASAJERO = "tfg.android.fcg.AVISO_OBTENER_PASAJERO";
	public static final String AVISO_HISTORIAL = "tfg.android.fcg.AVISO_HISTORIAL";
	public static final String AVISO_ELIMINAR_HISTORIAL = "tfg.android.fcg.AVISO_ELIMINAR_HISTORIAL";
	public static final String AVISO_VALORACION = "tfg.android.fcg.AVISO_VALORACION";
	public static final String AVISO_LOCALIZACION_GPS = "tfg.android.fcg.AVISO_LOCALIZACION_GPS";
	public static final String AVISO_PETICION_OTGCONDUCTOR = "tfg.android.fcg.AVISO_PETICION_OTGCONDUCTOR";
	public static final String AVISO_ACEPTAR_PETICION_OTGCONDUCTOR = "tfg.android.fcg.AVISO_ACEPTAR_PETICION_OTGCONDUCTOR";
	public static final String AVISO_RECHAZAR_PETICION_OTGCONDUCTOR = "tfg.android.fcg.AVISO_RECHAZAR_PETICION_OTGCONDUCTOR";
	public static final String AVISO_LOCALIZACION_GUARDADA = "tfg.android.fcg.AVISO_LOCALIZACION_GUARDADA";
	public static final String AVISO_RESULTADO_TRADUCIR_LOCALIZACION = "tfg.android.fcg.AVISO_RESULTADO_TRADUCIR_LOCALIZACION";
	public static final String AVISO_OBTENER_USUARIO = "tfg.android.fcg.AVISO_OBTENER_USUARIO";
	public static final String AVISO_DESLOGIN = "tfg.android.fcg.AVISO_DESLOGIN";

	public static final String CLAVE_RESULTADO_LOGIN = "tfg.android.fcg.CLAVE_RESULTADO_LOGIN";
	public static final String CLAVE_RESULTADO_CAMBIO_PASSWORD = "tfg.android.fcg.CLAVE_RESULTADO_CAMBIO_PASSWORD";
	public static final String CLAVE_RESULTADO_RECUPERAR_PASSWORD = "tfg.android.fcg.CLAVE_RESULTADO_RECUPERAR_PASSWORD";
	public static final String CLAVE_RESULTADO_REGISTRO_USUARIO = "tfg.android.fcg.CLAVE_RESULTADO_REGISTRO_USUARIO";
	public static final String CLAVE_ACTUALIZACION_USUARIO = "tfg.android.fcg.CLAVE_ACTUALIZACION_USUARIO";
	public static final String CLAVE_RESULTADO_NUEVO_VEHICULO = "tfg.android.fcg.CLAVE_RESULTADO_NUEVO_VEHICULO";
	public static final String CLAVE_RESULTADO_REGISTRO_POSICION = "tfg.android.fcg.CLAVE_RESULTADO_REGISTRO_POSICION";
	public static final String CLAVE_RESULTADO_ACTUALIZACION_POSICION = "tfg.android.fcg.CLAVE_RESULTADO_ACTUALIZACION_POSICION";
    public static final String CLAVE_RESULTADO_ELIMINAR_USUARIO = "tfg.android.fcg.CLAVE_RESULTADO_ELIMINAR_USUARIO";
    public static final String CLAVE_VEHICULOS_OTGPASAJERO = "tfg.android.fcg.CLAVE_VEHICULOS_OTGPASAJERO";
    public static final String CLAVE_LISTA_CONDUCTORES = "tfg.android.fcg.CLAVE_LISTA_CONDUCTORES";
    public static final String CLAVE_LISTA_PASAJEROS = "tfg.android.fcg.CLAVE_LISTA_PASAJEROS";
    public static final String CLAVE_CREACION_VINCULO = "tfg.android.fcg.CLAVE_CREACION_VINCULO";
    public static final String CLAVE_AVISO_PETICION_OTGCONDUCTOR = "tfg.android.fcg.CLAVE_AVISO_PETICION_OTGCONDUCTOR";
    public static final String CLAVE_ACEPTAR_PETICION_OTGCONDUCTOR = "tfg.android.fcg.CLAVE_ACEPTAR_PETICION_OTGCONDUCTOR";
    public static final String CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR = "tfg.android.fcg.CLAVE_RECHAZAR_PETICION_OTGCONDUCTOR";
    public static final String CLAVE_CONCRETAR_VINCULO = "tfg.android.fcg.CLAVE_CONCRETAR_VINCULO";
    public static final String CLAVE_ELIMINAR_VINCULO = "tfg.android.fcg.CLAVE_ELIMINAR_VINCULO";
    public static final String CLAVE_LISTA_PASAJEROS_VINCULO = "tfg.android.fcg.CLAVE_LISTA_PASAJEROS_VINCULO";
    public static final String CLAVE_CREACION_HISTORIAL = "tfg.android.fcg.CLAVE_CREACION_HISTORIAL";
    public static final String CLAVE_OBTENER_VEHICULO = "tfg.android.fcg.CLAVE_OBTENER_VEHICULO";
	public static final String CLAVE_RESULTADO_ACTUALIZACION_VEHICULO = "tfg.android.fcg.CLAVE_RESULTADO_ACTUALIZACION_VEHICULO";
	public static final String CLAVE_RESULTADO_ELIMINACION_VEHICULO = "tfg.android.fcg.CLAVE_RESULTADO_ELIMINACION_VEHICULO";
	public static final String CLAVE_OBTENER_CONDUCTOR = "tfg.android.fcg.CLAVE_OBTENER_CONDUCTOR";
	public static final String CLAVE_OBTENER_PASAJERO = "tfg.android.fcg.CLAVE_OBTENER_PASAJERO";
	public static final String CLAVE_RESULTADO_LOCALIZACION_GUARDADA = "tfg.android.fcg.CLAVE_RESULTADO_LOCALIZACION_GUARDADA";
	public static final String CLAVE_HISTORIAL = "tfg.android.fcg.CLAVE_HISTORIAL";
	public static final String CLAVE_VALORACION = "tfg.android.fcg.CLAVE_VALORACION";
	public static final String CLAVE_RESULTADO_ELIMINAR_HISTORIAL = "tfg.android.fcg.CLAVE_RESULTADO_ELIMINAR_HISTORIAL";
	public static final String CLAVE_LATITUD = "tfg.android.fcg.CLAVE_LATITUD";
	public static final String CLAVE_LONGITUD = "tfg.android.fcg.CLAVE_LONGITUD";
	public static final String CLAVE_RESULTADO_TRADUCIR_LOCALIZACION = "tfg.android.fcg.CLAVE_RESULTADO_TRADUCIR_LOCALIZACION";
	public static final String CLAVE_OBTENER_USUARIO = "tfg.android.fcg.CLAVE_OBTENER_USUARIO";
	public static final String CLAVE_DESLOGIN = "tfg.android.fcg.CLAVE_DESLOGIN";

	public static final String ERROR_ACTUALIZACION_USUARIO_CORREO = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_CORREO";
	public static final String ERROR_ACTUALIZACION_USUARIO_PASSWORD ="tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_PASSWORD";
	public static final String ERROR_ACTUALIZACION_USUARIO_NOMBRE = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_NOMBRE";
	public static final String ERROR_ACTUALIZACION_USUARIO_INFO = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_INFO";
	public static final String ERROR_ACTUALIZACION_USUARIO_REAUTENTICACION = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_REAUTENTICACION";
	public static final String ERROR_ACTUALIZACION_USUARIO_ORIGENYDESTINO = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_ORIGENYDESTINO";
	public static final String ERROR_ACTUALIZACION_USUARIO_FECHAYHORA = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_FECHAYHORA";
	public static final String ERROR_ACTUALIZACION_USUARIO_DATOVEHICULO = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_DATOVEHICULO";
	public static final String ERROR_ACTUALIZACION_USUARIO_VALORACION = "tfg.android.fcg.ERROR_ACTUALIZACION_USUARIO_VALORACION";
	//Métodos accesor de los presentadores, vistas y modelo.

	public static AppMediador getInstance(){
		return singleton;
	}

	//***************PRESENTADORES****************//

	public IPresentadorLogin getPresentadorLogin() {
		if(presentadorLogin == null)
			presentadorLogin = new PresentadorLogin();
		return presentadorLogin;
	}

	public void removePresentadorLogin(){
		presentadorLogin = null;
	}

	public IPresentadorRegistro getPresentadorRegistro() {
		if(presentadorRegistro == null)
			presentadorRegistro = new PresentadorRegistro();
		return presentadorRegistro;
	}

	public void removePresentadorRegistro(){
		presentadorRegistro = null;
	}

	public IPresentadorMapaOrigen getPresentadorMapaOrigen() {
		if(presentadorMapaOrigen == null)
			presentadorMapaOrigen = new PresentadorMapaOrigen();
		return presentadorMapaOrigen;
	}

	public void removePresentadorMapaOrigen(){
		presentadorMapaOrigen = null;
	}

	public IPresentadorPrincipal getPresentadorPrincipal() {
		if(presentadorPrincipal == null)
			presentadorPrincipal = new PresentadorPrincipal();
		return presentadorPrincipal;
	}

	public void removePresentadorPrincipal(){
		presentadorPrincipal = null;
	}

	public IPresentadorOTGPasajero getPresentadorOTGPasajero() {
		if(presentadorOTGPasajero == null)
			presentadorOTGPasajero = new PresentadorOTGPasajero();
		return presentadorOTGPasajero;
	}

	public void removePresentadorOTGPasajero(){
		presentadorOTGPasajero = null;
	}

	public IPresentadorOTGConductor getPresentadorOTGConductor() {
		if(presentadorOTGConductor == null)
			presentadorOTGConductor = new PresentadorOTGConductor();
		return presentadorOTGConductor;
	}

	public void removePresentadorOTGConductor(){
		presentadorOTGConductor = null;
	}

	public IPresentadorPerfil getPresentadorPerfil() {
		if(presentadorPerfil == null)
			presentadorPerfil = new PresentadorPerfil();
		return presentadorPerfil;
	}

	public void removePresentadorPerfil(){
		presentadorPerfil = null;
	}

	public IPresentadorVehiculo getPresentadorVehiculo() {
		if(presentadorVehiculo == null)
			presentadorVehiculo = new PresentadorVehiculo();
		return presentadorVehiculo;
	}

	public void removePresentadorVehiculo(){
		presentadorVehiculo = null;
	}

	public IPresentadorHistorial getPresentadorHistorial() {
		if(presentadorHistorial == null)
			presentadorHistorial = new PresentadorHistorial();
		return presentadorHistorial;
	}

	public void removePresentadorHistorial(){
		presentadorHistorial = null;
	}

	//***************MODELO******************//
    public IModelo getModelo() {
        return modelo;
    }

    public void setModelo(IModelo modelo) {
        this.modelo = modelo;
    }


	//****************VISTAS*****************//

	public IVistaLogin getVistaLogin() {
		return vistaLogin;
	}

	public void setVistaLogin(IVistaLogin vistaLogin){
		this.vistaLogin = vistaLogin;
	}

	public IVistaRegistro getVistaRegistro() {
		return vistaRegistro;
	}

	public void setVistaRegistro(IVistaRegistro vistaRegistro){
		this.vistaRegistro = vistaRegistro;
	}

	public IVistaMapaOrigen getVistaMapaOrigen() {
		return vistaMapaOrigen;
	}

	public void setVistaMapaOrigen(IVistaMapaOrigen vistaMapaOrigen){
		this.vistaMapaOrigen = vistaMapaOrigen;
	}

	public IVistaPrincipal getVistaPrincipal() {
		return vistaPrincipal;
	}

	public void setVistaPrincipal(IVistaPrincipal vistaPrincipal){
		this.vistaPrincipal = vistaPrincipal;
	}

	public IVistaOTGPasajero getVistaOTGPasajero() {
		return vistaOTGPasajero;
	}

	public void setVistaOTGPasajero(IVistaOTGPasajero vistaOTGPasajero){
		this.vistaOTGPasajero = vistaOTGPasajero;
	}

	public IVistaOTGConductor getVistaOTGConductor() {
		return vistaOTGConductor;
	}

	public void setVistaOTGConductor(IVistaOTGConductor vistaOTGConductor){
		this.vistaOTGConductor = vistaOTGConductor;
	}

	public IVistaPerfil getVistaPerfil() {
		return vistaPerfil;
	}

	public void setVistaPerfil(IVistaPerfil vistaPerfil){
		this.vistaPerfil = vistaPerfil;
	}

	public IVistaVehiculo getVistaVehiculo() {
		return vistaVehiculo;
	}

	public void setVistaVehiculo(IVistaVehiculo vistaVehiculo){
		this.vistaVehiculo = vistaVehiculo;
	}

	public IVistaHistorial getVistaHistorial() {
		return vistaHistorial;
	}

	public void setVistaHistorial(IVistaHistorial vistaHistorial){
		this.vistaHistorial = vistaHistorial;
	}

	// Metodos que manejan componentes de Android

	public void launchActivity(Class toActivity, Object fromComponent, Bundle extras) {
		Intent i = new Intent(this, toActivity);
		if (extras != null)
			i.putExtras(extras);	
		if (!fromComponent.getClass().equals(Activity.class))
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	public void launchActivity(Intent intent) {
		startActivity(intent);
	}
	
	public void launchActivityForResult(Class toActivity,
			Activity fromActivity, int requestCode, Bundle extras) {
		Intent i = new Intent(fromActivity, toActivity);
		if (extras != null)
			i.putExtras(extras);
		fromActivity.startActivityForResult(i, requestCode);
	}
	
	public void launchService(Class toService, Bundle extras) {
		Intent i = new Intent(this, toService);
		if (extras != null)
			i.putExtras(extras);
        startService(i);
	}
	
	public void stopService(Class toService) {
		Intent i = new Intent(this, toService);
        stopService(i);
	}
	
	public void registerReceiver(BroadcastReceiver receiver, String action) {
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(action));
	}	
	
	public void unRegisterReceiver(BroadcastReceiver receiver) {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
	}
	
	public void sendBroadcast(String action, Bundle extras) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (extras != null)
			intent.putExtras(extras);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		presentadorLogin = null;
		presentadorRegistro = null;
		presentadorMapaOrigen = null;
		presentadorPrincipal = null;
		presentadorOTGPasajero = null;
		presentadorOTGConductor = null;
		presentadorPerfil = null;
		presentadorVehiculo = null;
		presentadorHistorial = null;

		singleton = this;
	}
}
