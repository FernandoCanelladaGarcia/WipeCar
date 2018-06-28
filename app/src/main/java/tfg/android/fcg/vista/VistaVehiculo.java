package tfg.android.fcg.vista;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.presentador.IPresentadorVehiculo;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaVehiculo extends AppCompatActivity implements IVistaVehiculo, View.OnClickListener{

    private Button registroVehiculo;
    private EditText marca, modelo, matricula, email, password;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private Vehiculo vehiculoUsuario;
    private AppMediador appMediador;
    private IPresentadorVehiculo presentadorVehiculo;
    private final static String TAG = "depurador";
    private String emailActual, passwordActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_vehiculo);
        appMediador = AppMediador.getInstance();
        appMediador.setVistaVehiculo(this);
        presentadorVehiculo = appMediador.getPresentadorVehiculo();

        registroVehiculo = findViewById(R.id.regisHistorialButton);
        marca = (EditText) findViewById(R.id.marcaRegistro);
        modelo = (EditText) findViewById(R.id.modeloRegistro);
        matricula = (EditText) findViewById(R.id.matriculaRegistro);
        email = (EditText) findViewById(R.id.emailVehiculo);
        password = (EditText) findViewById(R.id.passwordVehiculo);

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login",0);
        emailActual = sharedPreferences.getString("email",null);
        passwordActual = sharedPreferences.getString("password",null);
        Log.i(TAG, "Vista Vehiculo" + " email: "+ emailActual + " password: " + passwordActual);
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
        int tarea = (int)informacion;
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        switch (tarea){
            case 0:
                dialogBuild.setTitle("Registro vehiculo");
                dialogBuild.setMessage("¿Esta seguro?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object[] informacion = new Object[5];
                        informacion[0] = marca.getText().toString();
                        informacion[1] = modelo.getText().toString();
                        informacion[2] = matricula.getText().toString();
                        informacion[4] = 0;
                        presentadorVehiculo.tratarGuardarVehiculo(informacion);
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;

        }
    }

    @Override
    public void cerrarDialogo() {

    }

    @Override
    public void prepararEdicion(Object informacion) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.regisHistorialButton:
                if(checkEditText()){
                    presentadorVehiculo.tratarGuardar(0);
                }
                break;
        }
    }

    private boolean checkEditText(){
        if(marca.getText().toString().equals("") || modelo.getText().toString().equals("") ||
                matricula.getText().toString().equals("") || email.getText().toString().equals("") || password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Rellene los datos por favor", Toast.LENGTH_SHORT).show();
            return false;
        }else if(!email.getText().toString().equals(emailActual)) {
            Toast.makeText(getApplicationContext(), "Email no correcto", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!password.getText().toString().equals(passwordActual)){
            Toast.makeText(getApplicationContext(), "Password no correcto", Toast.LENGTH_SHORT).show();
            return false;
        }else if(matricula.getText().toString().length() < 7){
            Toast.makeText(getApplicationContext(),"Formato de matrícula no correcto",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
