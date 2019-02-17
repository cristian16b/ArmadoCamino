package co.com.smartgeeks.smartgeeksmapas;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapasFragment extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapasFragment() {
        // Required empty public constructor

        //seteo por defecto las banderas booleanas
        //por defecto marca caminando
        this.caminando = true;
        //seteo a false ir manejando
        this.manejando = false;

        //seteo el origen y destino
        //como que estoy en el ingreso de universidad
        this.longitudDestino = -31.640771;
        this.latitudDestino  = -60.671849;
        this.longitudOrigen = this.longitudDestino;
        this.latitudOrigen  = this.latitudDestino;

        //seteo la url
        this.url = "";
    }

    //banderas booleanadas para elegir si va manejando o caminando
    private boolean caminando;
    private boolean manejando;

    GoogleMap map;
    Boolean actualPosition = true;
    JSONObject jso;

    //coord de origen
    Double longitudOrigen, latitudOrigen;
    //coord de destino
    Double longitudDestino,latitudDestino;
    //url
    String url;

    public void setCaminando()
    {
        this.caminando = true;
        this.manejando = false;
        //String salida = "c:" + String.valueOf(this.caminando) + " m:" + String.valueOf(this.manejando);
        String url = obtenerURL();
        Toast.makeText(getActivity().getApplicationContext(),url, Toast.LENGTH_LONG).show();
    }

    public void setManejando()
    {
        this.caminando = false;
        this.manejando = true;
        //String salida = "c:" + String.valueOf(this.caminando) + " m:" + String.valueOf(this.manejando);
        String url = obtenerURL();
        Toast.makeText(getActivity().getApplicationContext(),url, Toast.LENGTH_LONG).show();
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapasFragment newInstance(String param1, String param2) {
        MapasFragment fragment = new MapasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_mapas, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


           if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }




            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                //2.942043!4d-75.2522789


                if (actualPosition){
                    latitudOrigen = location.getLatitude();
                    longitudOrigen = location.getLongitude();
                    actualPosition=false;

                    LatLng miPosicion = new LatLng(latitudOrigen,longitudOrigen);

                    map.addMarker(new MarkerOptions().position(miPosicion).title("Aqui estoy yo"));

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitudOrigen,longitudOrigen))      // Sets the center of the map to Mountain View
                            .zoom(17)
                            .bearing(90)// Sets the zoom
                            .build();                   // Creates a CameraPosition from the builder
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    //obtengo la ruta
                    String url = obtenerURL();

                    RequestQueue queue = Volley.newRequestQueue(getActivity());

                    map.addMarker(new MarkerOptions().position(new LatLng(-31.640771,-60.671849)).title("Destino"));

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                jso = new JSONObject(response);
                                Toast.makeText(getActivity().getApplicationContext(),jso.toString(), Toast.LENGTH_LONG).show();

                                //armo la ruta
                                trazarRuta(jso);

                                //muestro la distancia y el tiempo requerido
                                mostrarDistanciaTiempo(jso);

                                //
                                Log.i("jsonRuta: ",""+response);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity().getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG).show();

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    queue.add(stringRequest);
                }
            }
        });
    }

    private String obtenerURL()
    {
        //LA URL PARA CONSULTAR AL WEBSERVICE SE USAR COMO SE VE:
        //ORIGIN
        //DESTINATION
        //KEY
        //SON PARAMETROS
        //SI SE PONE EN EL BROWSER TE MUESTRA LA RESPUESTA JSON O LOS ERRORES
        String key = "&key=" + "AIzaSyBXD5pYoMozTOE2fqGe_axUHunFZDXvA2U";

        String modo;
        //segun la bandera booleana
        //si caminando = true, muestro recorrido caminando
        //si caminando = false, muestro recorrido manejando
        if(caminando)
        {
            modo = "&mode=" + "walking";
        }
        else
        {
            modo = "&mode=" + "driving";
        }
        String origen =  this.latitudOrigen  + "," + this.longitudOrigen;
        String destino = this.latitudDestino + "," + this.longitudDestino;
        //armo la url
        String url ="https://maps.googleapis.com/maps/api/directions/json?origin="+ origen +"&destination=" + destino + key + modo;

        //guardo la url
        this.url = url;

//      Toast.makeText(getActivity().getApplicationContext(),modo, Toast.LENGTH_LONG).show();

        return url;
    }

    private void trazarRuta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        //ver https://developers.google.com/maps/documentation/directions/intro
        //ver https://stackoverflow.com/questions/14708935/how-to-get-json-array-values-in-android

        try {
            //getjson array accede al subarray de routes
            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){

                //accedo a la lista de pasos
                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");

                for (int j=0; j<jLegs.length();j++){

                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k<jSteps.length();k++){


                        String polyline = ""+((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end",""+polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        map.addPolyline(new PolylineOptions().addAll(list).color(Color.RED).width(10));
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private void mostrarDistanciaTiempo(JSONObject jso)
    {
        try {

            JSONArray tiempo = jso.getJSONArray("duration");
            JSONArray distancia = jso.getJSONArray("distance");
            //to-do ver como acceder
            //String t = tiempo["value"];



        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
