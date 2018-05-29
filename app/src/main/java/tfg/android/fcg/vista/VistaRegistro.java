package tfg.android.fcg.vista;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.presentador.IPresentadorRegistro;
import tfg.android.fcg.presentador.PresentadorRegistro;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaRegistro extends AppCompatActivity implements IVistaRegistro, View.OnClickListener{

    private Button botonRegistro;
    private EditText nombre, telefono, origen, email, password, repPassword;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorRegistro presentadorRegistro;

    private final static String TAG = "depurador";
    private String[] registro = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_registro);
        appMediador = (AppMediador)this.getApplication();
        appMediador.setVistaRegistro(this);
        presentadorRegistro = appMediador.getPresentadorRegistro();
        botonRegistro = (Button) findViewById(R.id.registro);
        botonRegistro.setOnClickListener(this);

        nombre = (EditText)findViewById(R.id.nombreInput);
        telefono = (EditText) findViewById(R.id.telefonoInput);
        origen = (EditText) findViewById(R.id.direccionInput);
        email = (EditText) findViewById(R.id.emailInput);
        password = (EditText) findViewById(R.id.passwordInput);
        repPassword = (EditText) findViewById(R.id.repPasswordInput);

        Log.i(TAG,"Vista Registro");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.registro){
            if(camposValidos()){
                Log.i(TAG, "Registro");
                registro[0] = email.getText().toString().trim();
                registro[1] = password.getText().toString().trim();
                registro[2] = nombre.getText().toString().trim();
                registro[3] = telefono.getText().toString().trim();
                registro[4] = origen.getText().toString().trim();
                presentadorRegistro.tratarRegistro(1);
            }
        }
    }

    @Override
    public void mostrarProgreso() {
        Log.i(TAG," mostrar Progreso");
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
        int tipo = (int) informacion;
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);

        switch (tipo){
            case 0:
                dialogBuild.setTitle("Error en el registro");
                dialogBuild.setMessage("No se ha podido registrar. " +
                        "Compruebe que ha introducido los campos correctamente.");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 1:
                dialogBuild.setTitle("Registro de usuario");
                dialogBuild.setMessage("¿Esta seguro?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presentadorRegistro.tratarOk(registro);
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presentadorRegistro.tratarCancelar();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Registro realizado satisfactoriamente", Toast.LENGTH_LONG).show();
                break;
            case 3:
                dialogBuild.setTitle("Usuario ya existente");
                dialogBuild.setMessage("No se ha podido registrar." +
                        "Compruebe los parámetros introducidos");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
        Log.i(TAG,"cerrar Dialogo");
        dialogo.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appMediador.removePresentadorRegistro();
    }

    private boolean camposValidos(){
        String[] partes = email.getText().toString().split("@");
        if(TextUtils.isEmpty(nombre.getText().toString())||
                TextUtils.isEmpty(telefono.getText().toString())||
                TextUtils.isEmpty(origen.getText().toString())||
                TextUtils.isEmpty(email.getText().toString())||
                TextUtils.isEmpty(password.getText().toString())||
                TextUtils.isEmpty(repPassword.getText().toString())){
            Log.i(TAG,"Algo vacio");
            Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!partes[1].equals("alu.ulpgc.es")){
            Log.i(TAG,"no ulpgc");
            Toast.makeText(getApplicationContext(), "El correo introducido no pertenece a la ULPGC", Toast.LENGTH_SHORT).show();
            return false;
        }else if (password.getText().toString().length() < 6){
            Log.i(TAG,"contraseña invalida");
            Toast.makeText(getApplicationContext(), "No es una longitud valida de contraseña. Minimo 6 caracteres", Toast.LENGTH_LONG).show();
            return false;
        }else if (!password.getText().toString().equals(repPassword.getText().toString())){
            Log.i(TAG,"contraseñas no coinciden");
            Toast.makeText(getApplicationContext(), "No coinciden las contraseñas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
