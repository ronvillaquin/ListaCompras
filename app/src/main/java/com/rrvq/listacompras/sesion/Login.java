package com.rrvq.listacompras.sesion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.MainActivity;
import com.rrvq.listacompras.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextInputLayout etEmailL, etPassL;
    TextInputEditText etEmail, etPass;
    TextView tvOlvidoPass, tvRegistrar;
    Button btnIniciar, btnOmitir;

    ProgressDialog progressDialog;

    String id_usuario="", email_usu="", nombre_usu="", apellido_usu="", pais="otro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //        para recuperar el pais
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        pais = tm.getNetworkCountryIso();

        castingView();
        verificarSQlite();
        progressDialog = new ProgressDialog(this);

        // lo primero que debe revisar es la sqlite para ver si ya tiene un usuario activo

        tvOlvidoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Recuperar.class);
                startActivity(intent);

            }
        });

        tvRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Registrar.class);
                startActivity(intent);

            }
        });

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etEmail.getText().toString().isEmpty()){
                    etEmailL.setError(getResources().getString(R.string.requerido));
                }else
                if (etPass.getText().toString().isEmpty()){
                    etPassL.setError(getResources().getString(R.string.requerido));
                }

                else
                    if (!etPass.getText().toString().isEmpty()){

                        progressDialog.setMessage(getResources().getString(R.string.cargando));
                        progressDialog.show();
                        obtenerDatos();

//                        evitarInyeccionSQL(etPass.getText().toString());

                    }


                /*if (!etPass.getText().toString().isEmpty()){

                    String pass1 = etPass.getText().toString();
                    char comillas = '"';

                        int aux = 0;
                        String c="";
                        for (int i=0; i<pass1.length(); i++){
                            if ("'".charAt(0) == pass1.charAt(i) || "?".charAt(0) == pass1.charAt(i) ||
                                    "¿".charAt(0) == pass1.charAt(i) || "$".charAt(0) == pass1.charAt(i) ||
                                    ";".charAt(0) == pass1.charAt(i) || "-".charAt(0) == pass1.charAt(i) ||
                                    "&".charAt(0) == pass1.charAt(i) || "<".charAt(0) == pass1.charAt(i) ||
                                    ">".charAt(0) == pass1.charAt(i) || comillas == pass1.charAt(i) ||
                                    "+".charAt(0) == pass1.charAt(i) || "%".charAt(0) == pass1.charAt(i) ||
                                    "/".charAt(0) == pass1.charAt(i) || "\\n".charAt(0) == pass1.charAt(i) ||
                                    " ".charAt(0) == pass1.charAt(i)){

                                aux = 1;
                                c = ""+pass1.charAt(i);
                            }
                        }
                        if (aux == 1){
                            Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.noseaceptancaracteres)+" "+c, Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.setMessage(getResources().getString(R.string.cargando));
                            progressDialog.show();
                            obtenerDatos();
                        }


                }*/

            }
        });

        btnOmitir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOmitir.setEnabled(false);
                progressDialog.setMessage(getResources().getString(R.string.cargando));
                progressDialog.show();
                registrarUsuario();
            }
        });


    }

    public void registrarUsuario(){

        id_usuario="";
        email_usu = "invitado@invitado.com";
        nombre_usu="invitado";
        apellido_usu = "invitado";
        final String pass = "invitado";
        final String pregunta1 = "invitado";
        final String respuesta1 = "invitado";
        final String pregunta2 = "invitado";
        final String respuesta2 = "invitado";

        String url = getResources().getString(R.string.urlRegistrarOmitir);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // formateo los espacios de inicio y fin de los echo o el json que resivo
                String respons = response.trim();

                if (respons.equalsIgnoreCase("No hay registros")){

                    Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.verifiqueusuarioycontrasena), Toast.LENGTH_SHORT).show();

