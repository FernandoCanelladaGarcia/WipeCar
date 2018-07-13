package tfg.android.fcg.vista;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

import tfg.android.fcg.AppMediador;
import tfg.android.fcg.R;
import tfg.android.fcg.modelo.Usuario;
import tfg.android.fcg.modelo.Vinculo;
import tfg.android.fcg.presentador.IPresentadorOTGConductor;

/**
 * Created by ferca on 21/03/2018.
 */

public class VistaOTGConductor extends Fragment implements IVistaOTGConductor, View.OnClickListener, TextToSpeech.OnInitListener{

    private AppMediador appMediador;
    private IPresentadorOTGConductor presentadorOTGConductor;
    private Button botonIniciarRuta, botonAceptarPasajero, botonRechazarPasajero, botonFinalizarRuta;
    private Usuario user;
    private Vinculo peticion;
    private TextToSpeech textToSpeech;
    @Override
    public View onCreateView (LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = layoutInflater.inflate(R.layout.layout_vista_otgconductor,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        appMediador = AppMediador.getInstance();
        appMediador.setVistaOTGConductor(this);
        presentadorOTGConductor = appMediador.getPresentadorOTGConductor();
        textToSpeech = new TextToSpeech(getActivity().getApplicationContext(),this);
    }

    @Override
    public void onInit(int status){
        if ( status == TextToSpeech.LANG_MISSING_DATA | status == TextToSpeech.LANG_NOT_SUPPORTED )
        {
            Toast.makeText(getActivity().getApplicationContext(), "ERROR LANG_MISSING_DATA | LANG_NOT_SUPPORTED", Toast.LENGTH_SHORT ).show();
        }
    }

    private void speak(String texto){
        textToSpeech.setLanguage( new Locale( "spa", "ESP" ) );
        textToSpeech.speak(texto,TextToSpeech.QUEUE_FLUSH,null,TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID);
        textToSpeech.setSpeechRate( 0.0f );
        textToSpeech.setPitch( 0.0f );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        botonIniciarRuta = (Button) view.findViewById(R.id.botonIniciarRuta);
        botonIniciarRuta.setOnClickListener(this);
        botonAceptarPasajero = (Button) view.findViewById(R.id.botonAceptarPasajero);
        botonAceptarPasajero.setOnClickListener(this);
        botonRechazarPasajero = (Button) view.findViewById(R.id.botonRechazarPasajero);
        botonRechazarPasajero.setOnClickListener(this);
        botonFinalizarRuta = (Button) view.findViewById(R.id.botonFinalizarRuta);
        botonFinalizarRuta.setEnabled(false);
        botonFinalizarRuta.setOnClickListener(this);
    }

    @Override
    public void indicarPeticionPasajero(Object informacion) {
        if(informacion == null){
            speak("Aun no ha recibido ninguna petición");
        }else{
            peticion = (Vinculo)informacion;
            speak("Pasajero con destino " + peticion.getDestino() + " en "+ peticion.getOrigen() + " solicita ser recogido");
        }
    }

    @Override
    public void indicarPasajeroAceptado(Object informacion) {
        speak("Pasajero en "+ peticion.getOrigen() + " con destino " + peticion.getDestino() + " aceptado");
    }

    @Override
    public void indicarPasajeroRechazado(Object informacion) {
        speak("La peetición de pasajero ha sido rechazada");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.botonIniciarRuta:
                appMediador.getVistaPrincipal().mostrarProgreso();
                user = appMediador.getVistaPrincipal().getUsuario();
                appMediador.getPresentadorOTGConductor().iniciar(user);
                botonFinalizarRuta.setEnabled(true);
                break;
            case R.id.botonAceptarPasajero:
                appMediador.getPresentadorOTGConductor().tratarAceptar(peticion);
                break;
            case R.id.botonRechazarPasajero:
                appMediador.getPresentadorOTGConductor().tratarRechazar(peticion);
                break;
            case R.id.botonFinalizarRuta:
                appMediador.getVistaPrincipal().mostrarProgreso();
                appMediador.getPresentadorOTGConductor().tratarParar(peticion);
                botonFinalizarRuta.setEnabled(false);
                break;

        }
    }

    @Override
    public void onDestroy() {

        if ( textToSpeech != null )
        {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
