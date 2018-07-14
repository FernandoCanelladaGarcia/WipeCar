package tfg.android.fcg.vista.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Historial;

public class AdapterHistorialLista extends BaseAdapter{

    private List<Historial> listaHistorial;
    private Context contexto;
    private String idUser;
    private AppMediador appMediador;

    public AdapterHistorialLista(Context contexto, List<Historial> listaHistorial, String idUser, AppMediador appMediador){
        this.listaHistorial = listaHistorial;
        this.contexto = contexto;
        this.idUser = idUser;
        this.appMediador = appMediador;
    }

    @Override
    public int getCount() {
        return listaHistorial.size();
    }

    @Override
    public Object getItem(int position) {
        return listaHistorial.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_lista_historial,null);
        }

        final Historial historial = listaHistorial.get(position);
        TextView nombreUsuario = (TextView)v.findViewById(R.id.NombreHistorial);
        TextView fecha = (TextView)v.findViewById(R.id.Fecha);
        TextView facultad = (TextView)v.findViewById(R.id.Facultad);
        ImageButton options = (ImageButton)v.findViewById(R.id.opcionesItemHistorial);
        ImageView icono = (ImageView) v.findViewById(R.id.iconoUserHistorial);

        if(historial.getIdPasajero().equals(idUser)){
            //Icono Coche
            icono.setImageResource(R.drawable.icon_car_user);
            nombreUsuario.setText(historial.getNombreConductor());

        }else{
            //Icono Pasajero
            icono.setImageResource(R.drawable.icon_user);
            nombreUsuario.setText(historial.getNombrePasajero());
        }
        fecha.setText(historial.getFecha() + " - "+ historial.getHora());
        facultad.setText(historial.getDestino());

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(contexto, v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.eliminarHistorial:
                                Toast.makeText(contexto,"Eliminar Historial", Toast.LENGTH_SHORT).show();
                                Object[] datos = new Object[]{true,historial.getIdPasajero(),historial.getIdConductor()};
                                appMediador.getPresentadorHistorial().tratarEliminar(datos);
                                break;
                            case R.id.agregarValoracion:
                                Toast.makeText(contexto,"Agregar Valoracion", Toast.LENGTH_SHORT).show();
                                if(historial.getIdPasajero().equals(idUser)){
                                    appMediador.getPresentadorHistorial().tratarValorar(historial.getIdConductor());
                                    Object[] val1 = new Object[]{false,idUser,historial.getIdConductor()};
                                    appMediador.getPresentadorHistorial().tratarEliminar(val1);
                                }else{
                                    Object[] val2 = new Object[]{false,historial.getIdPasajero(),idUser};
                                    appMediador.getPresentadorHistorial().tratarValorar(historial.getIdPasajero());
                                    appMediador.getPresentadorHistorial().tratarEliminar(val2);
                                }
                                break;
                        }
                        return true;
                    }
                });
                menu.inflate(R.menu.menu_item_lista_historial);
                menu.show();
            }
        });
        return v;
    }
}
