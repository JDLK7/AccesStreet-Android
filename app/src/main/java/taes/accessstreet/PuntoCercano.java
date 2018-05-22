package taes.accessstreet;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class PuntoCercano extends AsyncTask<String,Void,String> {
    private String url;
    private double latitud;
    private double longitud;
    private GoogleMap mMap;
    private String prefs;
    private int tol;
    private String urlBase;
    //http://uaccesible.francecentral.cloudapp.azure.com/miprueba2_-0.513225_38.385225_0
    public PuntoCercano(double latitud, double longitud, String tipo, String urlBase, GoogleMap mMap, String prefs, int tol) {
        this.url = "http://" + urlBase + "/miprueba" + tipo + "_" + longitud + "_" + latitud + "_0";
        this.longitud = longitud;
        this.latitud = latitud;
        this.prefs = prefs;
        this.tol = tol;
        this.mMap = mMap;
        this.urlBase = urlBase;
        this.getResponse();
    }

    public void getResponse() {
        this.execute(url);
    }

    @Override
    protected String doInBackground(String... url) {
        HttpHandler sh = new HttpHandler();
        String urlServer = url[0];
        return sh.makeServiceCall(urlServer);
    }

    @Override
    protected void onPostExecute(String jsonStr) {
        System.out.println(jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonResponse = new JSONObject(jsonStr);
                Double longitud_aux = jsonResponse.getDouble("x");
                Double latitud_aux = jsonResponse.getDouble("y");
                LatLng origen = new LatLng(this.latitud, this.longitud);
                LatLng destino = new LatLng(latitud_aux,longitud_aux);
                Route ruta = new Route(origen, destino,mMap, urlBase,prefs,tol);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
