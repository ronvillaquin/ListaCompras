package com.rrvq.listacompras.productos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.Amigos.MainActivity_Amigos;
import com.rrvq.listacompras.Constantes;
import com.rrvq.listacompras.DebesRegistrarteIntersticial;
import com.rrvq.listacompras.Flavor;
import com.rrvq.listacompras.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityProductos extends AppCompatActivity {

    Toolbar toolbar;

    // para el volley
    RecyclerView recyclerView;
    AdapterProductos adapterProductos;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    ArrayList<Productos> data = new ArrayList<>();

    // para los iconos precargados
    RecyclerView recyclerViewIconos;
    AdapterIconos adapterIconos;
    ArrayList<Iconos> dataIconos = new ArrayList<>();
    //no se cambia por string porque son nombres de las imagenes
    String[] imgIconos = {"abarrotes", "aceites","agua","bebidas","alcohol","cafe","lacteos","pan","reposteria",
            "dulces","carnes","pescados","congelados","conservados","enlatados","salsas","frutas","vegetales","preparada",
            "mascotas","higiene","hogar","limpieza","libros","ropa","bebes","belleza","salud","vehiculos","electrodomesticos"};
    int[] nombreIconos = {R.string.abarrotes, R.string.aceites, R.string.agua, R.string.bebidas, R.string.alcohol,
            R.string.cafe,R.string.lacteos,R.string.pan,R.string.reposteria,R.string.dulces,R.string.carnes,
            R.string.pescados,R.string.congelados,R.string.conservados,R.string.enlatados,R.string.salsas,R.string.frutas,
            R.string.vegetales,R.string.preparada,R.string.mascotas,R.string.higiene,R.string.hogar,R.string.limpieza,
            R.string.libros,R.string.ropa,R.string.bebes,R.string.belleza,R.string.salud,R.string.vehiculos,R.string.electrodomesticos};

    TextInputLayout etNombreLayout, etPrecioLayout, etCantidadLayout, etNotaLayout;
    TextInputEditText etNombre, etPrecio, etCantidad, etNota;

    TextView tvtotalMarcado, tvTotalSinMarcar, tvTotalTotal;
    float totalMarcado, totalSinMarcar, totalTotal;
    DecimalFormat formato = new DecimalFormat("#.#");

    ConstraintLayout vista1, vista2;

    FloatingActionButton btnFlotante, btnFlotante2;
    ImageButton ibx, ibAmigos;
    int auxAdd = 0;

    String id_lista, nombre_lista;
    ImageView ivIconoSeleccion;
    String icono_art = "abarrotes";

    TextView tvAmigos;

    AdView mAdView;

    LinearLayout linearImgAdd;

    String nombre, apellido, id_usuario, editable;

    InterstitialAd mInterstitialAd;

    ViewPageAdapter mPageAdapter;
    ViewPager2 viewPagerChecks;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aproductos);


        id_lista = getIntent().getStringExtra("id_lista");
        nombre_lista = getIntent().getStringExtra("nombre_lista");

        castinView();
        toolbarMenu();
        flechaBlanca();

        verificar();
        datosSqlite();


        // para cerrar la vista 2
        ibx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ocultar);
                vista2.startAnimation(animation);

                vista1.setVisibility(View.VISIBLE);
                vista2.setVisibility(View.GONE);

                auxAdd = 0;

                etNombre.setText("");
                etPrecio.setText("");
                etCantidad.setText("1");
                etNota.setText("");

                // para que se guarde el teclado
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(etPrecio.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(etCantidad.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(etNota.getWindowToken(), 0);

            }
        });

        ibAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nombre.equals("invitado")&&apellido.equals("invitado")) {
                    DebesRegistrarteIntersticial debesRegistrarteIntersticial = new DebesRegistrarteIntersticial(ActivityProductos.this);
                    debesRegistrarteIntersticial.dialogoRegistrarce();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity_Amigos.class);
                    intent.putExtra("id_lista", id_lista);
                    intent.putExtra("nombre_lista", nombre_lista);
                    startActivity(intent);
                }

            }
        });



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.cargando));
        progressDialog.show();

        obtenerArticulos();
        btnFlotanteAdd();
        btnFlotanteGuardar("", ""); //mando variable string para poder comparar si es editar o guarda nuevo
        refrescarRecycler();

        obtenerCantidadAmigos();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Constantes.INTERSTICIAL);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }

    public void mostrarFrgments(String responseDATA){
        // para crear la vista de tabs y pasar parametos a viewpageadapter
        //se llama al metodo publico mediante addFragment y se le pasa o agrega cada array
        tabLayout = findViewById(R.id.tabChecks);
        viewPagerChecks = findViewById(R.id.viewPagerChecks);

        mPageAdapter = new ViewPageAdapter(responseDATA, getSupportFragmentManager(), getLifecycle());
        viewPagerChecks.setAdapter(mPageAdapter);

        new TabLayoutMediator(tabLayout, viewPagerChecks, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull  TabLayout.Tab tab, int i) {

                switch (i){
                    case 0:
//                        tab.setText("Check");
                        tab.setIcon(R.drawable.ic_casilla_check_24);

                        break;
                    case 1:
//                        tab.setText("No Check");
                        tab.setIcon(R.drawable.ic_check_24);


                        break;
                }
            }
        }).attach();
    }


    public void datosSqlite(){
        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT id_usuario, nombre_usu, apellido_usu FROM sesion WHERE rowid="+1, null);
        if (fila.moveToFirst()) {
            id_usuario = fila.getString(0);
            nombre = fila.getString(1);
            apellido = fila.getString(2);

        }
    }

    private void castinView() {

        recyclerView = findViewById(R.id.recyclerview);
        swipeRefreshLayout = findViewById(R.id.refreshRecycler);
        recyclerViewIconos = findViewById(R.id.recyclerviewIconos);

        toolbar = findViewById(R.id.toolbar);
        btnFlotante = findViewById(R.id.btnFlotante);
        btnFlotante2 = findViewById(R.id.btnFlotante2);
        ibx = findViewById(R.id.ibx);
        ibAmigos = findViewById(R.id.ibAmigos);
        vista1 = findViewById(R.id.vista1);
        vista2 = findViewById(R.id.vista2);

        ivIconoSeleccion = findViewById(R.id.ivIconoSeleccion);

        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etCantidad = findViewById(R.id.etCantidad);
        etNota = findViewById(R.id.etNota);

        etNombreLayout = findViewById(R.id.etNombreL);
        etPrecioLayout = findViewById(R.id.etPrecioLayout);
        etCantidadLayout = findViewById(R.id.etCantidadLayout);
        etNotaLayout = findViewById(R.id.etNotaLayout);

        tvtotalMarcado = findViewById(R.id.tvTotalMarcado);
        tvTotalSinMarcar = findViewById(R.id.tvTotalSinMarcar);
        tvTotalTotal = findViewById(R.id.tvTotalTotal);

        tvAmigos = findViewById(R.id.tvamigos);

        linearImgAdd = findViewById(R.id.linearImgAdd);

        mAdView = findViewById(R.id.adView);

    }

    //******************************** pantalla 1 mostrar productos *************************//

    public void toolbarMenu() {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>" + nombre_lista + "</font>"));

        // para colocar icono de more 3 puntos de olor blanco
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_24_blanco));

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


                obtenerArticulos();
                adapterProductos.notifyDataSetChanged();

                //swipe tiempo de demora del circulo pprogreso
