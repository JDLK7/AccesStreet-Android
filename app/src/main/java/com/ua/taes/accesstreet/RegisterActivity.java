package com.ua.taes.accesstreet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    CardView logearse;
    CardView registrarse;
    TextView forgotPass;
    EditText nombre;
    EditText contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        logearse = (CardView)findViewById(R.id.login);
        registrarse = (CardView)findViewById(R.id.signup);
        forgotPass = (TextView)findViewById(R.id.forgot);

        logearse.setOnClickListener(this);
        registrarse.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;
        Toast toast;
        boolean errores = false;
        String nombreV,contraV;

        switch (v.getId()) {

            case (R.id.login):

                nombre = (EditText)findViewById(R.id.nombre);
                contra = (EditText)findViewById(R.id.contra);

                nombreV = nombre.getText().toString();
                contraV = contra.getText().toString();

                if(nombreV.isEmpty()) {

                    nombre.setError("Campo vacío");
                    errores = true;
                }

                if(contraV.isEmpty()) {

                    contra.setError("Campo vacío");
                    errores = true;
                }

                if(errores == false) {

                    //Creamos el Intent
                    Intent mapa = new Intent(LoginActivity.this, MapsActivity.class);

                    //Iniciamos la nueva actividad
                    startActivity(mapa);
                }

                break;

            case (R.id.signup):
                //Creamos el Intent
                Intent intent = new Intent(LoginActivity.this, RegistroUsuario.class);

                //Creamos la informacion a pasar entre actividades
                Bundle b = new Bundle();
                b.putString("NOMBRE","");

                //Añadimos la informacion al intent
                intent.putExtras(b);

                //Iniciamos la nueva actividad
                startActivity(intent);
                break;

            case (R.id.forgot):
                //Creamos el Intent
                Intent olvidar = new Intent(LoginActivity.this, OlvidarContra.class);

                //Iniciamos la nueva actividad
                startActivity(olvidar);
                break;

        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder myBuild = new AlertDialog.Builder(this);
        myBuild.setMessage("¿Seguro que quieres salir de AccessStreet?");
        myBuild.setTitle("Salir de AccessStreet");
        myBuild.setPositiveButton("SÍ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        myBuild.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog dialog = myBuild.create();
        dialog.show();
    }
}
