package com.rrvq.listacompras.Ajustes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.DebesRegistrarteIntersticial;
import com.rrvq.listacompras.Flavor;
import com.rrvq.listacompras.MainActivity_Politicas;
import com.rrvq.listacompras.R;

public class MainActivity_Ajustes extends AppCompatActivity {

    Toolbar toolbar;

    AdView mAdView;

    LinearLayout linearUsuario, linearPass, linearPolitica, linearEliminar;

    String nombre, apellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__ajustes);

        castinView();
        toolbarMenu();
        flechaBlanca();

        verificar();
        datosSqlite();

        btnUsuario();
        btnPass();
        btnPolitica();
//        btnEliminar();


    }

    public void castinView(){

        toolbar = findViewById(R.id.toolbar);

        linearUsuario = findViewById(R.id.linearUsuario);
        linearPass = findViewById(R.id.linearPass);
        linearPolitica = findViewById(R.id.linearPolitica);
//        linearEliminar = findViewById(R.id.linearEliminar);

        mAdView = findViewById(R.id.adView);


    }
    public void toolbarMenu() {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.ajustes) + "</font>"));
    }
    public void flechaBlanca() {

        // pra colocar la flecha de color blanco de volver
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // flecha de volver atras
    }

    public void btnUsuario(){

        linearUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombre.equals("invitado")&&apellido.equals("invitado")) {
                    DebesRegistrarteIntersticial debesRegistrarteIntersticial = new DebesRegistrarteIntersticial(MainActivity_Ajustes.this);
                    debesRegistrarteIntersticial.dialogoRegistrarce();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity_UsuarioEdit.class);
                    startActivity(intent);
                }

            }
        });

    }

    public void btnPass(){

        linearPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombre.equals("invitado")&&apellido.equals("invitado")) {
                    DebesRegistrarteIntersticial debesRegistrarteIntersticial = new DebesRegistrarteIntersticial(MainActivity_Ajustes.this);
                    debesRegistrarteIntersticial.dialogoRegistrarce();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity_RecuperarPass.class);
                    startActivity(intent);
                }

            }

        });
    }

    public void btnPolitica(){

        linearPolitica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity_Politicas.class);
                startActivity(intent);

            }
        });

    }

    public void btnEliminar(){

        linearEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

    public void datosSqlite(){
        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT nombre_usu, apellido_usu FROM sesion WHERE rowid="+1, null);
        if (fila.moveToFirst()) {
            nombre = fila.getString(0);
            apellido = fila.getString(1);

        }
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