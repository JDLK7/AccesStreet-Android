package taes.accessstreet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity {

    CardView logearse;
    CardView registrarse;
    TextView forgotPass;
    EditText nombre;
    EditText contra;
    RequestQueue requestQueue;
    StringRequest request;
    String urlObjetos = "http://uaccesible.francecentral.cloudapp.azure.com/api/user/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);
    }

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

                    urlObjetos += nombre.getText().toString() + "-" + contra.getText().toString();

                    request = new StringRequest(Request.Method.POST, urlObjetos, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            //System.out.println(response);

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.names().get(0).equals("success")) {


                                    Toast.makeText(getApplicationContext(),"SUCCESS " + jsonObject.getString("success"),Toast.LENGTH_SHORT).show();

                                    //Creamos el Intent
                                    Intent mapa = new Intent(LoginActivity.this, MainActivity.class);

                                    //Iniciamos la nueva actividad
                                    startActivity(mapa);

                                }

                                else {

                                    Toast.makeText(getApplicationContext(),"Inutil", Toast.LENGTH_SHORT).show();
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
                            hashMap.put("password",contra.getText().toString());

                            return hashMap;
                        }
                    };

                    requestQueue.add(request);

                }

                break;

            case (R.id.signup):
                //Creamos el Intent
                Intent intent = new Intent(this, RegisterActivity.class);

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
                Intent olvidar = new Intent(this, PasswordResetActivity.class);

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

