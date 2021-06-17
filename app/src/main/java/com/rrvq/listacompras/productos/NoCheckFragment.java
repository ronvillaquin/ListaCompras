package com.rrvq.listacompras.productos;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.Constantes;
import com.rrvq.listacompras.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoCheckFragment extends Fragment {

    View view;

    RecyclerView recyclerViewNoCheck;
    //    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Productos> data = new ArrayList<>();
    AdapterProductos adapterProductos;
    String responseDATA;
    String editable;
    String icono_art = "abarrotes";

    public NoCheckFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_no_check, container, false);

        casting();

        responseDATA = this.getArguments().getString(Constantes.KEY_FRAGMET);


//        refrescarRecycler();
        obtenerArticulos();




        return view;
    }

    private void casting(){

        recyclerViewNoCheck = view.findViewById(R.id.recyclerviewNOCheck);
//        swipeRefreshLayout = view.findViewById(R.id.refreshRecycler);

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

        recyclerViewNoCheck.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterProductos = new AdapterProductos(getContext(), data);


        adapterProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editable.equals("si")) {
                    //para usar solo listas y referenciasr con get listas.getIdLista() recibo de listas
                    final Productos productos = data.get(recyclerViewNoCheck.getChildAdapterPosition(v));

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


                        dialogo.setPositiveButton(getResources().getString(R.string.editar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogo, int id) {

                                dialogo.cancel();

                                String idlista = productos.getIdLista();  // para enviar al fragment addEDIT
                                String idP = productos.getIdProducto();
                                String nombreP = productos.getNombreP();
                                String precioP = productos.getPrecioP();
                                String cantidadP = productos.getCantidadP();
                                String notaP = productos.getNotaP();
                                String iconoP = productos.getIconoP();
                                // para tener el string del icono tambien
                                icono_art = productos.getIconoPString();


                                AddEditFragment addEditFragment = new AddEditFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString(Constantes.KEY_ID_LISTA, idlista);
                                bundle.putString(Constantes.KEY_ADD_EDIT, "editar");
                                bundle.putString(Constantes.KEY_ID_P, idP);
                                bundle.putString(Constantes.KEY_NOMBRE_P, nombreP);
                                bundle.putString(Constantes.KEY_PRECIO_P, precioP);
                                bundle.putString(Constantes.KEY_CANTIDAD_P, cantidadP);
                                bundle.putString(Constantes.KEY_NOTA_P, notaP);
                                bundle.putString(Constantes.KEY_ICONO_P, iconoP);

                                addEditFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.setCustomAnimations(R.anim.mostrar, R.anim.ocultar);
                                fragmentTransaction.add(R.id.frameLayout, addEditFragment);
                                fragmentTransaction.commit();

                                //para que regrese al fragment o actividad anterior
                                fragmentTransaction.addToBackStack(null);

                                //para ocultar el btn de activity flotante
                                ((ActivityProductos)getActivity()).ocultaBtnFlotante();


                            }
                        });


                        dialogo.show();




                    }
                }
                else {
                    Toast.makeText(getContext(), getResources().getString(R.string.noautorizado), Toast.LENGTH_SHORT).show();
                }

            }
        });



        recyclerViewNoCheck.setAdapter(adapterProductos);

        adapterProductos.notifyDataSetChanged();


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


    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getContext(), "acaba de iniciar NO", Toast.LENGTH_SHORT).show();
        agregaritemDATA();
    }


    private void agregaritemDATA() {

        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(getContext(), "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT * FROM pasarValores", null);

        if (fila.moveToFirst()){
            do {
                if (fila.getString(7).equals("no")){

                    data.add(new Productos(fila.getString(0),fila.getString(1),fila.getString(2),
                            fila.getString(3),fila.getString(4),fila.getString(5),
                            fila.getString(6),fila.getString(7),fila.getString(8),
                            fila.getString(9),fila.getString(10)));
                }
            }while (fila.moveToNext());

            eliminarDatosSqlite();
        }

        adapterProductos.notifyDataSetChanged();

    }

    private void eliminarDatosSqlite(){

        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(getContext(), "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();


        baseDeDatos.delete("pasarValores",null,null);
        baseDeDatos.close();

    }



}