package taes.accessstreet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class valorar extends AppCompatActivity {

    ImageButton megusta;
    ImageButton reportar;
    TextView puntuacion;
    TextView nombreCreador;
    TextView nombreFecha;
    TextView nombreTitulo;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valorar);


        String valor = getIntent().getStringExtra("resultado");
        System.out.println(valor);

        String[] split = valor.split("/");

        megusta = (ImageButton)findViewById(R.id.corazon);

        reportar = (ImageButton)findViewById(R.id.imageButton2);

        nombreTitulo = (TextView)findViewById(R.id.titulo);
        nombreCreador = (TextView)findViewById(R.id.creador);
        nombreFecha = (TextView)findViewById(R.id.textView19);

        nombreTitulo.setText(split[0]);
        nombreCreador.setText(split[2]);
        nombreFecha.setText(split[1]);

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
