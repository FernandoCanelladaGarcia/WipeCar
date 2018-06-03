package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.presentador.IPresentadorPerfil;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaPerfil extends AppCompatActivity implements IVistaPerfil, View.OnClickListener {

    private Button botonOrigen, botonHistorial;
    private FloatingActionButton botonEditar;
    private TextView nombre,telefono,email,password,marca,modelo,matricula;
    private Switch modoConductor;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private Usuario user;
    private Vehiculo vehiculo;
    private SharedPreferences sharedPreferences;
    private IPresentadorPerfil presentadorPerfil;

    private final static String TAG = "depurador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_perfil);
        appMediador = AppMediador.getInstance();
        appMediador.setVistaPerfil(this);
        presentadorPerfil = appMediador.getPresentadorPerfil();
        mostrarProgreso();

        botonEditar = (FloatingActionButton) findViewById(R.id.editButton);
        botonHistorial = (Button) findViewById(R.id.historialButton);
        botonOrigen = (Button) findViewById(R.id.origenButton);
        modoConductor = (Switch) findViewById(R.id.modoConductor);

        nombre = (TextView) findViewById(R.id.nombreTitle);
        telefono = (TextView) findViewById(R.id.telefonoTitle);
        email = (TextView) findViewById(R.id.emailTitle2);
        password = (TextView) findViewById(R.id.passwordTitle);
        marca = (TextView) findViewById(R.id.marcaTitle);
        modelo = (TextView) findViewById(R.id.modeloTitle);
        matricula = (TextView) findViewById(R.id.matriculaTitle);

        sharedPreferences = appMediador.getSharedPreferences("Login",0);
        boolean rol = sharedPreferences.getBoolean("rol",false);
        modoConductor.setChecked(rol);
        modoConductor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(user.getDatoVehiculo() != null){
                    modoConductor.setChecked(isChecked);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("rol",isChecked);
                    editor.apply();
                    if(isChecked){
                        Toast.makeText(getApplicationContext(), "MODO CONDUCTOR", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "MODO PASAJERO", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    mostrarDialogo(0);
                }
            }
        });
        email.setText(sharedPreferences.getString("email",null));
        password.setText(sharedPreferences.getString("password",null));
        String idUser = sharedPreferences.getString("idUser",null);
        presentadorPerfil.iniciar(idUser);
        Log.i(TAG, "Vista Perfil");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editButton:
                break;
            case R.id.historialButton:
                break;
            case R.id.origenButton:
                break;
        }
    }

    @Override
    public void mostrarProgreso() {
        Log.i(TAG,"mostrar Progreso");
        dialogoProgreso = new ProgressDialog(this);
        dialogoProgreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogoProgreso.setIndeterminate(true);
        dialogoProgreso.setCancelable(false);
        dialogoProgreso.setTitle("Espere por favor");
        dialogoProgreso.setMessage("Cargando...");
        dialogoProgreso.show();
    }

    @Override
    public void cerrarProgreso() {
        Log.i(TAG,"cerrar Progreso");
        dialogoProgreso.dismiss();
    }

    @Override
    public void mostrarDialogo(Object informacion) {
        int tarea = (int) informacion;
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        switch (tarea){
            case 0:
                dialogBuild.setTitle("Modo Conductor");
                dialogBuild.setMessage("Es la primera vez que cambia de modo. " +
                        "Va a pasar a registrar los datos de su vehiculo");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presentadorPerfil.tratarOk(0);
                        modoConductor.setChecked(true);
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modoConductor.setChecked(false);
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
        }
    }

    @Override
    public void cerrarDialogo() {
        dialogo.cancel();
    }

    @Override
    public void prepararEdicion(Object informacion) {

    }

    @Override
    public void prepararVista(Object informacion) {
        Object[] info = (Object[])informacion;
        user = (Usuario)info[0];
        if(info[1] != ""){
            vehiculo = (Vehiculo)info[1];
            modelo.setText(vehiculo.getModelo());
            marca.setText(vehiculo.getMarca());
            matricula.setText(vehiculo.getMatricula());
        }
        nombre.setText(user.getNombre());
        telefono.setText(user.getTelefono());
        cerrarProgreso();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appMediador.removePresentadorPerfil();
    }
}
