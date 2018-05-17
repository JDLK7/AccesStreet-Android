package taes.accessstreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class valorar extends AppCompatActivity {

    ImageButton megusta;
    ImageButton reportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorar);

        megusta = (ImageButton)findViewById(R.id.imageButton3);
        reportar = (ImageButton)findViewById(R.id.imageButton2);

        megusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                megusta.setImageResource(R.drawable.concorazon);
            }
        });

        megusta.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                megusta.setImageResource(R.drawable.sincorazon);
                return true;
            }
        });

        reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Creamos el Intent
                Intent mapa = new Intent(valorar.this,denuncias.class);

                //Iniciamos la nueva actividad
                startActivity(mapa);
            }
        });
    }
}
