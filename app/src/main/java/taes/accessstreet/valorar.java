package taes.accessstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class valorar extends AppCompatActivity {

    ImageButton boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valorar);

        boton = (ImageButton)findViewById(R.id.imageButton3);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boton.setImageResource(R.drawable.concorazon);
            }
        });

        boton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                boton.setImageResource(R.drawable.sincorazon);
                return true;
            }
        });
    }
}
