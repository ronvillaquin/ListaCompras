package com.rrvq.listacompras.AmigosDraw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.Flavor;
import com.rrvq.listacompras.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity_AmigosDraw extends AppCompatActivity {

    Toolbar toolbar;
    ProgressDialog progressDialog;
    LinearLayout linearLayout;

    AdView mAdView;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    AdapterAmigosDraw adapterAmigos;
    ArrayList<AmigosDraw> data = new ArrayList<>();

    String id_usuario, email_usu_creador;

    Cursor fila;

//    LinearLayout  linearBuscar;
    CardView cardView;

    EditText etBuscarAmigo;
    ImageButton ibBuscarAmigo;

    View view;

    LinearLayout linearImgAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__amigos_draw);

        castinView();
        toolbarMenu();
        flechaBlanca();
        datosSqlite();

        verificar();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.cargando));
        progressDialog.show();

        swipeRefreshLayout.setEnabled(false);


        refrescarRecycler();
        obtenerAmigos();

        buscarAmigo();


    }

    public void castinView(){

        toolbar = findViewById(R.id.toolbar);

        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.refreshRecycler);

//        linearCorreo = findViewById(R.id.linearCorreo);
//        linearBuscar = findViewById(R.id.linearBuscar);
        cardView = findViewById(R.id.cardView2);

        etBuscarAmigo = findViewById(R.id.etBuscarAmigo);
        ibBuscarAmigo = findViewById(R.id.ibBuscarAmigo);

        view = findViewById(R.id.activity_amigosDraw);

        linearImgAdd = findViewById(R.id.linearImgAdd);

        mAdView = findViewById(R.id.adView);

    }

    public void toolbarMenu() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + nombre_lista + "</font>"));
    }
    public void flechaBlanca() {

        // pra colocar la flecha de color blanco de volver
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // flecha de volver atras
    }

    public void refrescarRecycler() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                obtenerAmigos();
                adapterAmigos.notifyDataSetChanged();

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
        adapterAmigos = new AdapterAmigosDraw(this, data);


        adapterAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //para usar solo listas y referenciasr con get listas.getIdLista() recibo de listas
                AmigosDraw amigos = data.get(recyclerView.getChildAdapterPosition(v));

                /*Toast.makeText(getApplicationContext(), "Seleccion: "+
                        data.get(recyclerView.getChildAdapterPosition(v)).getNombre_usu()+
                        " ID: "+data.get(recyclerView.getChildAdapterPosition(v)).getId_usuario(), Toast.LENGTH_SHORT).show();*/

            }
        });


        recyclerView.setAdapter(adapterAmigos);


        progressDialog.dismiss();
    }

    public void obtenerAmigos(){

        // enviar el id del usuario para recuperar solo sus listas y las listas que le comparten
        // modificar el scrip para que solo busque los id del usuario

        String url = getResources().getString(R.string.urlBuscarAmigosDraw)+"?id_usuario="+id_usuario;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

//                String resString = response.toString();

                JSONObject jsonObject = null;
                data.clear();

                for (int i = 0; i < response.length(); i++) {


                    try {

                        jsonObject = response.getJSONObject(i);

                        if (jsonObject.getString("id_usuario").equalsIgnoreCase("No hay registros")) {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sinAmigos), Toast.LENGTH_SHORT).show();

                            linearImgAdd.setVisibility(View.VISIBLE);

                        } else {

                            data.add(new AmigosDraw(
                                    jsonObject.getString("id_usuario"),
                                    jsonObject.getString("email_usu"),
                                    jsonObject.getString("nombre_usu"),
                                    jsonObject.getString("apellido_usu")
                            ));

                            swipeRefreshLayout.setEnabled(true);

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
        }) /*{

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id_lista", id_lista);
//                parametros.put("id_usuario", id_usuario);


                return parametros;
            }
        }*/;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void datosSqlite(){
        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        fila = baseDeDatos.rawQuery("SELECT id_usuario, email_usu FROM sesion WHERE rowid="+1, null);
        if (fila.moveToFirst()) {
            id_usuario = fila.getString(0);
            email_usu_creador = fila.getString(1);
        }
    }

    public boolean onCreateOptionsMenu(Menu vista) {
        getMenuInflater().inflate(R.menu.item_buscar, vista);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem vista) {
        int id = vista.getItemId();

        if (id == R.id.itemBuscar) {

//                linearBuscar.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);

        }
        return super.onOptionsItemSelected(vista);
    }

    public void buscarAmigo(){

        ibBuscarAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!etBuscarAmigo.getText().toString().trim().isEmpty()){

                    if (!etBuscarAmigo.getText().toString().toLowerCase().trim().equals(email_usu_creador)){

                        guardarAmigo(etBuscarAmigo.getText().toString().trim());

                    }else {

                        Snackbar.make(view, getResources().getString(R.string.nosepuedetumismousuario), Snackbar.LENGTH_SHORT).show();

                    }

                }




            }
        });

    }

    public void guardarAmigo(final String etEmail){
        String url = getResources().getString(R.string.urlAgregarAmigoDraw);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Registrado")) {


                    obtenerAmigos();
                    progressDialog.dismiss();

//                    linearBuscar.setVisibility(View.GONE);
                    cardView.setVisibility(View.GONE);
                    etBuscarAmigo.setText("");

                    // para que se guarde el teclado
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etBuscarAmigo.getWindowToken(), 0);

                } else
                if (response.equalsIgnoreCase("No hay registros")){

//                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailnoExiste), Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, getResources().getString(R.string.emailnoExiste), Snackbar.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                }else
                if(response.equalsIgnoreCase("Existe")){

                    // para que se guarde el teclado
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etBuscarAmigo.getWindowToken(), 0);

//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailExiste), Toast.LENGTH_SHORT).show();

                    Snackbar.make(view, getResources().getString(R.string.emailExiste), Snackbar.LENGTH_SHORT).show();


                    progressDialog.dismiss();

                }else{

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

                parametros.put("email_usu", etEmail);
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