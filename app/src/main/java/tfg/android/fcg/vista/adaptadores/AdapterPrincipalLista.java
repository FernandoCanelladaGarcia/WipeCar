package tfg.android.fcg.vista.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;

public class AdapterPrincipalLista extends BaseAdapter{
    private List<Usuario> listaUsuarios;
    private List<Vehiculo> listaVehiculos;
    private Context contexto;
    private AppMediador appMediador;
    private FloatingActionButton floatPrincipal;
    private boolean rol;

    public AdapterPrincipalLista(Context contexto, List<Usuario> listaUsuarios, AppMediador appMediador){
        this.listaUsuarios = listaUsuarios;
        this.contexto = contexto;
        this.appMediador = appMediador;
        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        rol = sharedPreferences.getBoolean("rol", false);
    }

    public AdapterPrincipalLista(Context contexto, List<Usuario> listaUsuarios, AppMediador appMediador, List<Vehiculo> listaVehiculos){
        this.listaUsuarios = listaUsuarios;
        this.contexto = contexto;
        this.appMediador = appMediador;
        this.listaVehiculos = listaVehiculos;
        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        rol = sharedPreferences.getBoolean("rol", false);
    }
    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        floatPrincipal = (FloatingActionButton) v.findViewById(R.id.floatPrincipal);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
            if(rol){
                v = inflater.inflate(R.layout.item_lista_principal_pasajero,null);
                Usuario pasajero = listaUsuarios.get(position);
                TextView nombrePasajero = (TextView) v.findViewById(R.id.NombrePasajero);
                TextView origenPasajero = (TextView) v.findViewById(R.id.DireccionPasajero);
                floatPrincipal.setImageResource(R.drawable.icon_edit_salida);
                nombrePasajero.setText(pasajero.getNombre());
                origenPasajero.setText(pasajero.getOrigen());

            }else{
                v = inflater.inflate(R.layout.item_lista_principal_conductor,null);
                Usuario conductor = listaUsuarios.get(position);
                Vehiculo vehiculo = listaVehiculos.get(position);
                TextView nombreConductor = (TextView) v.findViewById(R.id.NombreConductor);
                TextView fechaSalida = (TextView) v.findViewById(R.id.Fecha);
                TextView marcaCoche = (TextView)v.findViewById(R.id.MarcaVehiculo);
                TextView modeloCoche = (TextView)v.findViewById(R.id.ModeloVehiculo);
                floatPrincipal.setImageResource(R.drawable.icon_edit_destino);
                nombreConductor.setText(conductor.getNombre());
                fechaSalida.setText(conductor.getHora());
                marcaCoche.setText(vehiculo.getMarca());
                modeloCoche.setText(vehiculo.getModelo());
            }

        }

        return v;
    }
}
