package taes.accessstreet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by J on 06/05/2018.
 */

public class OptionsActivity extends AppCompatActivity {
    private TextView barCount;
    private int valorSeekBar;
    public static final String PREFS_NAME = "Preferencias";
    SharedPreferences miPreferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        SeekBar sb = findViewById(R.id.seekBar);
        barCount = findViewById(R.id.seekBarCount);

        miPreferencia = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                barCount.setText(String.valueOf(progress));
                valorSeekBar = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Guardamos las preferencias del Primero "Semaforo Acustico"
                SharedPreferences.Editor editor = miPreferencia.edit();
                editor.putInt("tolerancia", valorSeekBar);
                editor.apply();
            }
        });

        CardView b= findViewById(R.id.signup);

        // Cargamos las preferencias del Primero "Parking discapacitados"
        Switch accessiblePark = (Switch) findViewById(R.id.accessiblePark_switch);
        boolean accessibleParkPref = miPreferencia.getBoolean("accessiblePark",false);
        accessiblePark.setChecked(accessibleParkPref);

        // Cargamos las preferencias del Primero "Baños adaptados"
        Switch accessibleWC = (Switch) findViewById(R.id.accessibleWC_switch);
        boolean accessibleWCPref = miPreferencia.getBoolean("accessibleWC",false);
        accessibleWC.setChecked(accessibleWCPref);

        // Cargamos las preferencias del Primero "Ascensores"
        Switch elevator = (Switch) findViewById(R.id.elevator_switch);
        boolean accessibleElevatorPref = miPreferencia.getBoolean("accessibleElevator",false);
        elevator.setChecked(accessibleElevatorPref);

        // Cargamos las preferencias del Primero "Escaleras"
        Switch accessibleStair = (Switch) findViewById(R.id.accessibleStair_switch);
        boolean accessibleStairPref = miPreferencia.getBoolean("accessibleStair",false);
        accessibleStair.setChecked(accessibleStairPref);

        // Cargamos las preferencias del Primero "Rampas accesibles"
        Switch accessibleSlope = (Switch) findViewById(R.id.accessibleSlope_switch);
        boolean accessibleSlopePref = miPreferencia.getBoolean("accessibleSlope",false);
        accessibleSlope.setChecked(accessibleSlopePref);

        // Cargamos las preferencias del Primero "Rampa inaccesible"
        Switch obstacleSlope = (Switch) findViewById(R.id.obstacleSlope_switch);
        boolean obstacleSlopePref = miPreferencia.getBoolean("obstacleSlope",false);
        obstacleSlope.setChecked(obstacleSlopePref);

        // Cargamos las preferencias del Primero "Escaleras inaccesibles"
        Switch obstacleStair = (Switch) findViewById(R.id.obstacleStair_switch);
        boolean obstacleStairPref = miPreferencia.getBoolean("obstacleStair",false);
        obstacleStair.setChecked(obstacleStairPref);

        // Cargamos las preferencias del Primero "Calzada en mal estado"
        Switch obstacleSidewalk = (Switch) findViewById(R.id.obstacleSidewalk_switch);
        boolean obstacleSidewalkPref = miPreferencia.getBoolean("obstacleSidewalk",false);
        obstacleSidewalk.setChecked(obstacleSidewalkPref);

        // Cargamos las preferencias del Primero "Zona en obras"
        Switch obstacleWorks = (Switch) findViewById(R.id.obstacleWorks_switch);
        boolean obstacleWorksPref = miPreferencia.getBoolean("obstacleWorks",false);
        obstacleWorks.setChecked(obstacleWorksPref);

        sb.setProgress(miPreferencia.getInt("tolerance",0));

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences miPreferencia = getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = miPreferencia.edit();

                // Guardamos las preferencias del Primero "Parking discapacitados"
                Switch accessiblePark = (Switch) findViewById(R.id.accessiblePark_switch);
                Boolean accessibleParkPref = accessiblePark.isChecked();
                editor.putBoolean("accessiblePark",accessibleParkPref);

                // Guardamos las preferencias del Primero "Baños adaptados"
                Switch accessibleWC = (Switch) findViewById(R.id.accessibleWC_switch);
                Boolean accessibleWCPref = accessibleWC.isChecked();
                editor.putBoolean("accessibleWC",accessibleWCPref);

                // Guardamos las preferencias del Primero "Ascensores"
                Switch elevator = (Switch) findViewById(R.id.elevator_switch);
                Boolean accessibleElevatorPref = elevator.isChecked();
                editor.putBoolean("accessibleElevator",accessibleElevatorPref);

                // Guardamos las preferencias del Primero "Escaleras"
                Switch accessibleStair = (Switch) findViewById(R.id.accessibleStair_switch);
                Boolean accessibleStairPref = accessibleStair.isChecked();
                editor.putBoolean("accessibleStair",accessibleStairPref);

                // Guardamos las preferencias del Primero "Rampas accesibles"
                Switch accessibleSlope = (Switch) findViewById(R.id.accessibleSlope_switch);
                Boolean accessibleSlopePref = accessibleSlope.isChecked();
                editor.putBoolean("accessibleSlope",accessibleSlopePref);

                // Guardamos las preferencias del Primero "Rampa inaccesible"
                Switch obstacleSlope = (Switch) findViewById(R.id.obstacleSlope_switch);
                Boolean obstacleSlopePref = obstacleSlope.isChecked();
                editor.putBoolean("obstacleSlope",obstacleSlopePref);

                // Guardamos las preferencias del Primero "Escaleras inaccesibles"
                Switch obstacleStair = (Switch) findViewById(R.id.obstacleStair_switch);
                Boolean obstacleStairPref = obstacleStair.isChecked();
                editor.putBoolean("obstacleStair",obstacleStairPref);

                // Guardamos las preferencias del Primero "Calzada en mal estado"
                Switch obstacleSidewalk = (Switch) findViewById(R.id.obstacleSidewalk_switch);
                Boolean obstacleSidewalkPref = obstacleSidewalk.isChecked();
                editor.putBoolean("obstacleSidewalk",obstacleSidewalkPref);

                // Guardamos las preferencias del Primero "Zona en obras"
                Switch obstacleWorks = (Switch) findViewById(R.id.obstacleWorks_switch);
                Boolean obstacleWorksPref = obstacleWorks.isChecked();
                editor.putBoolean("obstacleWorks",obstacleWorksPref);

                SeekBar sb= (SeekBar) findViewById(R.id.seekBar);
                editor.putInt("tolerance",sb.getProgress());

                editor.putBoolean("prefInit",true);
                editor.apply();
                Intent intent=new Intent();
                intent.setClass(OptionsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        CardView c= findViewById(R.id.salirB);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(OptionsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }



}
