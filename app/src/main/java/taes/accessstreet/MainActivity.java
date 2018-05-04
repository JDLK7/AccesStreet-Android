package taes.accessstreet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    /**
     * Realiza una petición al servidor para obtener los tipos de avisos disponibles
     * y los guarda en SharedPreferences para su uso posterior. Si no se obtiene
     * nada o no se puede parsear la respuesta, se guarda una cadena vacía.
     */
    protected void savePointTypes() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savePointTypes();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button wheelchair = findViewById(R.id.wheelchair_btn);

        wheelchair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
