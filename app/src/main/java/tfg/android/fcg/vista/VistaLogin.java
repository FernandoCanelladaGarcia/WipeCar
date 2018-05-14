package tfg.android.fcg.vista;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.presentador.IPresentadorLogin;

public class VistaLogin extends AppCompatActivity implements IVistaLogin, View.OnClickListener {

    private Button botonLogin, botonRegistro, botonRecordarPassword;
    private EditText email, password;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private IPresentadorLogin presentadorLogin;

    private final static String TAG = "depurador";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_login);
        appMediador = (AppMediador) this.getApplication();
        appMediador.setVistaLogin(this);
        presentadorLogin = appMediador.getPresentadorLogin();
        botonLogin = (Button) findViewById(R.id.botonlogin);
        botonLogin.setOnClickListener(this);

        botonRecordarPassword = (Button)  findViewById(R.id.botonRecordar);
        botonRecordarPassword.setOnClickListener(this);

        botonRegistro = (Button)  findViewById(R.id.botonRegistro);
        botonRegistro.setOnClickListener(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText)  findViewById(R.id.password);

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        String correoGuardado = sharedPreferences.getString("email", null);

        if (correoGuardado != null) {
            email.setText(correoGuardado);
        }
        Log.i(TAG,"Vista Login");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.botonlogin:
                if (camposValidos()) {
                    Log.i(TAG, "login");
                    String[] login = new String[2];
                    login[0] = email.getText().toString().trim();
                    login[1] = password.getText().toString().trim();
                    Log.i(TAG, email.getText().toString() + " " + password.getText().toString());
                    presentadorLogin.tratarLogin(login);
                    break;
                }
                break;
            case R.id.botonRegistro:
                Log.i(TAG,"registro");
                presentadorLogin.tratarNuevo();
                break;
            case R.id.botonRecordar:
                Log.i(TAG,"recordar");
                mostrarDialogo(1);
                break;
            default:
                Log.i(TAG,"Algo va mal");
                return;
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
        Log.i(TAG,"informacion " + informacion.toString());
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        switch (tipo) {
            case 0:
                dialogBuild.setTitle("Error en el login");
                dialogBuild.setMessage("No se ha podido iniciar sesión. " +
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
                final EditText email = new EditText(this);
                dialogBuild.setTitle("Recuperar Contraseña");
                dialogBuild.setMessage("Introduza su email para poder reenviarle " +
                        "un correo de recuperacion de contraseña");
                dialogBuild.setView(email);
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(email.getText().toString().trim())){
                            Toast.makeText(getApplicationContext(),
                                    "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                        }else if(email.getText().toString().trim().matches("^[a-zA-Z0-9_.+-]+@(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?(alu.ulpgc|)\\.com$")){
                            Toast.makeText(getApplicationContext(),
                                    "El correo no pertenece a la ULPGC", Toast.LENGTH_SHORT).show();
                        }else{
                            presentadorLogin.tratarRecuperarPassword(email.getText().toString().trim());
                            cerrarDialogo();
                        }
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 2:
                Toast.makeText(getApplicationContext(),
                        "Email de recuperacion de contraseña enviado satisfactoriamente",
                        Toast.LENGTH_LONG).show();
                break;
        }

    }


    @Override
    public void cerrarDialogo() {
        dialogo.cancel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appMediador.removePresentadorLogin();
    }

    private boolean camposValidos() {
        if (TextUtils.isEmpty(email.getText().toString()) ||
                TextUtils.isEmpty(password.getText().toString())){
            Log.i(TAG,"Algo vacio");
            Toast.makeText(getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().toString().trim().matches("^[a-zA-Z0-9_.+-]+@(?:(?:[a-zA-Z0-9-]+\\.)?[a-zA-Z]+\\.)?(alu.ulpgc|)\\.com$")){
            Log.i(TAG,"no ulpgc");
            Toast.makeText(getApplicationContext(), "El correo no pertenece a la ULPGC", Toast.LENGTH_SHORT).show();
            return false;
        } else if (email.getText().toString().length() < 6) {
            Log.i(TAG,"contraseña invalida");
            Toast.makeText(getApplicationContext(), "No es una longitud valida de contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
