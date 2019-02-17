package co.com.smartgeeks.smartgeeksmapas;


import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Pricnipal extends AppCompatActivity implements MapasFragment.OnFragmentInteractionListener {

    //creo una variable del tipo MapsFrament
    private MapasFragment mapa;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricnipal);

        //Fragment fragmento = new MapasFragment();
        this.mapa = new MapasFragment();

        //getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,fragmento).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,this.mapa).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //evento para trazar la ruta si se va caminando
    public void irCaminando(android.view.View view)
    {
        this.mapa.setCaminando();
        //Toast.makeText(this,"CLICK IR CAMINANDO", Toast.LENGTH_LONG).show();
        //this.caminando = true;
        //this.manejando = false;

    }

    //evento para trazar la ruta si se va manejando
    public void irManejando(android.view.View view)
    {
        this.mapa.setManejando();
        //this.caminando = false;
        //this.manejando = true;
        //Toast.makeText(this,"CLICK IR MANEJANDO", Toast.LENGTH_LONG).show();
    }
}
