package com.rrvq.listacompras.Ajustes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.Flavor;
import com.rrvq.listacompras.R;
import com.rrvq.listacompras.sesion.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_RecuperarPass extends AppCompatActivity {

    Toolbar toolbar;

    TextInputLayout etEmailL, etRespuesta1L,etRespuesta2L,etPassL,etPass2L;
    TextInputEditText etEmail, etPregunta1, etPregunta2, etRespuesta1, etRespuesta2, etPass, etPass2;

    Button btnBuscar, btnComprobar, btnGuardar;

    ProgressDialog progressDialog;

    LinearLayout linearEmail, linearPreguntas, linearPass;

    String id_usuario;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__recuperar_pass);

        castinView();
        flechaBlanca();

        verificar();
        progressDialog = new ProgressDialog(this);

        setBtnBuscar();
        setBtnComprobar();

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etPass.getText().toString().isEmpty() && !etPass2.getText().toString().isEmpty()) {
                    String pass1 = etPass.getText().toString();
                    String pass2 = etPass2.getText().toString();
                    char comillas = '"';

                    if ((pass1.equals(pass2)) && pass1.length() >= 6){

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
                            setBtnGuardar();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.verificarpass), Toast.LENGTH_SHORT).show();

                    }

                }else {
                    if (etPass.getText().toString().isEmpty()){
                        etPassL.setError(getResources().getString(R.string.requerido));
                    }else if (etPass2.getText().toString().isEmpty()){
                        etPass2L.setError(getResources().getString(R.string.requerido));
                    }
                }


            }
        });

    }

    public void castinView(){

        toolbar = findViewById(R.id.toolbar);

        etEmail = findViewById(R.id.etEmail);
        etPregunta1 = findViewById(R.id.etPregunta1);
        etPregunta2 = findViewById(R.id.etPregunta2);
        etRespuesta1 = findViewById(R.id.etRespuesta1);
        etRespuesta2 = findViewById(R.id.etRespuesta2);
        etPass = findViewById(R.id.etPass);
        etPass2 = findViewById(R.id.etPass2);
        etEmailL = findViewById(R.id.etEmailL);
        etRespuesta1L = findViewById(R.id.etRespuesta1L);
        etRespuesta2L = findViewById(R.id.etRespuesta2L);
        etPassL = findViewById(R.id.etPassL);
        etPass2L = findViewById(R.id.etPass2L);

        linearEmail = findViewById(R.id.linearEmail);
        linearPreguntas = findViewById(R.id.linearPreguntas);
        linearPass = findViewById(R.id.linearPass);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnComprobar = findViewById(R.id.btnComprobar);
        btnGuardar = findViewById(R.id.btnGuardar);

        mAdView = findViewById(R.id.adView);

    }

    public void flechaBlanca() {

        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.cambiarPass) + "</font>"));

        // pra colocar la flecha de color blanco de volver
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // flecha de volver atras
    }

    public void setBtnBuscar() {

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enviar el id del usuario para recuperar solo sus listas y las listas que le comparten
                // modificar el scrip para que solo busque los id del usuario

                if (!etEmail.getText().toString().isEmpty()) {

                    progressDialog.setMessage(getResources().getString(R.string.cargando));
                    progressDialog.show();

                    String url = getResources().getString(R.string.urlRecuperarPass)+"?email_usu="+etEmail.getText().toString();

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            //                String resString = response.toString();

                            JSONObject jsonObject = null;

                            for (int i = 0; i < response.length(); i++) {


                                try {

                                    jsonObject = response.getJSONObject(i);

                                    if (jsonObject.getString("id_usuario").equalsIgnoreCase("No hay registros")) {

                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailinvalido), Toast.LENGTH_SHORT).show();

                                    } else {

                                        linearEmail.setVisibility(View.GONE);
                                        linearPreguntas.setVisibility(View.VISIBLE);

                                        id_usuario = jsonObject.getString("id_usuario");
                                        etPregunta1.setText(jsonObject.getString("pregunta1") + " ?");
                                        etPregunta2.setText(jsonObject.getString("pregunta2") + " ?");

                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    //                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }


                            }

                            progressDialog.dismiss();

                            //                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // esto se puede dar mensaje de error de conexion
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();
                            //                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }
                    }); /*{

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> parametros = new HashMap<String, String>();

                            parametros.put("email_usu", etEmail.getText().toString().trim());


                            return parametros;
                        }
                    };*/

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(jsonArrayRequest);

                } else {
                    etEmailL.setError(getResources().getString(R.string.requerido));
                }
            }
        });
    }

    public void setBtnComprobar() {

        btnComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // enviar el id del usuario para recuperar solo sus listas y las listas que le comparten
                // modificar el scrip para que solo busque los id del usuario

                if (!etRespuesta1.getText().toString().isEmpty() && !etRespuesta2.getText().toString().isEmpty()) {

                    progressDialog.setMessage(getResources().getString(R.string.cargando));
                    progressDialog.show();

                    String url = getResources().getString(R.string.urlRecuperarPass);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equalsIgnoreCase("si")){

                                linearPreguntas.setVisibility(View.GONE);
                                linearPass.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.respuestasnocoinciden), Toast.LENGTH_SHORT).show();

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

                            parametros.put("id_usuario", id_usuario);
                            parametros.put("respuesta1", etRespuesta1.getText().toString());
                            parametros.put("respuesta2", etRespuesta2.getText().toString());


                            return parametros;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                } else {
                    if (etRespuesta1.getText().toString().isEmpty()){
                        etRespuesta1L.setError(getResources().getString(R.string.requerido));
                    }else if (etRespuesta2.getText().toString().isEmpty()){
                        etRespuesta2L.setError(getResources().getString(R.string.requerido));
                    }
                }
            }

        });

    }

    public void setBtnGuardar() {

        String url = getResources().getString(R.string.urlRecuperarPass);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("si")){


                    progressDialog.dismiss();
                    /*Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();*/
                    guardaSalir();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.passcambiado), Toast.LENGTH_SHORT).show();


                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();

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

                parametros.put("id_usuario", id_usuario);
                parametros.put("pass_usu", etPass.getText().toString().replace(" ",""));


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void guardaSalir(){
        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();


        baseDeDatos.delete("sesion", "rowid="+ 1,null);
        baseDeDatos.close();

        Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //************************  Para verificar si ya la compro o no ***************//
    private Boolean getBoolFromPref(Context context, String prefName, String constantName){

        SharedPreferences pref = context.getSharedPreferences(prefName, 0);
        return pref.getBoolean(constantName, false);

    }

    public void verificar(){

        //******************** Verificar la compra para mostrar anuncios   **********************//
        // se verifica desde una clase aparte en los dosflavors
        Flavor flavor = new Flavor();
        String sabor = flavor.getFlavor();
        if(sabor.equals("free")){

            // si ya pago quitar boton premium con
//            itemPremium.setVisible(false);

            String id = flavor.getId();
            Boolean b = getBoolFromPref(this, "myPref", id);

            if (b == true){
                mAdView.setVisibility(View.GONE);

            }else {

                MobileAds.initialize(this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
//                mAdView = findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

                //para quitar el boton del menu lateral de premium
//                itemPremium.setVisible(false);

            }

        }else if (sabor.equals("pro")){
//            itemPremium.setVisible(false);
            mAdView.setVisibility(View.GONE);
        }

        //***************************************************************************//

    }

    @Override
    public void onBackPressed() {
        finish();
        /*Intent intent = new Intent(this, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // con la de arriba se elimian todas todas menos la que se llamo
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
        startActivity(intent);*/
    }


}