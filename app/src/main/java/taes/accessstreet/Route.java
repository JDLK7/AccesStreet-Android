package taes.accessstreet;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class Route extends AsyncTask<String,Void,String> {

    private LatLng origen, destino;
    private String url;
    final String urlBase = "/api/rutaAux";
    ArrayList<String> coordenadas = new ArrayList<>();
    private GoogleMap mMap;

    //$x1=-0.51163670,38.38183460,0,-0.51799090,38.38634660,0;
    public Route(LatLng origen, LatLng destino, GoogleMap mMap, String url) {
        this.origen = origen;
        this.destino = destino;
        //this.url = "http://uaccesible.francecentral.cloudapp.azure.com:80" + urlBase + origen.longitude + "," + origen.latitude + ",0," + destino.longitude + "," + destino.latitude + ",0";
        this.url = "http://" + url + urlBase + origen.longitude + "," + origen.latitude + ",0," + destino.longitude + "," + destino.latitude + ",0";
        this.mMap = mMap;
        this.getResponse();
    }


    public void getResponse() {
        this.execute(url);
    }

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
    }

    /**
     * Transforma el json del servidor en puntos en el mapa
     *
     * @param jsonStr respuesta del servidor en formato json
     */
    @Override
    protected void onPostExecute(String jsonStr) {
        System.out.println(jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonResponse = new JSONObject(jsonStr);
                JSONArray jsonArray = jsonResponse.getJSONArray("Ruta");
                for(int i = 0; i < jsonArray.length(); i++) {
                    splitCoordenadas(jsonArray.getString(i));
                }

                PolylineOptions rectOptions = new PolylineOptions()
                        .width(10)
                        .color(Color.RED);

                rectOptions.add(origen);
                for (int i = 0; i <= coordenadas.size()-3; i++) {
                        System.out.println("Latitud " + coordenadas.get(i+1) + " Longitud " + coordenadas.get(i));
                        rectOptions.add(new LatLng(Double.parseDouble(coordenadas.get(i+1)), Double.parseDouble(coordenadas.get(i))));
                        i = i+2;
                }
                rectOptions.add(destino);

                //Polyline polyline = mMap.addPolyline(rectOptions);
                MapsActivity.RUTE_LINE = mMap.addPolyline(rectOptions);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void splitCoordenadas(String s) {
        System.out.println(s);
        String [] strs = s.split (",");
        coordenadas.add(strs[0]);
        coordenadas.add(strs[1]);
        coordenadas.add(strs[2]);
    }

    public ArrayList<String> getCoordenadas() {
        return this.coordenadas;
    }
}
