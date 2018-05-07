package taes.accessstreet;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    CardView register;
    EditText contrasenya;
    EditText repetir;
    EditText nombre;
    EditText email;
    RequestQueue requestQueue;
    StringRequest request;
    String urlObjetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlObjetos = "http://"
                + getResources().getString(R.string.accesstreet_api_host) + ":"
                + getResources().getString(R.string.accesstreet_api_port) + "/api/user/register/";

        setContentView(R.layout.content_register);

        register = (CardView)findViewById(R.id.registrarse);

        register.setOnClickListener(this);
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
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

                    urlObjetos += nombre.getText().toString() + "-" + email.getText().toString() + "-" + contrasenya.getText().toString();

                    request = new StringRequest(Request.Method.POST, urlObjetos, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonObject = new JSONObject(response);

                                //.out.println(response);


                                if(jsonObject.names().get(0).equals("success")) {


                                    Toast.makeText(getApplicationContext(),"SUCCESS " + jsonObject.getString("success"),Toast.LENGTH_SHORT).show();

                                    //Creamos el Intent
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                    //Iniciamos la nueva actividad
                                    startActivity(intent);

                                }

                                else {

                                    // Toast.makeText(getApplicationContext(),"ERROR: " + jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String,String> hashMap = new HashMap<String,String>();

                            hashMap.put("name",nombre.getText().toString());
                            hashMap.put("email",email.getText().toString());
                            hashMap.put("password",contrasenya.getText().toString());

                            return hashMap;
                        }
                    };

                    requestQueue.add(request);

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
