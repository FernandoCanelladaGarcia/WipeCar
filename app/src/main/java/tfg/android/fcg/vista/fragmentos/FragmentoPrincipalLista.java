package tfg.android.fcg.vista.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.vista.adaptadores.AdapterPrincipalLista;

public class FragmentoPrincipalLista extends Fragment{

    public FragmentoPrincipalLista(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pickup, container, false);
    }
}
