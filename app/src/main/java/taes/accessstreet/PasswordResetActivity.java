package taes.accessstreet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

public class PasswordResetActivity extends AppCompatActivity implements View.OnClickListener {

    CardView enviar;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_password_reset);

        enviar = (CardView)findViewById(R.id.enviar);
        email = (EditText)findViewById(R.id.correo);

        enviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text;
        Toast toast;
        boolean errores = false;
        String emailV;

        switch (v.getId()) {

            case (R.id.enviar):

                emailV = email.getText().toString();

                if(emailV.isEmpty()) {

                    email.setError("Campo vacío");
                    errores = true;
                }

                if(errores == false) {

                    text = "Mensaje enviado correctamente !";
                    toast = Toast.makeText(context, text, duration);
                    toast.show();

                    //Creamos el Intent
                    Intent intent = new Intent(this, LoginActivity.class);

                    //Iniciamos la nueva actividad
                    startActivity(intent);
                }

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

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

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
