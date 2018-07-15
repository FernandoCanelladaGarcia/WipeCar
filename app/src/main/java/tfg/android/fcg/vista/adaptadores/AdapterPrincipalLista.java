package tfg.android.fcg.vista.adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.modelo.Vinculo;

public class AdapterPrincipalLista extends BaseAdapter{

    private ArrayList<Usuario> listaUsuarios;
    private ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();
    private ArrayList<Vinculo> listaVinculos;
    private Context contexto;
    private AppMediador appMediador;
    private boolean rol;
    private final static String TAG = "depurador";

    public AdapterPrincipalLista(Context contexto, ArrayList<Usuario> listaUsuarios, ArrayList<Vinculo> vinculos, AppMediador appMediador){
        this.listaUsuarios = listaUsuarios;
        this.listaVinculos = vinculos;
        this.contexto = contexto;
        this.appMediador = appMediador;
        SharedPreferences sharedPreferences = appMediador.getSharedPreferences("Login", 0);
        rol = sharedPreferences.getBoolean("rol", false);
    }

    public AdapterPrincipalLista(Context contexto, ArrayList<Usuario> listaUsuarios, AppMediador appMediador, ArrayList<Vehiculo> listaVehiculos){
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

            if(rol){
                v = inflater.inflate(R.layout.item_lista_principal_pasajero,null);
                final Usuario pasajero = listaUsuarios.get(position);
                final Vinculo vinculo = listaVinculos.get(position);
                Log.i(TAG,"Detino del vinculo " + vinculo.getDestino());
                TextView nombrePasajero = (TextView) v.findViewById(R.id.NombrePasajero);
                TextView origenPasajero = (TextView) v.findViewById(R.id.DireccionPasajero);
                TextView valoracionPasajero = (TextView) v.findViewById(R.id.valoracionPasajero);
                nombrePasajero.setText(pasajero.getNombre());
                origenPasajero.setText(pasajero.getOrigen());
                if(pasajero.getValoracion() != 0) {
                    valoracionPasajero.setText(String.valueOf(pasajero.getValoracion()));
                }else{
                    valoracionPasajero.setText("Sin valorar");
                }
                if(vinculo.isVinculo()){
                    CardView cardView = (CardView) v.findViewById(R.id.cardItemListaUsuario_Pasajero);
                    cardView.setCardBackgroundColor(appMediador.getResources().getColor(R.color.background2));
                }else{
                    CardView cardView = (CardView) v.findViewById(R.id.cardItemListaUsuario_Pasajero);
                    cardView.setCardBackgroundColor(appMediador.getResources().getColor(R.color.noactivo));
                }
                ImageButton options = (ImageButton)v.findViewById(R.id.opcionesItemPasajero);
                options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu menu = new PopupMenu(contexto,v);
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                switch (id){
                                    case R.id.aceptarPasajero:
                                        //Confirmar vinculo
                                        Object[] datos = new Object[2];
                                        datos[0] = 5;
                                        datos[1] = vinculo;
                                        appMediador.getVistaPrincipal().mostrarDialogo(datos);
                                        //Enviar push OK
                                        break;
                                    case R.id.rechazarPasajero:
                                        //Eliminar vinculo
                                        Object[] informacion = new Object[2];
                                        informacion[0] = 4;
                                        informacion[1] = vinculo;
                                        appMediador.getVistaPrincipal().mostrarDialogo(informacion);
                                        //Enviar push No OK
                                        break;
                                    case R.id.chatPasajero:
                                        if(vinculo.isVinculo()){
                                            //HACER MUESTRA DIALOGO DE CHAT
                                            Object[] respuesta = new Object[2];
                                            respuesta[0] = 7;
                                            respuesta[1] = pasajero;
                                            appMediador.getVistaPrincipal().mostrarDialogo(respuesta);
                                        }else{
                                            //Vinculo no confirmado Toast de no confirmado
                                            Toast.makeText(appMediador.getApplicationContext(),"No ha aceptado al pasajero como acompañante",Toast.LENGTH_SHORT).show();
                                        }
                                        break;
                                }
                                return true;
                            }
                        });
                        menu.inflate(R.menu.menu_item_principal_pasajero);
                        menu.show();
                    }
                });

            }else{
                v = inflater.inflate(R.layout.item_lista_principal_conductor,null);
                final Usuario conductor = listaUsuarios.get(position);
                Vehiculo vehiculo = listaVehiculos.get(position);
                TextView nombreConductor = (TextView) v.findViewById(R.id.NombreConductor);
                TextView fechaSalida = (TextView) v.findViewById(R.id.Fecha);
                TextView marcaCoche = (TextView)v.findViewById(R.id.MarcaVehiculo);
                TextView modeloCoche = (TextView)v.findViewById(R.id.ModeloVehiculo);
                TextView destinovehiculo = (TextView)v.findViewById(R.id.DestinoVehiculo);
                TextView valoracionCond = (TextView)v.findViewById(R.id.valoracionConductor);
                nombreConductor.setText(conductor.getNombre());
                fechaSalida.setText(conductor.getFecha() + "-" +conductor.getHora());
                marcaCoche.setText(vehiculo.getMarca());
                modeloCoche.setText(vehiculo.getModelo());
                destinovehiculo.setText(conductor.getDestino());
                if(conductor.getValoracion() != 0) {
                    valoracionCond.setText(String.valueOf(conductor.getValoracion()));
                }else{
                    valoracionCond.setText("Sin valorar");
                }
                ImageButton options = (ImageButton)v.findViewById(R.id.opcionesItemConductor);
                options.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PopupMenu menu = new PopupMenu(contexto,v);
                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId();
                                switch(id){
                                    case R.id.seleccionarConductor:
                                        Object[] informacion = new Object[]{3,conductor};
                                        appMediador.getVistaPrincipal().mostrarDialogo(informacion);
                                        break;
                                }
                                return true;
                            }
                        });
                        menu.inflate(R.menu.menu_item_principal_conductor);
                        menu.show();
                    }
                });
            }
        }
        return v;
    }
}
