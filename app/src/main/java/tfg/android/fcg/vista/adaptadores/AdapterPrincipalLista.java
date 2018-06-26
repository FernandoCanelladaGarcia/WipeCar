package tfg.android.fcg.vista.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.vista.holders.HolderItemPrincipal;

public class AdapterPrincipalLista extends RecyclerView.Adapter<HolderItemPrincipal>{

    protected Context contexto;
    protected ArrayList<Usuario> listaUsuarios;

    public AdapterPrincipalLista(Context contexto, ArrayList<Usuario> listaUsuarios){
        this.contexto = contexto;
        this.listaUsuarios = listaUsuarios;
    }

    @Override
    public HolderItemPrincipal onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        HolderItemPrincipal mainHolder;
        ViewGroup mainGroup;

        //TODO: CHECK MODO USO USUARIO
        if(1 == 1){
             mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_lista_principal_conductor,parent,false);
             mainHolder = new HolderItemPrincipal(mainGroup){
                 @Override
                 public String toString(){
                     return super.toString();
                 }
             };

        }else{
            mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_lista_principal_pasajero,parent,false);
            mainHolder = new HolderItemPrincipal(mainGroup){
                @Override
                public String toString(){
                    return super.toString();
                }
            };
        }

        return mainHolder;
    }

    @Override
    public void onBindViewHolder(HolderItemPrincipal holder, int position) {
        final HolderItemPrincipal mainHolder = holder;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
