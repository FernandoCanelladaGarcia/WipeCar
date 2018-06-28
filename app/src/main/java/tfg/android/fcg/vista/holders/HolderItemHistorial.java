package tfg.android.fcg.vista.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import tfg.android.fcg.R;

public class HolderItemHistorial extends RecyclerView.ViewHolder implements OnClickListener {

    public TextView h_Nombre, h_Fecha, h_Facultad;

    public HolderItemHistorial(View itemView) {
        super(itemView);

        this.h_Nombre = (TextView) itemView.findViewById(R.id.NombreHistorial);
        this.h_Facultad = (TextView)itemView.findViewById(R.id.Facultad);
        this.h_Fecha = (TextView)itemView.findViewById(R.id.Fecha);
    }

    @Override
    public void onClick(View v) {

    }
}
