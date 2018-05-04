package taes.accessstreet;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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

    public void createInterestPoint(View v) {
        final Intent intent = new Intent(this, MapsActivity.class);

        int type = 0;

        switch (v.getId()) {
            case R.id.accessiblePark: type = 2;
                break;
            case R.id.accessibleWC: type = 3;
                break;
            case R.id.accessibleStair: type = 5;
                break;
            case R.id.accessibleSlope: type = 6;
                break;
        }

        String url = "http://"
                + getResources().getString(R.string.accesstreet_api_host) + ":"
                + getResources().getString(R.string.accesstreet_api_port) + "/api/puntos";

        JSONObject payload = new JSONObject();

        try {
            payload.put("lat", 0);
            payload.put("lng", 0);
            payload.put("type", type);
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, url, payload, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
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
