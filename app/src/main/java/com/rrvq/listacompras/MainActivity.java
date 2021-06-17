package com.rrvq.listacompras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.rrvq.listacompras.Ajustes.MainActivity_Ajustes;
import com.rrvq.listacompras.AmigosDraw.MainActivity_AmigosDraw;
import com.rrvq.listacompras.listas.AdapterListas;
import com.rrvq.listacompras.listas.Listas;
import com.rrvq.listacompras.productos.ActivityProductos;
import com.rrvq.listacompras.sesion.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //vista en general
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    FloatingActionButton btnFlotante;
//    ProgressBar progressBar;
    ProgressDialog progressDialog;
    AdView mAdView;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    AdapterListas adapterListas;
    ArrayList<Listas> data = new ArrayList<>();

    Cursor fila;
    //declarar globar para recibir de inicio de session o de donde se guardo para mantener activa la session
    String id_usuario, email, nombre, apellido;

    TextView tvnombre, tvemail;

    //para eliminar los botones del drawer menu
    MenuItem itemPremium;

    View view;

    LinearLayout linearImgAdd;
    InterstitialAd mInterstitialAd;

//    int p = 100;
//    int incr = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        castingView();
        toolbarMenu();
        datosSqlite();

        verificar();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.cargando));
        progressDialog.show();


        obtenerLista();
        btnFlotanteListener();
        refrescarRecycler();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Constantes.INTERSTICIAL);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    public void castingView(){
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.vistaNavegacion);
        btnFlotante = findViewById(R.id.btnFlotante);
        // este es el progress bar de prueba
//        progressBar = findViewById(R.id.pb);
        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.refreshRecycler);

        //recibo los texview del draw para poder editarlos
        tvnombre = navigationView.getHeaderView(0).findViewById(R.id.tvnombre);
        tvemail = navigationView.getHeaderView(0).findViewById(R.id.tvemail);


        itemPremium = navigationView.getMenu().findItem(R.id.itemPremium);
        // para ver o no los botones se declaro global y se usa donde sea
//        itemPremium.setVisible(false);

        view = findViewById(R.id.activity_main);

        linearImgAdd = findViewById(R.id.linearImgAdd);

        mAdView = findViewById(R.id.adView);



    }

    public void refrescarRecycler() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                obtenerLista();

                //swipe tiempo de demora del circulo pprogreso
//                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void setRecyclerView(){
        //----------------para el recyclervie
        /*data.add(new Listas("1", "Casa", "5/8", "30"));
        data.add(new Listas("2", "1", "5/8", "10"));*/

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterListas = new AdapterListas(this, data);


        adapterListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //para usar solo listas y referenciasr con get listas.getIdLista() recibo de listas
                Listas listas = data.get(recyclerView.getChildAdapterPosition(v));

                /*Toast.makeText(getApplicationContext(), "Seleccion: "+
                        data.get(recyclerView.getChildAdapterPosition(v)).getNombreLista()+
                        " ID: "+data.get(recyclerView.getChildAdapterPosition(v)).getIdLista()+
                        " IDUSUARIO: "+data.get(recyclerView.getChildAdapterPosition(v)).getId_usuario(), Toast.LENGTH_SHORT).show();*/

                Intent intent = new Intent(getApplicationContext(), ActivityProductos.class);
                String id = data.get(recyclerView.getChildAdapterPosition(v)).getIdLista();
                String nombre = data.get(recyclerView.getChildAdapterPosition(v)).getNombreLista();
                intent.putExtra("id_lista", id);
                intent.putExtra("nombre_lista", nombre);
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(adapterListas);


        progressDialog.dismiss();
    }

    public void obtenerLista(){

        // enviar el id del usuario para recuperar solo sus listas y las listas que le comparten
        // modificar el scrip para que solo busque los id del usuario

        String url = getResources().getString(R.string.urlBuscarLista)+"?id_usuario="+id_usuario+"&email_usu="+email+"";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

//                String resString = response.toString();

                JSONObject jsonObject = null;
                data.clear();

                for (int i = 0; i < response.length(); i++) {


                    try {

                        jsonObject = response.getJSONObject(i);

                        if (jsonObject.getString("id_lista").equalsIgnoreCase("cerrar sesion")){

                            //para cerrar sesion en caso de que tester todavia tenga guardado mysqlite los datos
                            //Conexion a la base de datos SQLITE
                            AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(MainActivity.this, "BDListas", null, 1);
                            SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();


                            baseDeDatos.delete("sesion", "rowid="+ 1,null);
                            baseDeDatos.close();

                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.actualizarApp), Toast.LENGTH_SHORT).show();

                        }else

                        if (jsonObject.getString("id_lista").equalsIgnoreCase("No hay registros")) {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sinlistas), Toast.LENGTH_SHORT).show();

                            linearImgAdd.setVisibility(View.VISIBLE);

                        } else {


                            data.add(new Listas(
                                    jsonObject.getString("id_lista"),
                                    jsonObject.getString("nombre_lis"),
                                    jsonObject.getString("id_usuario"),
                                    jsonObject.getString("total"),
                                    jsonObject.getString("check"),
                                    jsonObject.getString("cantida_amigos")

                            ));

                            linearImgAdd.setVisibility(View.INVISIBLE);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
//                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        swipeRefreshLayout.setRefreshing(false);
                    }


                }

                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);

