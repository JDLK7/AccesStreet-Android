package taes.accessstreet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by J on 06/05/2018.
 */

public class OptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Button b= findViewById(R.id.aplicar);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> sel = new ArrayList<Integer>();
                Switch tg= findViewById(R.id.switch1);
                if(tg.isChecked()){
                    sel.add(1);
                }
                tg= findViewById(R.id.switch2);
                if(tg.isChecked()){
                    sel.add(2);
                }
                tg= findViewById(R.id.switch3);
                if(tg.isChecked()){
                    sel.add(3);
                }
                tg= findViewById(R.id.switch4);
                if(tg.isChecked()){
                    sel.add(4);
                }
                tg= findViewById(R.id.switch5);
                if(tg.isChecked()){
                    sel.add(5);
                }
                tg= findViewById(R.id.switch6);
                if(tg.isChecked()){
                    sel.add(6);
                }
                tg= findViewById(R.id.switch7);
                if(tg.isChecked()){
                    sel.add(7);
                }
                tg= findViewById(R.id.switch8);
                if(tg.isChecked()){
                    sel.add(8);
                }
                tg= findViewById(R.id.switch9);
                if(tg.isChecked()){
                    sel.add(9);
                }
                tg= findViewById(R.id.switch10);
                if(tg.isChecked()){
                    sel.add(10);
                }
                SeekBar s=findViewById(R.id.tolerance);
                int tol= s.getProgress();
                Intent intent=new Intent();
                intent.setClass(OptionsActivity.this, MapsActivity.class);
                intent.putIntegerArrayListExtra("sel",sel);
                intent.putExtra("tol",tol);
                startActivity(intent);
            }
        });

        b= findViewById(R.id.salir);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(OptionsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



}