//                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    public void btnFlotanteAdd(){

        // onclick del boton flotente
        btnFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editable.equals("si")) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mostrar);
                    vista2.startAnimation(animation);

                    vista1.setVisibility(View.GONE);
                    vista2.setVisibility(View.VISIBLE);

                    auxAdd = 1;

                    obtenerIconos();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.noautorizado), Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void setRecyclerView(){

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterProductos = new AdapterProductos(this, data);


            adapterProductos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (editable.equals("si")) {
                        //para usar solo listas y referenciasr con get listas.getIdLista() recibo de listas
                        final Productos productos = data.get(recyclerView.getChildAdapterPosition(v));

                    /*Toast.makeText(getApplicationContext(), "Seleccion: "+
                            data.get(recyclerView.getChildAdapterPosition(v)).getNombreP()+
                            " ID: "+data.get(recyclerView.getChildAdapterPosition(v)).getIdProducto(), Toast.LENGTH_SHORT).show();*/


                        if (productos.getCheckP().equals("no")){

                            final AlertDialog.Builder dialogo = new AlertDialog.Builder(ActivityProductos.this);

                            dialogo.setTitle(getResources().getString(R.string.articulo));
        //                    dialogo.setCancelable(false);

                            LayoutInflater inflater = ActivityProductos.this.getLayoutInflater();

                            View dialogView = inflater.inflate(R.layout.dialog_producto, null);
                            dialogo.setView(dialogView);

                            TextInputEditText etnombreP, etcantidadP, etprecioP, etnotaP;
                            ImageView ivI;

                            etnombreP = dialogView.findViewById(R.id.etNombre);
                            etcantidadP = dialogView.findViewById(R.id.etCantidad);
                            etprecioP = dialogView.findViewById(R.id.etPrecio);
                            etnotaP = dialogView.findViewById(R.id.etNota);
                            ivI = dialogView.findViewById(R.id.ivIcono);

                            etnombreP.setText(productos.getNombreP());
                            etcantidadP.setText(productos.getCantidadP());
                            etprecioP.setText(productos.getPrecioP());
                            etnotaP.setText(productos.getNotaP());
                            ivI.setImageResource(Integer.parseInt(productos.getIconoP()));

                            //para el bootn aceptar del dialogo
                            dialogo.setPositiveButton(getResources().getString(R.string.check), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo, int id) {

                                    //cuando se da check se envia a editar a check
                                    editarCheck(productos.getIdProducto(), "si");

                                }
                            });
        /*
                            dialogo.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo, int id) {
                                    // accion si da cancelar que no haga nada
                                    dialogo.cancel();
                                }
                            });*/

                            dialogo.setNeutralButton(getResources().getString(R.string.editar), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo, int id) {

                                    dialogo.cancel();

                                    String idP = productos.getIdProducto();
                                    String nombreP = productos.getNombreP();
                                    String precioP = productos.getPrecioP();
                                    String cantidadP = productos.getCantidadP();
                                    String notaP = productos.getNotaP();
                                    String iconoP = productos.getIconoP();
                                    // para tener el string del icono tambien
                                    icono_art = productos.getIconoPString();

                                    editarArticulo(idP, nombreP, precioP, cantidadP, notaP, iconoP);

                                    obtenerIconos();

                                }
                            });


                            dialogo.show();




                        }
                        else if (productos.getCheckP().equals("si")){

                            editarCheck(productos.getIdProducto(), "no");

                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.noautorizado), Toast.LENGTH_SHORT).show();
                    }

                }
            });


        recyclerView.setAdapter(adapterProductos);


        progressDialog.dismiss();


    }

    public void obtenerArticulos(){

        // enviar el id del usuario para recuperar solo sus listas y las listas que le comparten
        // modificar el scrip para que solo busque los id del usuario

        totalMarcado=0;
        totalSinMarcar=0;
        totalTotal=0;
        String url = getResources().getString(R.string.urlBuscarArticulo)+id_lista+"&id_usu_share="+id_usuario+"";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                mostrarFrgments(response.toString());


//                String resString = response.toString();
//                resString = response.toString();

                JSONObject jsonObject = null;
//                data.clear();

                tvtotalMarcado.setText("$0");
                tvTotalSinMarcar.setText("$0");
                tvTotalTotal.setText("$0");

                //*************para poner los que no estan check  ************//
                for (int i = 0; i < response.length(); i++) {


                    try {

                        jsonObject = response.getJSONObject(i);

                        if (jsonObject.getString("id_lista").equalsIgnoreCase("No hay registros")) {

                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.sinproductos), Toast.LENGTH_SHORT).show();

                            linearImgAdd.setVisibility(View.VISIBLE);
                            editable = jsonObject.getString("editable");

                        } else {

                                editable = jsonObject.getString("editable");

                                totalSinMarcar = totalSinMarcar + Float.parseFloat(jsonObject.getString("precio_art"));
                            totalMarcado = totalMarcado + Float.parseFloat(jsonObject.getString("precio_art"));

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

                totalTotal = totalMarcado + totalSinMarcar;

                tvtotalMarcado.setText("$" + formato.format(totalMarcado));
                tvTotalSinMarcar.setText("$" + formato.format(totalSinMarcar));
                tvTotalTotal.setText("$" + formato.format(totalTotal));

//                setRecyclerView();



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

    private void editarCheck(final String id_art, final String check) {

        String url = getResources().getString(R.string.urleditarCheck);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Editado")) {


                    obtenerArticulos();

                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id_articulo", id_art);
                parametros.put("check_art", check);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void obtenerCantidadAmigos(){
        String url = getResources().getString(R.string.urlCantidadAmigos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                tvAmigos.setText(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id_lista", id_lista);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    //*******************************Pantalla 2 guardar nuevo producto *****************//

    public void setRecyclerViewIconos(){
        //----------------para el recyclervie
        /*data.add(new Listas("1", "Casa", "5/8", "30"));
        data.add(new Listas("2", "1", "5/8", "10"));*/

//        recyclerViewIconos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIconos.setLayoutManager(new GridLayoutManager(this, 3));
        adapterIconos = new AdapterIconos(this, dataIconos);


        adapterIconos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //para usar solo listas y referenciasr con get listas.getIdLista() recibo de listas
                Iconos iconos = dataIconos.get(recyclerView.getChildAdapterPosition(v));

                /*Toast.makeText(getApplicationContext(), "Seleccion: "+
                        dataIconos.get(recyclerViewIconos.getChildAdapterPosition(v)).getNombreC(), Toast.LENGTH_SHORT).show();*/

                icono_art = iconos.getIconString();
                ivIconoSeleccion.setImageResource(Integer.parseInt(iconos.getIcono()));

            }
        });


        recyclerViewIconos.setAdapter(adapterIconos);

    }

    public void obtenerIconos(){

        dataIconos.clear();
        for (int i=0; i<imgIconos.length; i++){


            int imgs = getResources().getIdentifier(imgIconos[i], "drawable", getPackageName());
            String imgss = String.valueOf(imgs);

            dataIconos.add(new Iconos(imgss, getString(nombreIconos[i]), imgIconos[i]));
        }

        setRecyclerViewIconos();

    }

    public void btnFlotanteGuardar(final String auxS, final String idP){

        // onclick del boton flotente
        btnFlotante2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (auxS.equals("editar")){

                    if (!etNombre.getText().toString().isEmpty()) {

//                    etNombreLayout.setError("");

                        String nombre_art = etNombreLayout.getEditText().getText().toString().trim();
                        String cantidad_art = etCantidadLayout.getEditText().getText().toString().trim();
                        String precio_art = etPrecioLayout.getEditText().getText().toString().trim();
                        String nota_art = etNotaLayout.getEditText().getText().toString().trim();

                    /*String cantidad_art = etCantidad.getText().toString();
                    String precio_art = etPrecio.getText().toString();
                    String nota_art = etNota.getText().toString();*/

                        guardarArticuloEditado(idP, nombre_art, cantidad_art, precio_art, nota_art);

                        etNombreLayout.setError("");

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(etPrecio.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(etCantidad.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(etNota.getWindowToken(), 0);

                        btnFlotanteGuardar("",""); // lo coloco sin nada los parametros de nuevo

                    } else {

                        etNombreLayout.setError(getResources().getString(R.string.requerido));
                    }

                }
                else {


                    if (!etNombre.getText().toString().isEmpty()) {

//                    etNombreLayout.setError("");

                        String nombre_art = etNombreLayout.getEditText().getText().toString().trim();
                        String cantidad_art = etCantidadLayout.getEditText().getText().toString().trim();
                        String precio_art = etPrecioLayout.getEditText().getText().toString().trim();
                        String nota_art = etNotaLayout.getEditText().getText().toString().trim();

                    /*String cantidad_art = etCantidad.getText().toString();
                    String precio_art = etPrecio.getText().toString();
                    String nota_art = etNota.getText().toString();*/

                        guardarArticulo(nombre_art, cantidad_art, precio_art, nota_art);

                        etNombre.setText("");
                        etPrecio.setText("");
                        etCantidad.setText("1");
                        etNota.setText("");

                        etNombreLayout.setError("");

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(etPrecio.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(etCantidad.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(etNota.getWindowToken(), 0);

                    } else {

                        etNombreLayout.setError(getResources().getString(R.string.requerido));
                    }

                }



            }
        });
    }

    private void editarArticulo(String idP, String nombreP, String precioP, String cantidadP, String notaP, String iconoP) {

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.mostrar);
        vista2.startAnimation(animation);

        vista1.setVisibility(View.GONE);
        vista2.setVisibility(View.VISIBLE);

        auxAdd = 1;

        etNombre.setText(nombreP);
        ivIconoSeleccion.setImageResource(Integer.parseInt(iconoP));
        etCantidad.setText(cantidadP);
        etPrecio.setText(precioP);
        etNota.setText(notaP);

        btnFlotanteGuardar("editar", idP);

    }

    private void guardarArticuloEditado(final String idP, final String nombre_art, final String cantidad_art, final String precio_art, final String nota_art) {


        progressDialog.show();

        String url = getResources().getString(R.string.urleditarArticulo);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Editado")) {

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ocultar);
                    vista2.startAnimation(animation);

                    vista1.setVisibility(View.VISIBLE);
                    vista2.setVisibility(View.GONE);

                    auxAdd = 0;


                    obtenerArticulos();
                    progressDialog.dismiss();

                    etNombre.setText("");
                    etPrecio.setText("");
                    etCantidad.setText("1");
                    etNota.setText("");


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

                parametros.put("id_articulo", idP);
                parametros.put("nombre_art", nombre_art);
                parametros.put("precio_art", precio_art);
                parametros.put("cantidad_art", cantidad_art);
                parametros.put("nota_art", nota_art);
                parametros.put("icono_art", icono_art);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void guardarArticulo(final String nombre_art, final String cantidad_art, final String precio_art, final String nota_art) {

        progressDialog.show();

        String url = getResources().getString(R.string.urlagregarArticulo);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Registrado")) {

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ocultar);
                    vista2.startAnimation(animation);

                    vista1.setVisibility(View.VISIBLE);
                    vista2.setVisibility(View.GONE);

                    auxAdd = 0;


                    obtenerArticulos();
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

                parametros.put("nombre_art", nombre_art);
                parametros.put("precio_art", precio_art);
                parametros.put("cantidad_art", cantidad_art);
                parametros.put("nota_art", nota_art);
                parametros.put("icono_art", icono_art);
                parametros.put("check_art", "no");
                parametros.put("id_lista", id_lista);


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



    //************************GENERAL   **************************************//

    public boolean onCreateOptionsMenu(Menu vista) {
        getMenuInflater().inflate(R.menu.menu_seleccionar, vista);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem vista) {
        int id = vista.getItemId();

        if (editable.equals("si")) {
            if (id == R.id.itemSeleccionar) {
                btnMoreSeleccionar("si", id_lista);
            }else if (id == R.id.itemDeseleccionar){
                btnMoreSeleccionar("no", id_lista);

            }
        }
        else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.noautorizado), Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(vista);
    }

    public void btnMoreSeleccionar(final String valor, final String id_lista){

        String url = getResources().getString(R.string.urleditarChecktodos);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Editado")) {


                    obtenerArticulos();

                } else {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id_lista", id_lista);
                parametros.put("check_art", valor);


                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public void onBackPressed() {

        if (auxAdd == 1){
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ocultar);
            vista2.startAnimation(animation);

            vista1.setVisibility(View.VISIBLE);
            vista2.setVisibility(View.GONE);
            auxAdd = 0;

            etNombre.setText("");
            etPrecio.setText("");
            etCantidad.setText("1");
            etNota.setText("");

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etPrecio.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etCantidad.getWindowToken(), 0);
            imm.hideSoftInputFromWindow(etNota.getWindowToken(), 0);

        }else {
            finish();
        }

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

}
