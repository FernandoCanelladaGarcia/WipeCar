package tfg.android.fcg.vista.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Historial;
import tfg.android.fcg.vista.holders.HolderItemHistorial;

public class AdapterHistorialLista extends RecyclerView.Adapter<HolderItemHistorial>{

    private List<Historial> listaHistorial;
    private Context contexto;

    public AdapterHistorialLista(Context contexto, List<Historial> listaHistorial){
        this.listaHistorial = listaHistorial;
        this.contexto = contexto;
    }

    @Override
    public HolderItemHistorial onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_historial,null);
        HolderItemHistorial holderItemHistorial = new HolderItemHistorial(layoutView);
        return holderItemHistorial;
    }

    @Override
    public void onBindViewHolder(HolderItemHistorial holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.listaHistorial.size();
    }
}
