package taes.accessstreet;

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

        a = (TextView) findViewById(R.id.textView1);
        b = (TextView) findViewById(R.id.textView2);
        c = (TextView) findViewById(R.id.textView3);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Reporte enviado correctamente", Toast.LENGTH_LONG).show();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Reporte enviado correctamente", Toast.LENGTH_LONG).show();
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Reporte enviado correctamente", Toast.LENGTH_LONG).show();
            }
        });
    }
}
