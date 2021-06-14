package com.rrvq.listacompras.productos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.rrvq.listacompras.Constantes;
import com.rrvq.listacompras.MainActivity;
import com.rrvq.listacompras.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckFragment extends Fragment {

    View view;
    RecyclerView recyclerViewCheck;
//    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Productos> data = new ArrayList<>();
    AdapterProductos adapterProductos;
    String responseDATA;
    String editable;
    ActivityProductos activityProductos;
    String icono_art = "abarrotes";

    public CheckFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_check, container, false);

        casting();

        Toast.makeText(getContext(), "Hola prueba 1", Toast.LENGTH_SHORT).show();

        responseDATA = this.getArguments().getString(Constantes.KEY_FRAGMET);


//        refrescarRecycler();
        obtenerArticulos();


        return view;
    }

    private void casting(){

        recyclerViewCheck = view.findViewById(R.id.recyclerviewCheck);
//        swipeRefreshLayout = view.findViewById(R.id.refreshRecycler);

       activityProductos = new ActivityProductos();

    }


   /* public void refrescarRecycler() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                activityProductos.obtenerArticulos();

                adapterProductos.notifyDataSetChanged();

                //swipe tiempo de demora del circulo pprogreso
//                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }*/


    public void setRecyclerView(){

        recyclerViewCheck.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterProductos = new AdapterProductos(getContext(), data);


        adapterProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editable.equals("si")) {
                    //para usar solo listas y referenciasr con get listas.getIdLista() recibo de listas
                    final Productos productos = data.get(recyclerViewCheck.getChildAdapterPosition(v));

                    /*Toast.makeText(getApplicationContext(), "Seleccion: "+
                            data.get(recyclerView.getChildAdapterPosition(v)).getNombreP()+
                            " ID: "+data.get(recyclerView.getChildAdapterPosition(v)).getIdProducto(), Toast.LENGTH_SHORT).show();*/


                    if (productos.getCheckP().equals("no")){

                        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());

                        dialogo.setTitle(getResources().getString(R.string.articulo));
                        //                    dialogo.setCancelable(false);

                        LayoutInflater inflater = getLayoutInflater();

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
                                activityProductos.editarCheck(productos.getIdProducto(), "si");

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

                                activityProductos.editarArticulo(idP, nombreP, precioP, cantidadP, notaP, iconoP);

                                activityProductos.obtenerIconos();

                            }
                        });


                        dialogo.show();




                    }
                    else if (productos.getCheckP().equals("si")){

                        activityProductos.editarCheck(productos.getIdProducto(), "no");

                    }
                }
                else {
                    Toast.makeText(getContext(), getResources().getString(R.string.noautorizado), Toast.LENGTH_SHORT).show();
                }

            }
        });


        recyclerViewCheck.setAdapter(adapterProductos);


//        progressDialog.dismiss();


    }

    public void obtenerArticulos(){


        try {
            JSONArray response = new JSONArray(responseDATA); // para convertirlo en json el string
            JSONObject jsonObject = null;
            data.clear();


            //*************para poner los que no estan check  ************//
            for (int i = 0; i < response.length(); i++) {


                try {

                    jsonObject = response.getJSONObject(i);

                    if (jsonObject.getString("id_lista").equalsIgnoreCase("No hay registros")) {

                        Toast.makeText(getContext(), getResources().getString(R.string.sinproductos), Toast.LENGTH_SHORT).show();

                        editable = jsonObject.getString("editable");

                    } else {

//                            int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                        int img = getResources().getIdentifier(jsonObject.getString("icono_art"), "drawable", getContext().getPackageName());
                        String imgs = String.valueOf(img);


                        if (jsonObject.getString("check_art").equalsIgnoreCase("no")) {
                            data.add(new Productos(
                                    jsonObject.getString("id_articulo"),
                                    jsonObject.getString("nombre_art"),
                                    jsonObject.getString("precio_art"),
                                    jsonObject.getString("cantidad_art"),
                                    jsonObject.getString("nota_art"),
                                    imgs,
                                    jsonObject.getString("icono_art"),
                                    jsonObject.getString("check_art"),
                                    jsonObject.getString("id_lista"),
                                    jsonObject.getString("id_usuario"),
                                    jsonObject.getString("editable")
                            ));

                            editable = jsonObject.getString("editable");

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            setRecyclerView();



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}