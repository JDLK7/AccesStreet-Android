package taes.accessstreet;

import com.google.android.gms.maps.GoogleMap;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;
    private Context context;

    public CustomInfoWindowAdapter(Context c){
        this.context = c;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this.context,"Click Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getInfoContents(Marker m) {
        //Carga layout personalizado.
        View v = ((Activity)context).getLayoutInflater().inflate(R.layout.infowindow_layout, null);
        String[] info = m.getTitle().split("&");
        String url = m.getSnippet();

        TextView name_tv = v.findViewById(R.id.info_window_nombre);
        TextView details_tv = v.findViewById(R.id.info_window_desc);
        Button btn = v.findViewById(R.id.button2);

        name_tv.setText(m.getTitle());
        details_tv.setText(m.getSnippet());

        return v;
    }


    public void mostrarDatos(View v) {

    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        //Toast.makeText(this, "Close Info Window", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        return false;
    }

}
