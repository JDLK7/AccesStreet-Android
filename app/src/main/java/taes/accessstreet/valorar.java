package taes.accessstreet;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class valorar extends AppCompatActivity {

    TextView nombreCreador;
    TextView nombreFecha;
    TextView nombreTitulo;
    TextView nombreTipo;
    TextView nombreTolerancia;
    ImageView imagenValorar;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.valorar);


        String valor = getIntent().getStringExtra("resultado");
        System.out.println(valor);

        String[] split = valor.split("/");

        nombreTitulo = (TextView)findViewById(R.id.titulo_valorar);
        nombreCreador = (TextView)findViewById(R.id.usuario_valorar);
        nombreFecha = (TextView)findViewById(R.id.fecha_valorar);
        nombreTipo = (TextView) findViewById(R.id.tipo_valorar);
        nombreTolerancia = (TextView) findViewById(R.id.tolerancia_valorar);
        imagenValorar = (ImageView) findViewById(R.id.imagen_valorar);

        //Titulo - Fecha - Tipo - Tolerancia - Nombre

        nombreTitulo.setText(split[0]);
        nombreFecha.setText("Fecha: " + split[1]);
        int tipo = Integer.parseInt(split[2]);
        if (tipo >= 0 && tipo <= 6) {
            nombreTipo.setText("Tipo: Punto de Interes");
        }
        else {
            nombreTipo.setText("Tipo: Obstaculo");
        }
        nombreTolerancia.setText("Toleracia: " + split[3]);
        String ruta = "R.drawable." + split[4];
        switch (split[4]) {
            case "marker_accessible_park":
                imagenValorar.setImageResource(R.drawable.marker_accessible_park);
                break;
            case "marker_accessible_slope":
                imagenValorar.setImageResource(R.drawable.marker_accessible_slope);
                break;
            case "marker_accessible_stair":
                imagenValorar.setImageResource(R.drawable.marker_accessible_stair);
                break;
            case "marker_accessible_wc":
                imagenValorar.setImageResource(R.drawable.marker_accessible_wc);
                break;
            case "marker_obstacle_sidewalk":
                imagenValorar.setImageResource(R.drawable.marker_obstacle_sidewalk);
                break;
            case "marker_obstacle_slope":
                imagenValorar.setImageResource(R.drawable.marker_obstacle_slope);
                break;
            case "marker_obstacle_stair":
                imagenValorar.setImageResource(R.drawable.marker_obstacle_stair);
                break;
            case "marker_obstacle_works":
                imagenValorar.setImageResource(R.drawable.marker_obstacle_works);
                break;

        }
        //imagenValorar.setImageResource(R.drawable.marker_accessible_park);
        nombreCreador.setText("Usuario: " + split[5]);
    }
}