//                    Snackbar.make(findViewById(R.id.activity_login), ""+getResources().getString(R.string.verifiqueusuarioycontrasena), Snackbar.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }else {


                    String stringObject = "";

                    for (int i=0; i<respons.length(); i++){

                        if (respons.charAt(i) == "[".charAt(0) || respons.charAt(i) == "]".charAt(0)){

                        }else {
                            stringObject = stringObject + respons.charAt(i);
                        }
                    }

                    try {

//                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonObject = new JSONObject(stringObject);

                        id_usuario = jsonObject.getString("id_usuario");
                        email_usu = jsonObject.getString("email_usu");
                        nombre_usu = jsonObject.getString("nombre_usu");
                        apellido_usu = jsonObject.getString("apellido_usu");


                        verificarSQlite();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("email_usu", email_usu.toLowerCase().replace(" ",""));
                parametros.put("pass_usu", pass.replace(" ",""));
                parametros.put("nombre_usu", nombre_usu.trim());
                parametros.put("apellido_usu", apellido_usu.trim());
                parametros.put("pregunta1", pregunta1.trim());
                parametros.put("respuesta1", respuesta1.trim()); //lover minisculas trin espacio inio final
                parametros.put("pregunta2", pregunta2.trim());
                parametros.put("respuesta2", respuesta2.trim());
                parametros.put("pais", pais);


                return parametros;
            }
        };

        // para que olo haga una peticion y no se envien varias
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        btnOmitir.setEnabled(true);
    }

    private void evitarInyeccionSQL(String pass1) {

            char comillas = '"';

            int aux = 0;
            String c="";
            for (int i=0; i<pass1.length(); i++){
                if ("'".charAt(0) == pass1.charAt(i) || "?".charAt(0) == pass1.charAt(i) ||
                        "¿".charAt(0) == pass1.charAt(i) || "$".charAt(0) == pass1.charAt(i) ||
                        ";".charAt(0) == pass1.charAt(i) || "-".charAt(0) == pass1.charAt(i) ||
                        "&".charAt(0) == pass1.charAt(i) || "<".charAt(0) == pass1.charAt(i) ||
                        ">".charAt(0) == pass1.charAt(i) || comillas == pass1.charAt(i) ||
                        "+".charAt(0) == pass1.charAt(i) || "%".charAt(0) == pass1.charAt(i) ||
                        "/".charAt(0) == pass1.charAt(i) || "\\n".charAt(0) == pass1.charAt(i) ||
                        " ".charAt(0) == pass1.charAt(i)){

                    aux = 1;
                    c = ""+pass1.charAt(i);
                }
            }
            if (aux == 1){
                Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.noseaceptancaracteres)+" "+c, Toast.LENGTH_SHORT).show();
            }else {
                progressDialog.setMessage(getResources().getString(R.string.cargando));
                progressDialog.show();
                obtenerDatos();
            }


    }

    public void castingView(){

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        etEmailL = findViewById(R.id.etEmailL);
        etPassL = findViewById(R.id.etPassL);

        tvOlvidoPass = findViewById(R.id.tvOlvidoPass);
        tvRegistrar = findViewById(R.id.tvRegistrar);

        btnIniciar = findViewById(R.id.btnIniciar);
        btnOmitir = findViewById(R.id.btnOmitir);
    }

    public void obtenerDatos(){

        String url = getResources().getString(R.string.urlInicioSesion);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // formateo los espacios de inicio y fin de los echo o el json que resivo
                String respons = response.trim();

                if (respons.equalsIgnoreCase("No hay registros")){

                    Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.verifiqueusuarioycontrasena), Toast.LENGTH_SHORT).show();

//                    Snackbar.make(findViewById(R.id.activity_login), ""+getResources().getString(R.string.verifiqueusuarioycontrasena), Snackbar.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }else {


                    String stringObject = "";

                    for (int i=0; i<respons.length(); i++){

                        if (respons.charAt(i) == "[".charAt(0) || respons.charAt(i) == "]".charAt(0)){

                        }else {
                            stringObject = stringObject + respons.charAt(i);
                        }
                    }

                    try {

//                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonObject = new JSONObject(stringObject);

                        id_usuario = jsonObject.getString("id_usuario");
                        email_usu = jsonObject.getString("email_usu");
                        nombre_usu = jsonObject.getString("nombre_usu");
                        apellido_usu = jsonObject.getString("apellido_usu");


                        verificarSQlite();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                        progressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();

                        Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();
                    }
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();


                parametros.put("email_usu", etEmail.getText().toString().toLowerCase().replace(" ",""));
                parametros.put("pass_usu", etPass.getText().toString().replace(" ",""));


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void verificarSQlite(){

        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(getApplicationContext(), "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT * FROM sesion WHERE rowid="+1, null);
        if (fila.moveToFirst()) {

            if (!id_usuario.isEmpty()) {

                ContentValues addUsuario = new ContentValues();
                addUsuario.put("id_usuario", id_usuario);
                addUsuario.put("email_usu", email_usu);
                addUsuario.put("nombre_usu", nombre_usu);
                addUsuario.put("apellido_usu", apellido_usu);

                baseDeDatos.update("sesion", addUsuario, "rowid="+1, null);
            }else {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        }else {

            if (!id_usuario.isEmpty()){

                ContentValues addUsuario = new ContentValues();
                addUsuario.put("id_usuario", id_usuario);
                addUsuario.put("email_usu", email_usu);
                addUsuario.put("nombre_usu", nombre_usu);
                addUsuario.put("apellido_usu", apellido_usu);

                baseDeDatos.insert("sesion", null, addUsuario);
            }

        }

        baseDeDatos.close();
    }


}
