package tfg.android.fcg.vista;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tfg.android.fcg.AppMediador;

public class VistaLogin extends AppCompatActivity implements IVistaLogin, View.OnClickListener {

    private Button botonLogin, botonRegistro, botonRecordarPassword;
    private EditText email, password;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_login);
        appMediador = (AppMediador) this.getApplication();
        appMediador.setVistaLogin(this);

        botonLogin = (Button) findViewById(R.id.botonlogin);
        botonRecordarPassword = (Button) findViewById(R.id.botonRecordar);
        botonRegistro = (Button) findViewById(R.id.botonRegistro);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        setContentView(R.layout.layout_vista_login);

        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        String correoGuardado = sharedPreferences.getString("email", null);

        if (correoGuardado != null) {
            email.setText(correoGuardado);
        }

        botonLogin.setOnClickListener(this);
        botonRegistro.setOnClickListener(this);
        botonRecordarPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.botonlogin:
                if (camposValidos()) {
                    Object[] login = new Object[2];
                    login[0] = email.getText().toString().trim();
                    login[1] = password.getText().toString().trim();
                    appMediador.getPresentadorLogin().tratarLogin(login);
                    break;
                }
            case R.id.botonRegistro:
                appMediador.getPresentadorLogin().tratarNuevo();
                break;
            case R.id.botonRecordar:
                mostrarDialogo(1);
                break;
        }
    }


    @Override
    public void mostrarProgreso() {
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
        dialogoProgreso.dismiss();
    }


    @Override
    public void mostrarDialogo(Object informacion) {

        int tipo = (int) informacion;
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
                                    "Rellena todos los campos", Toast.LENGTH_SHORT);
                        }else if(email.getText().toString().trim().matches(".*@alu.ulpgc.es*")){
                            Toast.makeText(getApplicationContext(),
                                    "El correo no pertenece a la ULPGC", Toast.LENGTH_SHORT);
                        }else{
                            appMediador.getPresentadorLogin()
                                    .tratarRecuperarPassword(email.getText().toString().trim());
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
                        Toast.LENGTH_LONG);
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

        if (TextUtils.isEmpty(email.getText().toString().trim()) ||
                TextUtils.isEmpty(password.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(), "Rellena todos los campos", Toast.LENGTH_SHORT);
            return false;
        } else if (email.getText().toString().trim().matches(".*@alu.ulpgc.es*")) {
            Toast.makeText(getApplicationContext(), "El correo no pertenece a la ULPGC", Toast.LENGTH_SHORT);
            return false;
        } else if (email.getText().toString().trim().length() < 6) {
            Toast.makeText(getApplicationContext(), "No es una longitud valida de contraseña", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }
}
