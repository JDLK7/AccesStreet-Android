package taes.accessstreet;
import android.view.LayoutInflater;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.style.EasyEditSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class ListaPuntos {
    public String name;
    public ArrayList<MarkerOptions> puntos;

    public ListaPuntos(String name) {
        this.name = name;
        puntos = new ArrayList<>();
    }
}

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, PopupMenu.OnMenuItemClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private View mapView;
    ArrayList<LatLng> MarkerPoints;
    private LatLng ContextPoint;

    private Marker destino;
    private Marker destino_buscado;
    static Polyline RUTE_LINE;
    private View locationButton;

    private ArrayList<ListaPuntos> marcadores = new ArrayList<>();
    private Marker marcador;
    double lat = 0.0;
    double lgn = 0.0;
    String urlObjetos;
    private double latitudInicial;
    private double longitudInicial;
    private double latitudFinal;
    private double longitudFinal;

    private boolean accessibleParkPref;
    private boolean accessibleWCPref;
    private boolean elevatorPref;
    private boolean accessibleStairPref;
    private boolean accessibleSlopePref;
    private boolean obstacleSlopePref;
    private boolean obstacleStairPref;
    private boolean obstacleSidewalkPref;
    private boolean obstacleWorksPref;

    public static final String PREFS_NAME = "Preferencias";

    /**
     * Carga las preferencias del usuario en memoria para poder filtrar los puntos de interés y los obstáculos.
     */
    protected void loadUserPreferences() {
        SharedPreferences miPreferencia = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        accessibleParkPref = miPreferencia.getBoolean("accessiblePark",false);
        accessibleWCPref = miPreferencia.getBoolean("accessibleWC",false);
        elevatorPref = miPreferencia.getBoolean("elevator",false);
        accessibleStairPref = miPreferencia.getBoolean("accessibleStair",false);
        accessibleSlopePref = miPreferencia.getBoolean("accessibleSlope",false);
        obstacleSlopePref = miPreferencia.getBoolean("obstacleSlope",false);
        obstacleStairPref = miPreferencia.getBoolean("obstacleStair",false);
        obstacleSidewalkPref = miPreferencia.getBoolean("obstacleSidewalk",false);
        obstacleWorksPref = miPreferencia.getBoolean("obstacleWorks",false);
    }

    /**
     * Realiza una petición al servidor para obtener los tipos de avisos disponibles
     * y los guarda en SharedPreferences para su uso posterior. Si no se obtiene
     * nada o no se puede parsear la respuesta, se guarda una cadena vacía.
     */
    protected void loadPointTypes() {
        String url = "http://"
                + getResources().getString(R.string.accesstreet_api_host) + ":"
                + getResources().getString(R.string.accesstreet_api_port) + "/api/tipos";

        final SharedPreferences preferences = getSharedPreferences("taes.accesstreet", Context.MODE_PRIVATE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray types = response.getJSONArray("tipos");
                            preferences.edit().putString("types", types.toString()).apply();
                        } catch (JSONException e) {
                            preferences.edit().putString("types", "").apply();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        preferences.edit().putString("types", "").apply();
                    }
                });

        AccesstreetRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlObjetos = "http://"
                + getResources().getString(R.string.accesstreet_api_host) + ":"
                + getResources().getString(R.string.accesstreet_api_port) + "/api/tiposTodos";

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapView = mapFragment.getView(); //Getting map view
        MarkerPoints = new ArrayList<>(); //Inicializar el vector de marker points

        //Autocomplete search bar
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.getView().setBackgroundColor(Color.WHITE);

        //Changing the icon of the search bar
        ImageView search_button = (ImageView) findViewById(R.id.place_autocomplete_search_button);
        search_button.setImageResource(R.drawable.ic_menu_black_24px);
        search_button.setOnClickListener(this);

        //Find the place and add a marker
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());

                String placeDetailsStr = place.getName() + "\n"
                        + place.getId() + "\n"
                        + place.getLatLng().toString() + "\n"
                        + place.getAddress() + "\n";

                //mMap.clear();
                LatLng coordenadas = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                if (destino_buscado != null) destino_buscado.remove();
                destino_buscado = mMap.addMarker(new MarkerOptions().position(coordenadas).title(place.getAddress().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordenadas));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        //Set location button listener
        FloatingActionButton floating_location = (FloatingActionButton) findViewById(R.id.location_btn);
        floating_location.setOnClickListener(this);

        /**
         * Carga las preferencias del usuario.
         */
        loadUserPreferences();
        loadPointTypes();
    }

    /**
     * Comprueba si un tipo de aviso (DADO SU NOMBRE DE ICONO) está filtrado es deseado por el usuario o no.
     *
     * @param iconName
     * @return
     */
    protected boolean isShownAlert(String iconName) {
        return (iconName.equals("marker_accessible_park") && accessibleParkPref)
            || (iconName.equals("marker_accessible_wc") && accessibleWCPref)
            || (iconName.equals("R.mipmap.ascensor") && elevatorPref)
            || (iconName.equals("marker_accessible_stair") && accessibleStairPref)
            || (iconName.equals("marker_accessible_slope") && accessibleSlopePref)
            || (iconName.equals("marker_obstacle_slope") && obstacleSlopePref)
            || (iconName.equals("marker_obstacle_stair") && obstacleStairPref)
            || (iconName.equals("marker_obstacle_sidewalk") && obstacleSidewalkPref)
            || (iconName.equals("marker_obstacle_works") && obstacleWorksPref);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.place_autocomplete_search_button:
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, OptionsActivity.class);
                startActivity(intent);

            case R.id.location_btn:
                locationButton.callOnClick();
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        Intent addAlertActivity = new Intent(this, AddAlertActivity.class);
        addAlertActivity.putExtra("pointLat", point.latitude);
        addAlertActivity.putExtra("pointLng", point.longitude);
        startActivity(addAlertActivity);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.marker_menu, menu);
        menu.setHeaderTitle("Añadir un marcador");
        menu.setHeaderIcon(R.drawable.ic_location_on_black_24px);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_option1:
                mMap.addMarker(new MarkerOptions()
                        .position(ContextPoint)
                        .title("Escalera")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_stairs)));
                break;
            case R.id.item_option2:
                mMap.addMarker(new MarkerOptions()
                        .position(ContextPoint)
                        .title("Obra")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_worker)));
                break;
            case R.id.item_option3:
                mMap.addMarker(new MarkerOptions()
                        .position(ContextPoint)
                        .title("Bordillo")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bordillo)));
                break;
            case R.id.item_option4:
                mMap.addMarker(new MarkerOptions()
                        .position(ContextPoint)
                        .title("Pendiente")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pendiente)));
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder myBuild = new android.support.v7.app.AlertDialog.Builder(this);
        myBuild.setMessage("¿Seguro que quieres salir de AccessStreet?");
        myBuild.setTitle("Salir de AccessStreet");
        myBuild.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                moveTaskToBack(true);
            }
        });

        myBuild.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        android.support.v7.app.AlertDialog dialog = myBuild.create();
        dialog.show();
    }

    /**
     * Este metodo lo que hace es reubicar el boton de my location y zoom
     *  y se adapta al aspect ratio de la pantalla de cada dispositivo
     */
    @SuppressLint("ResourceType")
    private void setUpMap() {
        // Find myLocationButton view
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        /*@SuppressLint("ResourceType") View myLocationButton = mapFragment.getView().findViewById(0x2);

        if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // location button is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton.getLayoutParams();

            // Align it to - parent BOTTOM|RIGHT
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170,
                    getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);

            myLocationButton.setLayoutParams(params);
        }*/

        locationButton = mapFragment.getView().findViewById(0x2);
        if(locationButton != null)
            locationButton.setVisibility(View.GONE);


        /*@SuppressLint("ResourceType") View zoomControls = mapFragment.getView().findViewById(0x1);

        if (zoomControls != null && zoomControls.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) zoomControls.getLayoutParams();

            // Align it to - parent top|left
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                    getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);
        }*/
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.setPadding(0,0,50, 140);

        // Add a marker in Spain and move the camera just an example

        GPSTracking gps = new GPSTracking(getApplicationContext());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLat(), gps.getLng()), 15));

        //Displaying the button location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }

        setUpMap(); //Placing location button
        //mMap.getUiSettings().setZoomControlsEnabled(true); //Displaying the zoom buttons
        mMap.getUiSettings().setZoomGesturesEnabled(true); //Enable zoom gestures (pinch gestures)
        mMap.setOnMapLongClickListener(this); //Callback declaration for long map click
        mMap.setOnMapClickListener(this); //Callback declaration for the simple click
        CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        mMap.setOnMarkerClickListener(customInfoWindow);
        //mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(LayoutInflater.from(this.getActivity())));
        PeticionObjetos hiloConexionTodos = new PeticionObjetos();
        hiloConexionTodos.execute(urlObjetos);
    }

    /**
     * Clase auxiliar que pedirá todos los puntos al servidor y los mostrará en el mapa
     */
    public class PeticionObjetos extends AsyncTask<String,Void,String> {

        /**
         * hace la peticion al servidor y devuelve su respuesta
         * @param url url del servidor
         * @return respuesta del servidor
         */
        @Override
        protected String doInBackground(String... url) {
            HttpHandler sh = new HttpHandler();
            String urlServer = url[0];
            return sh.makeServiceCall(urlServer);
            //return null;
        }

        /**
         * Transforma el json del servidor en puntos en el mapa
         *
         * @param jsonStr respuesta del servidor en formato json
         */
        @Override
        protected void onPostExecute(String jsonStr) {
            if (jsonStr != null) {
                try {
                    JSONArray vector = new JSONArray(jsonStr);
                    for (int i = 0; i < vector.length(); i++) {
                        JSONObject tipo = vector.getJSONObject(i);
                        String id = tipo.getString("id");
                        String name = tipo.getString("name");
                        String iconName = tipo.getString("icon");

                        ListaPuntos lista = new ListaPuntos(name);

                        JSONArray misPuntos = tipo.getJSONArray("puntos");
                        String puntosLeidos;


                        if (isShownAlert(iconName)) {
                            for (int j = 0; j < misPuntos.length(); j++) {
                                puntosLeidos = misPuntos.getString(j);
                                String[] coords = puntosLeidos.split(",");
                                Double lat = Double.parseDouble(coords[1]);
                                Double lng = Double.parseDouble(coords[0]);
                                LatLng coordenada = new LatLng(lat, lng);

                                int iconId = getResources().getIdentifier(iconName, "drawable", getPackageName());

                                if (iconId == 0) {
                                    iconId = R.drawable.user;
                                }

                                MarkerOptions mark = new MarkerOptions().position(coordenada).icon(BitmapDescriptorFactory.fromResource(iconId));
                                mark.title(name);
                                mark.snippet(name);
                                lista.puntos.add(mark);
                                mMap.addMarker(mark);
                            }
                            marcadores.add(lista);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onMapClick(LatLng destination) {
        //mMap.clear(); //Esto de momento lo dejamos para poder realizar las pruebas
        if (destino != null) destino.remove();
        destino = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(destination.latitude, destination.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        GPSTracking gps = new GPSTracking(getApplicationContext());
        System.out.println("******************** Origen --> " + gps.getLng() + ":" + gps.getLat());
        System.out.println("******************** Destino --> " + destination.longitude + ":" + destination.latitude);
        getRoute(new LatLng(gps.getLat(), gps.getLng()), destination);
    }

    public void getRoute(LatLng origin, LatLng destination) {
        System.out.println("*************** Dibujando la polilinea *******************");
        //origin = new LatLng(38.383446, -0.515578);
        if (RUTE_LINE!=null)RUTE_LINE.remove();
        Route ruta = new Route(origin, destination,mMap, getString(R.string.accesstreet_api_host) + ":" + getString(R.string.accesstreet_api_port));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    public void showMenu(View v) {
        Intent intent = new Intent(this, AddAlertActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_option1:
                Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.item_option2:
                Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.item_option3:
                Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.item_option4:
                Toast.makeText(this, item.toString(), Toast.LENGTH_LONG).show();
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            default:
                return false;
        }
    }
}
