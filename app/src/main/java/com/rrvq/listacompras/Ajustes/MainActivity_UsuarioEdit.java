package com.rrvq.listacompras.Ajustes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.rrvq.listacompras.MainActivity;
import com.rrvq.listacompras.R;
import com.rrvq.listacompras.sesion.Login;

import java.util.HashMap;
import java.util.Map;

public class MainActivity_UsuarioEdit extends AppCompatActivity {

    Toolbar toolbar;

    TextInputLayout etNombreL, etApellidoL, etPassL;
    TextInputEditText etNombre, etApellido, etPass;

    Button btnEditar;

    Cursor fila;

    String id_usuario;
    ProgressDialog progressDialog;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__usuario_edit);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.cargando));

        castinView();
        toolbarMenu();
        flechaBlanca();
        datosSqlite();
        getbtnEditar();

        verificar();


    }

    public void castinView(){

        toolbar = findViewById(R.id.toolbar);

        etNombre = findViewById(R.id.etNombre);
        etNombreL = findViewById(R.id.etNombreL);

        etApellido = findViewById(R.id.etApellido);
        etApellidoL = findViewById(R.id.etApellidoL);

        etPass = findViewById(R.id.etPass);
        etPassL = findViewById(R.id.etPassL);

        btnEditar = findViewById(R.id.btnEditar);

        mAdView = findViewById(R.id.adView);

    }

    public void toolbarMenu() {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.editar) + "</font>"));
    }
    public void flechaBlanca() {

        // pra colocar la flecha de color blanco de volver
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // flecha de volver atras
    }

    public void datosSqlite(){
        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        fila = baseDeDatos.rawQuery("SELECT id_usuario, email_usu, nombre_usu, apellido_usu FROM sesion WHERE rowid="+1, null);
        if (fila.moveToFirst()) {
            id_usuario = fila.getString(0);
            String email = fila.getString(1);
            String nombre = fila.getString(2);
            String apellido = fila.getString(3);

            etNombre.setText(nombre);
            etApellido.setText(apellido);
        }

        baseDeDatos.close();
    }

    public void getbtnEditar(){

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (etNombre.getText().toString().isEmpty()){
                    etNombreL.setError(getResources().getString(R.string.requerido));
                }else
                    if (etApellido.getText().toString().isEmpty()){
                        etApellidoL.setError(getResources().getString(R.string.requerido));
                    }else {
                        dialogPass();
                    }

            }
        });

    }

    public void dialogPass(){


//                Toast.makeText(getApplicationContext(), "Boton flotante", Toast.LENGTH_SHORT).show();

        //*********************** DIALOGO   *********************************/
        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity_UsuarioEdit.this);

//                LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        dialog.setTitle(getResources().getString(R.string.confirmarPass));
//                dialog.setCancelable(false);

//                dialog.setView(R.layout.dialog_edittext);
//                dialog.setView(inflater.inflate(R.layout.dialog_edittext, null));

        LayoutInflater inflater = MainActivity_UsuarioEdit.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pass, null);
        dialog.setView(dialogView);
        final EditText etDialog = dialogView.findViewById(R.id.etPass);


        //para el bootn aceptar del dialogo
        dialog.setPositiveButton(getResources().getString(R.string.editar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

                String etPass = etDialog.getText().toString();

                if (!etPass.isEmpty()){

                    //para guardar el teclado
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etDialog.getWindowToken(), 0);

                    progressDialog.show();
                    editarDatos(etPass);

                }else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.passRequerido), Toast.LENGTH_SHORT).show();
                }


            }
        });
        // para el boton cancelar del dialogo
        dialog.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                // accion si da cancelar que no haga nada
                dialogo.cancel();

            }
        });
        dialog.show();


    }

    private void editarDatos(final String etPass) {

        String url = getResources().getString(R.string.urlEditarUsuario);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Editado")){

                    //Conexion a la base de datos SQLITE
                    AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(MainActivity_UsuarioEdit.this, "BDListas", null, 1);
                    SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

                    ContentValues datos = new ContentValues();
                    datos.put("nombre_usu", etNombre.getText().toString());
                    datos.put("apellido_usu", etApellido.getText().toString());
                    baseDeDatos.update("sesion", datos, "id_usuario=" + id_usuario, null);

                    baseDeDatos.close();


                    progressDialog.dismiss();
                    Intent intent = new Intent(MainActivity_UsuarioEdit.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    // con la de arriba se elimian todas todas menos la que se llamo
                    //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.usuarioEditado), Toast.LENGTH_SHORT).show();


                }else {
                    if (response.equalsIgnoreCase("Pass invalido")){

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.passInvalido), Toast.LENGTH_SHORT).show();
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

                parametros.put("nombre_usu", etNombre.getText().toString());
                parametros.put("apellido_usu", etApellido.getText().toString());
                parametros.put("pass_usu", etPass);
                parametros.put("id_usuario", id_usuario);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



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


}