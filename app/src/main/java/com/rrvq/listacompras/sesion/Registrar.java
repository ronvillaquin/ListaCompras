package com.rrvq.listacompras.sesion;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rrvq.listacompras.R;
import com.rrvq.listacompras.sesion.Login;

import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {

    Toolbar toolbar;
    ProgressDialog progressDialog;
    Button btnRegistrar;

    TextInputLayout etemailL, etpassL, etpass2L, etpregunta1L, etpregunta2L, etresuesta1L, etrespuesta2L, etnombreL, etapellidoL;
    TextInputEditText etemail, etpass, etpass2, etpregunta1, etpregunta2, etresuesta1, etrespuesta2, etnombre, etapellido;
    String pais="otro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        //        para recuperar el pais
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        pais = tm.getNetworkCountryIso();

        castinView();
        flechaBlanca();
        progressDialog = new ProgressDialog(this);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etemail.getText().toString();
                int auxi = 0;

                for (int i = 0; i<email.length(); i++){
                    if ("@".charAt(0) == email.charAt(i)){
                        auxi=1;
                    }
                }
                if (auxi == 0 || etemail.getText().toString().isEmpty()){
                    etemailL.setError(getResources().getString(R.string.emailinvalido));
                }else
                 if (etnombre.getText().toString().isEmpty()){
                     etnombreL.setError(getResources().getString(R.string.requerido));
                 }else
                 if (etapellido.getText().toString().isEmpty()){
                         etapellidoL.setError(getResources().getString(R.string.requerido));
                }else
                if (etpass.getText().toString().isEmpty()) {
                    etpassL.setError(getResources().getString(R.string.requerido));
                }else
                if (etpass2.getText().toString().isEmpty()) {
                    etpass2L.setError(getResources().getString(R.string.requerido));
                }else
                if (etpregunta1.getText().toString().isEmpty()) {
                    etpregunta1L.setError(getResources().getString(R.string.requerido));
                }else
                if (etresuesta1.getText().toString().isEmpty()) {
                    etresuesta1L.setError(getResources().getString(R.string.requerido));
                }else
                if (etpregunta2.getText().toString().isEmpty()) {
                    etpregunta2L.setError(getResources().getString(R.string.requerido));
                }else
                if (etrespuesta2.getText().toString().isEmpty()) {
                    etrespuesta2L.setError(getResources().getString(R.string.requerido));
                }else
                if (!etpass.getText().toString().isEmpty() && !etpass2.getText().toString().isEmpty()) {
                    String pass1 = etpass.getText().toString();
                    String pass2 = etpass2.getText().toString();
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
                            registrarUsuario();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), ""+getResources().getString(R.string.verificarpass), Toast.LENGTH_SHORT).show();

                    }

                }


            }
        });

    }

    public void castinView(){

        toolbar = findViewById(R.id.toolbar);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        etemail = findViewById(R.id.etEmail);
        etpass = findViewById(R.id.etPass);
        etpass2 = findViewById(R.id.etPass2);
        etpregunta1 = findViewById(R.id.etPregunta1);
        etpregunta2 = findViewById(R.id.etPregunta2);
        etresuesta1 = findViewById(R.id.etRespuest1);
        etrespuesta2 = findViewById(R.id.etResuesta2);
        etnombre = findViewById(R.id.etNombre);
        etapellido = findViewById(R.id.etApellido);

        etemailL = findViewById(R.id.etEmailL);
        etpassL = findViewById(R.id.etPassL);
        etpass2L = findViewById(R.id.etPass2L);
        etpregunta1L = findViewById(R.id.etPregunta1L);
        etpregunta2L = findViewById(R.id.etPregunta2L);
        etresuesta1L = findViewById(R.id.etRespuest1L);
        etrespuesta2L = findViewById(R.id.etResuesta2L);
        etnombreL = findViewById(R.id.etNombreL);
        etapellidoL = findViewById(R.id.etApellidoL);

    }

    public void registrarUsuario(){


        String url = getResources().getString(R.string.urlRegistrarUsuario);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Registrado")){


                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.usuarioregistrado), Toast.LENGTH_SHORT).show();


                }else {
                    if (response.equalsIgnoreCase("si")){

                        etemail.setError(getResources().getString(R.string.emailyaexiste));
                    }else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();

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

                parametros.put("email_usu", etemail.getText().toString().toLowerCase().replace(" ",""));
                parametros.put("pass_usu", etpass.getText().toString().replace(" ",""));
                parametros.put("nombre_usu", etnombre.getText().toString().trim());
                parametros.put("apellido_usu", etapellido.getText().toString().trim());
                parametros.put("pregunta1", etpregunta1.getText().toString().trim());
                parametros.put("respuesta1", etresuesta1.getText().toString().trim()); //lover minisculas trin espacio inio final
                parametros.put("pregunta2", etpregunta2.getText().toString().trim());
                parametros.put("respuesta2", etrespuesta2.getText().toString().trim());
                parametros.put("pais", pais);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void flechaBlanca() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // pra colocar la flecha de color blanco de volver
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // flecha de volver atras
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
