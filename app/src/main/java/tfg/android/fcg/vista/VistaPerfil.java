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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
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

    private Button botonHistorial, botonAyuda;
    private FloatingActionButton botonEditar, botonGuardarPerfil, botonEliminarPerfil;
    private TextView nombre, telefono, email, password, marca, modelo, matricula, guardarPerfilTitle, eliminarPerfilTitle;
    private EditText editNombre, editTelefono, editEmail, editPass, editMarca, editModelo, editMatricula;
    private Switch modoConductor;
    private ProgressDialog dialogoProgreso;
    private AlertDialog dialogo;
    private AppMediador appMediador;
    private Usuario user;
    private Vehiculo vehiculo;
    private SharedPreferences sharedPreferences;
    private IPresentadorPerfil presentadorPerfil;
    private Object[] perfil;
    private boolean fabAbierto, datosVehiculo;
    private String datoVehiculo;
    private final static String TAG = "depurador";

    private Animation show_guardar;
    private Animation hide_guardar;
    private Animation show_eliminar;
    private Animation hide_eliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_perfil);
        appMediador = AppMediador.getInstance();
        appMediador.setVistaPerfil(this);
        presentadorPerfil = appMediador.getPresentadorPerfil();
        perfil = new Object[9];

        mostrarProgreso();

        fabAbierto = false;
        datosVehiculo = false;
        datoVehiculo = "";

        show_guardar = AnimationUtils.loadAnimation(this, R.anim.guardar_show);
        hide_guardar = AnimationUtils.loadAnimation(this, R.anim.guardar_hide);
        show_eliminar = AnimationUtils.loadAnimation(this, R.anim.eliminar_show);
        hide_eliminar = AnimationUtils.loadAnimation(this, R.anim.eliminar_hide);

        guardarPerfilTitle = (TextView) findViewById(R.id.guardarPerfilTitle);
        eliminarPerfilTitle = (TextView) findViewById(R.id.eliminarPerfilTitle);

        botonEditar = (FloatingActionButton) findViewById(R.id.editButton);
        botonGuardarPerfil = (FloatingActionButton) findViewById(R.id.guardarPerfil);
        botonEliminarPerfil = (FloatingActionButton) findViewById(R.id.eliminarPerfil);
        botonHistorial = (Button) findViewById(R.id.regisHistorialButton);
        botonAyuda = (Button) findViewById(R.id.ayudaButton);
        modoConductor = (Switch) findViewById(R.id.modoConductor);

        nombre = (TextView) findViewById(R.id.marcaRegistro);
        telefono = (TextView) findViewById(R.id.modeloRegistro);
        email = (TextView) findViewById(R.id.emailPerfil);
        password = (TextView) findViewById(R.id.passwordPerfil);
        marca = (TextView) findViewById(R.id.marcaTitle);
        modelo = (TextView) findViewById(R.id.modeloTitle);
        matricula = (TextView) findViewById(R.id.matriculaTitle);

        editNombre = (EditText) findViewById(R.id.editTextNombre);
        editTelefono = (EditText) findViewById(R.id.editTextTelefono);
        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPass = (EditText) findViewById(R.id.editTextPassword);
        editMarca = (EditText) findViewById(R.id.editTextMarca);
        editModelo = (EditText) findViewById(R.id.editTextModelo);
        editMatricula = (EditText) findViewById(R.id.editTextMatricula);

        sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        String idUser = sharedPreferences.getString("idUser", null);
        presentadorPerfil.iniciar(idUser);
        email.setText(sharedPreferences.getString("email", null));
        password.setText(sharedPreferences.getString("password", null));

        boolean rol = sharedPreferences.getBoolean("rol", false);
        modoConductor.setChecked(rol);
        modoConductor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (user.getDatoVehiculo() != null) {
                    modoConductor.setChecked(isChecked);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("rol", isChecked);
                    if (isChecked) {
                        Toast.makeText(getApplicationContext(), "MODO CONDUCTOR", Toast.LENGTH_SHORT).show();
                        presentadorPerfil.tratarConductor(isChecked);
                    } else {
                        Toast.makeText(getApplicationContext(), "MODO PASAJERO", Toast.LENGTH_SHORT).show();
                        presentadorPerfil.tratarConductor(isChecked);
                    }
                    editor.apply();
                } else if (!botonEditar.isEnabled()) {
                    if (isChecked) {
                        Toast.makeText(getApplicationContext(), "MODO CONDUCTOR", Toast.LENGTH_SHORT).show();
                        perfil[7] = isChecked;
                    } else {
                        Toast.makeText(getApplicationContext(), "MODO PASAJERO", Toast.LENGTH_SHORT).show();
                        perfil[7] = isChecked;
                    }
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("rol", true);
                    editor.apply();
                    mostrarDialogo(0);
                }
            }
        });
        editEmail.setText(sharedPreferences.getString("email", null));
        editPass.setText(sharedPreferences.getString("password", null));
        Log.i(TAG, "Vista Perfil");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                if (!fabAbierto) {
                    presentadorPerfil.tratarEditar(1);
                } else {
                    presentadorPerfil.tratarEditar(3);
                }
                break;
            case R.id.regisHistorialButton:
                presentadorPerfil.tratarHistorial();
                break;
            case R.id.guardarPerfil:
                presentadorPerfil.tratarGuardar(2);
                break;
            case R.id.eliminarPerfil:
                presentadorPerfil.tratarPapelera(4);
                break;
            case R.id.ayudaButton:
                Toast.makeText(getApplicationContext(), "Ayuda para el usuario como Dialogo", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void mostrarProgreso() {
        Log.i(TAG, "mostrar Progreso");
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
        Log.i(TAG, "cerrar Progreso");
        dialogoProgreso.dismiss();
    }

    @Override
    public void mostrarDialogo(Object informacion) {
        int tarea = (int) informacion;
        AlertDialog.Builder dialogBuild = new AlertDialog.Builder(this);
        switch (tarea) {
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
            case 1:
                dialogBuild.setTitle("Modo Edición");
                dialogBuild.setMessage("¿Desea editar o eliminar su perfil?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presentadorPerfil.tratarOk(1);
                        botonHistorial.setEnabled(false);
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 2:
                dialogBuild.setTitle("Modo Edición");
                dialogBuild.setMessage("¿Esta seguro?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (obtenerNuevoPerfil() != null) {
                            presentadorPerfil.tratarGuardarPerfil(obtenerNuevoPerfil());
                            botonEditar.setEnabled(true);
                            hideFabButtons();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No ha introducido cambios", Toast.LENGTH_SHORT).show();
                            cerrarDialogo();
                        }
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 3:
                dialogBuild.setTitle("Modo Edición");
                dialogBuild.setMessage("¿Desea cancelar su edicion?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (obtenerNuevoPerfil() != null) {
                            presentadorPerfil.tratarOk(2);
                            hideFabButtons();
                        } else {
                            cerrarDialogo();
                        }
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cerrarDialogo();
                    }
                });
                dialogo = dialogBuild.create();
                dialogo.show();
                break;
            case 4:
                dialogBuild.setTitle("¡ATENCIÓN!");
                dialogBuild.setMessage("Esta a punto de eliminar su cuenta en WipeCar. Esta acción no puede ser deshecha. ¿Esta seguro?");
                dialogBuild.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Object[] perfil = new Object[3];
                        perfil[0] = user.getIdUser();
                        perfil[1] = email.getText().toString();
                        perfil[2] = password.getText().toString();
                        presentadorPerfil.tratarEliminarPerfil(perfil);
                    }
                });
                dialogBuild.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
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
        dialogo.cancel();
    }

    @Override
    public void prepararEdicion() {
        Log.i(TAG, "Modo Edicion");
        fabAbierto = true;
        nombre.setVisibility(View.INVISIBLE);
        telefono.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        modelo.setVisibility(View.INVISIBLE);
        marca.setVisibility(View.INVISIBLE);
        matricula.setVisibility(View.INVISIBLE);

        editNombre.setVisibility(View.VISIBLE);
        editTelefono.setVisibility(View.VISIBLE);
        editEmail.setVisibility(View.VISIBLE);
        editPass.setVisibility(View.VISIBLE);
        editModelo.setVisibility(View.VISIBLE);
        editMarca.setVisibility(View.VISIBLE);
        editMatricula.setVisibility(View.VISIBLE);
        cerrarProgreso();
        showFabButtons();
    }

    //TODO: NUEVO, REDACCION
    @Override
    public void salirEdicion() {
        Log.i(TAG, "Salir modo Edicion");
        fabAbierto = false;
        nombre.setVisibility(View.VISIBLE);
        telefono.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        modelo.setVisibility(View.VISIBLE);
        marca.setVisibility(View.VISIBLE);
        matricula.setVisibility(View.VISIBLE);

        editNombre.setVisibility(View.INVISIBLE);
        editTelefono.setVisibility(View.INVISIBLE);
        editEmail.setVisibility(View.INVISIBLE);
        editPass.setVisibility(View.INVISIBLE);
        editModelo.setVisibility(View.INVISIBLE);
        editMarca.setVisibility(View.INVISIBLE);
        editMatricula.setVisibility(View.INVISIBLE);
        cerrarProgreso();
        hideFabButtons();
    }

    @Override
    public void prepararVista(Object informacion) {
        Object[] info = (Object[]) informacion;
        user = (Usuario) info[0];
        if (info[1] != "") {
            datosVehiculo = true;
            datoVehiculo = (String) user.getDatoVehiculo();
            vehiculo = (Vehiculo) info[1];
            modelo.setText(vehiculo.getModelo());
            marca.setText(vehiculo.getMarca());
            matricula.setText(vehiculo.getMatricula());
            editModelo.setText(vehiculo.getModelo());
            editMarca.setText(vehiculo.getMarca());
            editMatricula.setText(vehiculo.getMatricula());
        }
        nombre.setText(user.getNombre());
        telefono.setText(user.getTelefono());
        editNombre.setText(user.getNombre());
        editTelefono.setText(user.getTelefono());
        cerrarProgreso();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appMediador.removePresentadorPerfil();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    private Object obtenerNuevoPerfil() {
        String[] partes = editEmail.getText().toString().split("@");

        if (nombre.getText().toString() != editNombre.getText().toString()) {
            perfil[0] = editNombre.getText().toString();
        }
        if (telefono.getText().toString() != editTelefono.getText().toString()) {
            perfil[1] = editTelefono.getText().toString();
        }
        if (email.getText().toString() != editEmail.getText().toString() && partes[1].equals("alu.ulpgc.es")) {
            perfil[2] = editEmail.getText().toString();
        }
        if (password.getText().toString() != editPass.getText().toString()) {
            perfil[3] = editPass.getText().toString();
        }
        if (modelo.getText().toString() != editModelo.getText().toString()) {
            perfil[5] = editModelo.getText().toString();
        }
        if (marca.getText().toString() != editMarca.getText().toString()) {
            perfil[4] = editMarca.getText().toString();
        }
        if (matricula.getText().toString() != editMatricula.getText().toString()) {
            perfil[6] = editMatricula.getText().toString();
        }
        if (datoVehiculo != "") {
            perfil[8] = datoVehiculo;
        } else {
            return null;
        }
        return perfil;
    }

    private void showFabButtons() {
        botonGuardarPerfil.setVisibility(View.VISIBLE);
        botonEliminarPerfil.setVisibility(View.VISIBLE);

        botonGuardarPerfil.startAnimation(show_guardar);
        botonEliminarPerfil.startAnimation(show_eliminar);

        guardarPerfilTitle.setVisibility(View.VISIBLE);
        eliminarPerfilTitle.setVisibility(View.VISIBLE);
    }

    private void hideFabButtons() {
        guardarPerfilTitle.setVisibility(View.INVISIBLE);
        eliminarPerfilTitle.setVisibility(View.INVISIBLE);

        botonGuardarPerfil.startAnimation(hide_guardar);
        botonEliminarPerfil.startAnimation(hide_eliminar);

        botonGuardarPerfil.setVisibility(View.INVISIBLE);
        botonEliminarPerfil.setVisibility(View.INVISIBLE);
    }
}
