package taes.accessstreet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class denuncias extends AppCompatActivity {

    TextView a;
    TextView b;
    TextView c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportar);

        a = (TextView)findViewById(R.id.textView1);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Reporte enviado correctamente", Toast.LENGTH_SHORT).show();

                Intent mapa = new Intent(denuncias.this, MapsActivity.class);
                startActivity(mapa);
            }
        });
    }
}
