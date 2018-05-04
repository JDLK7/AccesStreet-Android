package taes.accessstreet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, PopupMenu.OnMenuItemClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private View mapView;
    ArrayList<LatLng> MarkerPoints;
    private LatLng ContextPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Getting map view
        mapView = mapFragment.getView();

        //Inicializar el vector de marker points
        MarkerPoints = new ArrayList<>();

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

                mMap.clear();
                LatLng coordenadas = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                mMap.addMarker(new MarkerOptions().position(coordenadas).title(place.getAddress().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(coordenadas));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.place_autocomplete_search_button:
                showMenu(v);
        }
    }

    @Override
    public void onMapLongClick(LatLng point) {
        registerForContextMenu(findViewById(R.id.map));
        openContextMenu(findViewById(R.id.map));
        ContextPoint = point;
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
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
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

        // Add a marker in Spain and move the camera
        LatLng spain = new LatLng(40, -3);
        mMap.addMarker(new MarkerOptions().position(spain).title("Marker in Spain"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(spain));

        //Displaying the button location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        } else {
            mMap.setMyLocationEnabled(true);
        }

        //Placing the button location in the right button
        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 300);

        //Displaying the zoom buttons
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Enable zoom gestures (pinch gestures)
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        //Callback declaration for long map click
        mMap.setOnMapLongClickListener(this);

        //Callback declaratio for the simple click
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng destination) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(destination.latitude, destination.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
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
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.map_menu);
        popup.show();
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
