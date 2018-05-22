package taes.accessstreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class valorar extends AppCompatActivity {

    ImageButton megusta;
    ImageButton reportar;
    TextView puntuacion;
    TextView nombreCreador;
    TextView nombreFecha;
    TextView nombreTitulo;

    String creador;
    String fechaCreacion;
    String titulo;

    protected void getPoint() {
        int pointId = getIntent().getIntExtra("pointID", -1);

        if (pointId != -1) {
            /**
             * Se prepara el JSON de respuesta y se realiza una petición POST
             * al servidor para crear el nuevo aviso en el servicio web.
             */
            String url = "http://"
                    + getResources().getString(R.string.accesstreet_api_host) + ":"
                    + getResources().getString(R.string.accesstreet_api_port) + "/api/puntos/info/" + pointId;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject typePoint = response.getJSONObject("typepoint");
                                if (typePoint != null) {
                                    titulo = typePoint.getString("name");
                                    fechaCreacion = typePoint.getString("created_at");
                                }

                                JSONObject user = response.getJSONObject("user");
                                if (user != null) {
                                    creador = user.getString("email");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valorar);

        getPoint();
        System.out.println(titulo);

        megusta = (ImageButton)findViewById(R.id.corazon);

        reportar = (ImageButton)findViewById(R.id.imageButton2);

        nombreTitulo = (TextView)findViewById(R.id.titulo);
        nombreCreador = (TextView)findViewById(R.id.creador);
        nombreFecha = (TextView)findViewById(R.id.textView19);

        nombreTitulo.setText(titulo);
        nombreCreador.setText(creador);
        nombreFecha.setText(fechaCreacion);

        megusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                megusta.setImageResource(R.drawable.concorazon);
                puntuacion = (TextView)findViewById(R.id.gustar);
                puntuacion.setText("Le gusta a 1 personas");

            }
        });

        megusta.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                megusta.setImageResource(R.drawable.sincorazon);
                puntuacion = (TextView)findViewById(R.id.gustar);
                puntuacion.setText("Le gusta a 0 personas");

                return true;
            }
        });

    }
}