//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();

                setRecyclerView();

                //colocar aqui para que no de error cuando no tiene inter
                adapterListas.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // esto se puede dar mensaje de error de conexion
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void btnFlotanteListener(){

        // onclick del boton flotente
        btnFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getApplicationContext(), "Boton flotante", Toast.LENGTH_SHORT).show();

                //*********************** DIALOGO   *********************************/
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

//                LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                dialog.setTitle(getResources().getString(R.string.agregarLista));
//                dialog.setCancelable(false);

//                dialog.setView(R.layout.dialog_edittext);
//                dialog.setView(inflater.inflate(R.layout.dialog_edittext, null));

                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_edittext, null);
                dialog.setView(dialogView);
                final EditText etDialog = dialogView.findViewById(R.id.etDialog);


                //para el bootn aceptar del dialogo
                dialog.setPositiveButton(getResources().getString(R.string.agregar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo, int id) {

                        String etnombre = etDialog.getText().toString().trim();

                        if (!etnombre.isEmpty()){

                            progressDialog.show();
                            guardarLista(etnombre);

                        }else {
                            etDialog.setError(getResources().getString(R.string.nombre));
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
        });
    }

    private void guardarLista(final String etnombre) {

        String url = getResources().getString(R.string.urlAgregarLista);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Registrado")) {


                    obtenerLista();
                    progressDialog.dismiss();

                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("nombre_lis", etnombre);
                parametros.put("id_usuario", id_usuario);


                return parametros;
            }
        };

        // para cuando hago peticiones con requesstring solamente ************
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void datosSqlite(){
        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        fila = baseDeDatos.rawQuery("SELECT id_usuario, email_usu, nombre_usu, apellido_usu FROM sesion WHERE rowid="+1, null);
        if (fila.moveToFirst()) {
            id_usuario = fila.getString(0);
            email = fila.getString(1);
            nombre = fila.getString(2);
            apellido = fila.getString(3);

            tvnombre.setText(nombre + " " + apellido);
            tvemail.setText(email);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.itemListas:

                intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // con la de arriba se elimian todas todas menos la que se llamo
        //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
                startActivity(intent);
                finish();

                break;
            case R.id.itemAmigos:

                if (nombre.equals("invitado")&&apellido.equals("invitado")) {
                    DebesRegistrarteIntersticial debesRegistrarteIntersticial = new DebesRegistrarteIntersticial(this);
                    debesRegistrarteIntersticial.dialogoRegistrarce();
                }else {

                    intent = new Intent(this, MainActivity_AmigosDraw.class);
                    // con la de arriba se elimian todas todas menos la que se llamo
                    //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
                    startActivity(intent);
                }

                break;
            /*case R.id.itemInvitaciones:

                break;*/
            case R.id.itemAjustes:

                intent = new Intent(this, MainActivity_Ajustes.class);
                // con la de arriba se elimian todas todas menos la que se llamo
                //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
                startActivity(intent);

                break;
            case R.id.itemSalir:

                //Conexion a la base de datos SQLITE
                AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDListas", null, 1);
                SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();


                baseDeDatos.delete("sesion", "rowid="+ 1,null);
                baseDeDatos.close();

                intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

                break;
            case R.id.itemPremium:

                //verificar si es pro o gratis y si ya pago o no
                intent = new Intent(this, MainActivity_Premium.class);
                // con la de arriba se elimian todas todas menos la que se llamo
                //        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);  // con esta elimina solo las q estan delante de la que se llamo
                startActivity(intent);

                break;
            default:
                break;
        }
        return false;
    }

    //*********************  Toolbar y menu lateral ********************************/
    public void toolbarMenu() {
        setSupportActionBar(toolbar);
        //        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.listas) + "</font>"));

        // Toogle de hamburguesa
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.cerrar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        navigationView.setItemIconTintList(null);  // para que los iconos tengan su color original

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

                //para quitar el boton del menu lateral de premium
                itemPremium.setVisible(false);
                mAdView.setVisibility(View.GONE);

            }else {


                MobileAds.initialize(this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
//                mAdView = findViewById(R.id.adView); // se colocoen el castin xq daba error
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

//                progressTimeIntersticial();


            }

        }
        else if (sabor.equals("pro")){
            itemPremium.setVisible(false);
            mAdView.setVisibility(View.GONE);
        }

        //***************************************************************************//

    }

    public void setIntersticial() {

        Flavor flavor = new Flavor();
        String sabor = flavor.getFlavor();
        if (sabor.equals("free")) {

            // si ya pago quitar boton premium con
//            itemPremium.setVisible(false);

            String id = flavor.getId();
            Boolean b = getBoolFromPref(this, "myPref", id);

            if (b == true) {


            } else {

                Constantes constantes = new Constantes(this);
                Boolean Online = constantes.isOnline();

                if (Online) {

//            // para el intersticial
//            InterstitialAd mInterstitialAd;
//            mInterstitialAd = new InterstitialAd(this);
//            mInterstitialAd.setAdUnitId(Constantes.INTERSTICIAL);
//            mInterstitialAd.loadAd(new AdRequest.Builder().build());


                    // colocar la publicidad
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();

                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                // Load the next interstitial.

                            }

                        });

                    }

                    mInterstitialAd = new InterstitialAd(this);
                    mInterstitialAd.setAdUnitId(Constantes.INTERSTICIAL);
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());

                }

            }
        }else if (sabor.equals("pro")){

        }
    }


    //****************  PROGRESS ITERTICIAL CON TIEMPO PARA RESPONDER  ********************//

    /*public void progressTimeIntersticial(){

        Constantes constantes = new Constantes(this);
        Boolean Online = constantes.isOnline();

        if (Online) {

            // para el intersticial
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(Constantes.INTERSTICIAL);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());


            countDownTimer = new CountDownTimer(Constantes.COUNT_TIMER_INTERTICIAL, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {


                }

                @Override
                public void onFinish() {

                    // colocar la publicidad
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();

                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdClosed() {
                                // Load the next interstitial.
                                progressTimeIntersticial();

                            }

                        });

                    }

                }
            }.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        // para cuadno cierre o pause el app
        countDownTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // para cuadno cierre o pause el app
        countDownTimer.cancel();
    }*/



}
