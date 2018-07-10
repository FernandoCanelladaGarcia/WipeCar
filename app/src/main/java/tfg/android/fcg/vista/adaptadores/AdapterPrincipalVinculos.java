package tfg.android.fcg.vista.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vehiculo;
import tfg.android.fcg.modelo.Vinculo;

public class AdapterPrincipalVinculos extends BaseAdapter{

    private ArrayList<Usuario> listaVinculos;
    private ArrayList<Vehiculo> listaVehiculos;
    private ArrayList<Vinculo> vinculos;
    private Context context;
    private AppMediador appMediador;
    private final static String TAG = "depurador";

    public AdapterPrincipalVinculos(Context context, ArrayList<Usuario> vinculos, AppMediador appMediador, ArrayList<Vehiculo> vehiculosVinculo, ArrayList<Vinculo> listaVinculos){
        this.listaVinculos = vinculos;
        this.listaVehiculos = vehiculosVinculo;
        this.vinculos = listaVinculos;
        this.appMediador = appMediador;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listaVinculos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaVinculos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.item_lista_principal_conductor,null);
            Usuario conductor = listaVinculos.get(position);
            Vehiculo vehiculo = listaVehiculos.get(position);
            Vinculo vinculo = vinculos.get(position);
            Log.i(TAG,"Detino del vinculo " + vinculo.getDestino());
            TextView nombreConductor = (TextView) v.findViewById(R.id.NombreConductor);
            TextView fechaSalida = (TextView) v.findViewById(R.id.Fecha);
            TextView marcaCoche = (TextView)v.findViewById(R.id.MarcaVehiculo);
            TextView modeloCoche = (TextView)v.findViewById(R.id.ModeloVehiculo);
            TextView destinovehiculo = (TextView)v.findViewById(R.id.DestinoVehiculo);
            TextView valoracionVehiculo = (TextView)v.findViewById(R.id.valoracionConductor);
            nombreConductor.setText(conductor.getNombre());
            fechaSalida.setText(conductor.getFecha() + "-" +conductor.getHora());
            marcaCoche.setText(vehiculo.getMarca());
            modeloCoche.setText(vehiculo.getModelo());
            destinovehiculo.setText(conductor.getDestino());
            valoracionVehiculo.setText(conductor.getValoracion());
            ImageButton options = (ImageButton)v.findViewById(R.id.opcionesItemConductor);
            options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu menu = new PopupMenu(context,v);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            int id = item.getItemId();
                            switch(id){
                                case R.id.chatConductor:
                                    break;
                                case R.id.eliminarConductor:
                                    break;
                            }
                            return true;
                        }
                    });
                    menu.inflate(R.menu.menu_item_principal_conductor);
                    menu.getMenu().getItem(2).setVisible(true);
                    menu.getMenu().getItem(1).setVisible(true);
                    menu.getMenu().getItem(0).setVisible(false);
                    menu.show();
                }
            });
        }
        return v;
    }
}
