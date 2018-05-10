package taes.accessstreet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static final String PREFS_NAME = "Preferencias";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Button b= findViewById(R.id.aplicar);

        SharedPreferences miPreferencia = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Cargamos las preferencias del Primero "Semaforo Acustico"
        Switch acousticSemaphore = (Switch) findViewById(R.id.acousticSemaphore_switch);
        boolean acousticSemaphorePref = miPreferencia.getBoolean("acousticSemaphore",false);
        acousticSemaphore.setChecked(acousticSemaphorePref);

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
        boolean elevatorPref = miPreferencia.getBoolean("elevator",false);
        elevator.setChecked(elevatorPref);

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

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(OptionsActivity.this, MapsActivity.class);
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
