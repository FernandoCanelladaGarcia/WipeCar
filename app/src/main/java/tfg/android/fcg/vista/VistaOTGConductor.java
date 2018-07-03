package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tfg.android.fcg.R;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaOTGConductor extends Fragment implements IVistaOTGConductor{


    @Override
    public View onCreateView (LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return layoutInflater.inflate(R.layout.fragment_otg, container, false);
    }

    @Override
    public void indicarPeticionPasajero(Object informacion) {

    }

    @Override
    public void indicarPasajeroAceptado(Object informacion) {

    }

    @Override
    public void indicarPasajeroRechazado(Object informacion) {

    }
}
