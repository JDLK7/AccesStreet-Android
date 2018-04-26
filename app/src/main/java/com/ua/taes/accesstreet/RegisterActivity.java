package com.ua.taes.accesstreet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    CardView register;
    EditText contrasenya;
    EditText repetir;
    EditText nombre;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (CardView)findViewById(R.id.registrarse);

        // register.setOnClickListener(this);
    }

    public void onClick(View v) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;
        Toast toast;
        String contraV,verificar,nombreV,emailV;
        boolean errores = false;

        nombre = (EditText)findViewById(R.id.nombre);
        email = (EditText)findViewById(R.id.email);
        contrasenya = (EditText)findViewById(R.id.contrasenya);
        repetir = (EditText)findViewById(R.id.repetirContra);

        nombreV = nombre.getText().toString();
        emailV = email.getText().toString();
        contraV = contrasenya.getText().toString();
        verificar = repetir.getText().toString();

        switch (v.getId()) {

            case (R.id.registrarse):

                if(nombreV.isEmpty()) {

                    nombre.setError("Campo vacío");
                    errores = true;
                }

                if(emailV.isEmpty()) {

                    email.setError("Campo vacío");
                    errores = true;
                }

                if(contraV.isEmpty()) {

                    contrasenya.setError("Campo vacío");
                    errores = true;
                }

                if(verificar.isEmpty()) {

                    repetir.setError("Campo vacío");
                    errores = true;
                }

                if(contraV.equals(verificar) == false) {

                    text = "ERROR: Contraseñas diferentes";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();
                    errores = true;
                }

                if(errores == false) {

                    text = "Usuario registrado exitosamente !";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();

                    //Creamos el Intent
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                    //Iniciamos la nueva actividad
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}
