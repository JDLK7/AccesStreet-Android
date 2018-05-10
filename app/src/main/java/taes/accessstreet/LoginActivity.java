package taes.accessstreet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.widget.CheckBox;
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

    public static final String PREFS_NAME = "Preferencias";
    CardView logearse;
    CardView registrarse;
    TextView forgotPass;
    EditText nombre;
    EditText contra;
    RequestQueue requestQueue;
    StringRequest request;
    String urlObjetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        urlObjetos = "http://"
                + getResources().getString(R.string.accesstreet_api_host) + ":"
                + getResources().getString(R.string.accesstreet_api_port) + "/api/user/";

        setContentView(R.layout.activity_login);
        requestQueue = Volley.newRequestQueue(this);

        EditText name = findViewById(R.id.nombre);
        EditText pass = findViewById(R.id.contra);
        CheckBox recuerdame = (CheckBox) findViewById(R.id.checkBox);
        //Obtenemos la preferencias para saber si hay que ir a la pantalla de configuración inicial
        SharedPreferences miPreferencia = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String namePref = miPreferencia.getString("name","").toString();
        name.setText(namePref);
        String passPref = miPreferencia.getString("pass","").toString();
        pass.setText(passPref);

        boolean recuerdamePref = miPreferencia.getBoolean("recuerdame",false);
        recuerdame.setChecked(recuerdamePref);
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

                            String aux = "http://uaccesible.francecentral.cloudapp.azure.com/api/user/";

                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                if(jsonObject.names().get(0).equals("success")) {


                                    Toast.makeText(getApplicationContext(),"SUCCESS !!",Toast.LENGTH_SHORT).show();

                                    //Obtenemos la preferencias para saber si hay que ir a la pantalla de configuración inicial
                                    SharedPreferences miPreferencia = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = miPreferencia.edit();

                                    boolean prefInit = miPreferencia.getBoolean("prefInit",false);
                                    CheckBox recuerdame = findViewById(R.id.checkBox);

                                    if(recuerdame.isChecked()){
                                        Boolean recuerdamePref = recuerdame.isChecked();
                                        editor.putBoolean("recuerdame",recuerdamePref);
                                        EditText name = findViewById(R.id.nombre);
                                        EditText pass = findViewById(R.id.contra);
                                        String namePref = name.getText().toString();
                                        String passPref = pass.getText().toString();
                                        editor.putBoolean("recuerdame",recuerdamePref);
                                        editor.putString("name",namePref);
                                        editor.putString("pass",passPref);
                                        editor.apply();
                                    }


                                    if(prefInit) {
                                        //Creamos el Intent
                                        Intent mapa = new Intent(LoginActivity.this, MapsActivity.class);

                                        //Iniciamos la nueva actividad
                                        startActivity(mapa);
                                    }
                                    else{
                                        Intent preferencias = new Intent(LoginActivity.this, PreferencesActivity.class);
                                        startActivity(preferencias);
                                    }
                                }

                                else {

                                    Toast.makeText(getApplicationContext(),"ERROR: " + jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                    urlObjetos = aux;
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

