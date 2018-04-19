package tfg.android.fcg.vista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaOTGConductor extends AppCompatActivity implements IVistaOTGConductor{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vista_otgconductor);
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
