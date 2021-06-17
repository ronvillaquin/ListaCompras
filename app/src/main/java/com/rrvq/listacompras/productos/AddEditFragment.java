package com.rrvq.listacompras.productos;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rrvq.listacompras.Constantes;
import com.rrvq.listacompras.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddEditFragment extends Fragment {

    private View view;

    TextView tvTitleToolbar;
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


    private TextInputLayout etNombreLayout, etPrecioLayout, etCantidadLayout, etNotaLayout;
    private TextInputEditText etNombre, etPrecio, etCantidad, etNota;

    private FloatingActionButton  btnFlotante2;

    private ImageButton ibx;

    private ImageView ivIconoSeleccion;
    private String icono_art = "abarrotes";

    private String add_edit,idP, nombreP, precioP, cantidadP, notaP, iconoP, id_lista;





    public AddEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_edit, container, false);

        recibeParametros();

        castingView();
        obtenerIconos();

        if (add_edit.equals("editar")){
            llenarCamposEditar(idP,nombreP,precioP,cantidadP,notaP,iconoP);
            //paracolocar titulo en el toolvar use texview porgue getsuportactionbar daba error
            tvTitleToolbar.setText(getResources().getString(R.string.editarProducto));
        }else {
            btnFlotanteGuardar(add_edit,idP);
            //paracolocar titulo en el toolvar use texview porgue getsuportactionbar daba error
            tvTitleToolbar.setText(getResources().getString(R.string.agregarProducto));
        }

        cerrarFragment();



        return view;
    }



    private void recibeParametros(){
        id_lista = this.getArguments().getString(Constantes.KEY_ID_LISTA);
        add_edit = this.getArguments().getString(Constantes.KEY_ADD_EDIT);
        idP = this.getArguments().getString(Constantes.KEY_ID_P);
        nombreP = this.getArguments().getString(Constantes.KEY_NOMBRE_P);
        precioP = this.getArguments().getString(Constantes.KEY_PRECIO_P);
        cantidadP = this.getArguments().getString(Constantes.KEY_CANTIDAD_P);
        notaP = this.getArguments().getString(Constantes.KEY_NOTA_P);
        iconoP = this.getArguments().getString(Constantes.KEY_ICONO_P);
    }

    private void castingView(){
        tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);

        recyclerViewIconos = view.findViewById(R.id.recyclerviewIconos);

        btnFlotante2 = view.findViewById(R.id.btnFlotante2);
        ibx = view.findViewById(R.id.ibx);

        ivIconoSeleccion = view.findViewById(R.id.ivIconoSeleccion);

        etNombre = view.findViewById(R.id.etNombre);
        etPrecio = view.findViewById(R.id.etPrecio);
        etCantidad = view.findViewById(R.id.etCantidad);
        etNota = view.findViewById(R.id.etNota);

        etNombreLayout = view.findViewById(R.id.etNombreL);
        etPrecioLayout = view.findViewById(R.id.etPrecioLayout);
        etCantidadLayout = view.findViewById(R.id.etCantidadLayout);
        etNotaLayout = view.findViewById(R.id.etNotaLayout);
    }

    private void setRecyclerViewIconos(){
        //----------------para el recyclervie
        /*data.add(new Listas("1", "Casa", "5/8", "30"));
        data.add(new Listas("2", "1", "5/8", "10"));*/

//        recyclerViewIconos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIconos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapterIconos = new AdapterIconos(getContext(), dataIconos);


        adapterIconos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //para usar solo listas y referenciasr con get listas.getIdLista() recibo de listas
                Iconos iconos = dataIconos.get(recyclerViewIconos.getChildAdapterPosition(v));

                /*Toast.makeText(getApplicationContext(), "Seleccion: "+
                        dataIconos.get(recyclerViewIconos.getChildAdapterPosition(v)).getNombreC(), Toast.LENGTH_SHORT).show();*/

                icono_art = iconos.getIconString();

                //para que aparsca el icono con una animacion
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.mostrar);

                ivIconoSeleccion.setAnimation(animation);
                ivIconoSeleccion.setImageResource(Integer.parseInt(iconos.getIcono()));

            }
        });


        recyclerViewIconos.setAdapter(adapterIconos);

    }

    private void obtenerIconos(){

        dataIconos.clear();
        for (int i=0; i<imgIconos.length; i++){


            int imgs = getResources().getIdentifier(imgIconos[i], "drawable", getContext().getPackageName());
            String imgss = String.valueOf(imgs);

            dataIconos.add(new Iconos(imgss, getString(nombreIconos[i]), imgIconos[i]));
        }

        setRecyclerViewIconos();

    }

    private void btnFlotanteGuardar(final String auxS, final String idP){

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

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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

    private void llenarCamposEditar(String idP, String nombreP, String precioP, String cantidadP, String notaP, String iconoP) {

        etNombre.setText(nombreP);
        ivIconoSeleccion.setImageResource(Integer.parseInt(iconoP));
        etCantidad.setText(cantidadP);
        etPrecio.setText(precioP);
        etNota.setText(notaP);

        btnFlotanteGuardar(add_edit, idP);

    }

    private void guardarArticuloEditado(final String idP, final String nombre_art, final String cantidad_art, final String precio_art, final String nota_art) {


        String url = getResources().getString(R.string.urleditarArticulo);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Editado")) {



                    etNombre.setText("");
                    etPrecio.setText("");
                    etCantidad.setText("1");
                    etNota.setText("");

                    ((ActivityProductos)getActivity()).obtenerArticulos();
                    ((ActivityProductos)getActivity()).muestraBtnFlotante();

                    getActivity().getSupportFragmentManager().popBackStack();


                } else {

                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }


    private void guardarArticulo(final String nombre_art, final String cantidad_art, final String precio_art, final String nota_art) {


        String url = getResources().getString(R.string.urlagregarArticulo);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Registrado")) {


                    ((ActivityProductos)getActivity()).obtenerArticulos();
                    ((ActivityProductos)getActivity()).muestraBtnFlotante();

                    getActivity().getSupportFragmentManager().popBackStack();

                } else {

                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void cerrarFragment(){
        ibx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                //pra mostrar el boton del activity flotante
                ((ActivityProductos)getActivity()).muestraBtnFlotante();

//                ((ActivityProductos)getActivity()).obtenerArticulos();

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        // para que se guarde el teclado cuando cierro el fragment
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNombre.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etPrecio.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etCantidad.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etNota.getWindowToken(), 0);
    }


}