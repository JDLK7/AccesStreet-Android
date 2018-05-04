package taes.accessstreet;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class AddAlertActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_interest_points:
                    selectedFragment = InterestPointsFragment.newInstance();
                    mTextMessage.setText("Puntos de interés");
                    break;
                case R.id.navigation_obstacles:
                    selectedFragment = ObstaclesFragment.newInstance();
                    mTextMessage.setText("Obstáculos");
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
            transaction.commit();

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, InterestPointsFragment.newInstance());
        transaction.commit();
    }

    /**
     * Devuelve el id del tipo de aviso cuyo nombre recibe como parámetro.
     *
     * @param selectedType nombre del aviso en el servicio web.
     * @return id del aviso en el servicio web.
     * @throws JSONException
     */
    protected int getAlertId(String selectedType) throws JSONException {
        /**
         * Se obtienen los tipos de avisos guardados en SharedPreferences
         */
        SharedPreferences preferences = getSharedPreferences("taes.accesstreet", Context.MODE_PRIVATE);
        String strTypes = preferences.getString("types", "0");

        JSONArray types = new JSONArray(strTypes);

        int selectedTypeId = -1;

        /**
         * Se busca el aviso seleccionado en el array de avisos disponibles y se obtiene su id.
         */
        for (int i = 0; i < types.length(); i++) {
            JSONObject type = types.getJSONObject(i);

            String currentType = type.getString("name");

            if (selectedType.equals(currentType)) {
                selectedTypeId = type.getInt("id");
                break;
            }
        }

        /**
         * Si no se ha el aviso, significaría que hay una incongruencia entre los avisos
         * definidos en el cliente y en el servidor. Se lanza una excepción.
         */
        if (selectedTypeId == -1) {
            throw new RuntimeException("El tipo de punto seleccionado no existe en el servicio web");
        }

        return selectedTypeId;
    }

    /**
     * Realiza una llamada al servicio web para crear un nuevo aviso del tipo seleccionado.
     *
     * @param v objeto de la interfaz pulsado.
     * @throws JSONException
     */
    public void createInterestPoint(View v) throws JSONException {

        String selectedType = String.valueOf(v.getTag());
        int selectedTypeId = getAlertId(selectedType);

        /**
         * Se prepara el JSON de respuesta y se realiza una petición POST
         * al servidor para crear el nuevo aviso en el servicio web.
         */
        String url = "http://"
                + getResources().getString(R.string.accesstreet_api_host) + ":"
                + getResources().getString(R.string.accesstreet_api_port) + "/api/puntos";

        JSONObject payload = new JSONObject();
        payload.put("lat", 0);
        payload.put("lng", 0);
        payload.put("type", selectedTypeId);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, url, payload, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.message_alert_created, Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

        AccesstreetRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
