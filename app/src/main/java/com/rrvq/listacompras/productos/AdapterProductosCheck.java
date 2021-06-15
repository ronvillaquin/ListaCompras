package com.rrvq.listacompras.productos;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterProductosCheck extends RecyclerView.Adapter<AdapterProductosCheck.ViewHolder> implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private final List<ProductosCheck> data;
    private final Context context;

    private ProductosCheck productosChange;
    private ViewHolder vholder;

    private ProgressDialog progressDialog;

    private String id_usuario;


    private View.OnClickListener listener;

    public AdapterProductosCheck(Context context, List<ProductosCheck> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recyclerview_productos, parent, false);
        //con este escucha los envento de la lista
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        datosSqlite();
        final ProductosCheck productos = data.get(position);
        vholder = holder;

        //para no reciclar las vistas y que no se cambien de posicion los elementos
        //l desabilito
        holder.setIsRecyclable(false);

//        int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());

        holder.imgIcono.setImageResource(Integer.parseInt(productos.getIconoP()));
        holder.tvnombreP.setText(productos.getNombreP());
        holder.tvcantidadP.setText(productos.getCantidadP());
        holder.tvprecioP.setText("$" + productos.getPrecioP());

        if (productos.getCheckP().equals("no")) {
            holder.linearC.setBackgroundResource(android.R.color.transparent);
            holder.linearDivisor.setBackgroundResource(R.color.transparente);

            holder.checkBox.setChecked(false);
//            holder.ivCheck.setImageResource(R.drawable.ic_casilla_check_24);
        } else if (productos.getCheckP().equals("si")) {
            holder.linearDivisor.setBackgroundResource(R.color.transparente);
            holder.checkBox.setChecked(true);
//            holder.linearC.setBackgroundResource(R.color.sombreproductoCheck);
//            holder.ivCheck.setImageResource(R.drawable.ic_check_24);
//            holder.cardView.setElevation(0);
//            holder.cardView.setRadius(0);
        }

        if (!productos.getId_usuarioCreador().equals(id_usuario)){

            holder.ibmore.setVisibility(View.INVISIBLE);
        }


        holder.ibmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.menu_popup_eliminar);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.itemEliminar) {

                            final AlertDialog.Builder dialogo = new AlertDialog.Builder(context);

                            dialogo.setTitle(context.getResources().getString(R.string.eliminar));
                            dialogo.setMessage(context.getResources().getString(R.string.segurodeeliminar));
//                            dialogo.setCancelable(false);

                            //para el bootn aceptar del dialogo
                            dialogo.setPositiveButton(context.getResources().getString(R.string.si), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo, int id) {

                                    progressDialog = new ProgressDialog(context);
                                    progressDialog.setMessage(context.getResources().getString(R.string.cargando));
                                    progressDialog.show();

                                    String url = context.getResources().getString(R.string.urlEliminarArticulo);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            if (response.equalsIgnoreCase("Eliminado")) {

                                                Toast.makeText(context, context.getResources().getString(R.string.articuloEliminado), Toast.LENGTH_SHORT).show();

                                                data.remove(position);
                                                notifyDataSetChanged();

                                                progressDialog.dismiss();

                                                if (context instanceof ActivityProductos){
                                                    ((ActivityProductos)context).setIntersticial();
                                                }

                                            } else {
                                                Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            Toast.makeText(context, context.getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                        }
                                    }) {

                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {

                                            Map<String, String> parametros = new HashMap<String, String>();

                                            parametros.put("id_articulo", productos.getIdProducto());


                                            return parametros;
                                        }
                                    };

                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    requestQueue.add(stringRequest);


                                }
                            });
                            // para el boton cancelar del dialogo
                            dialogo.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogo, int id) {
                                    // accion si da cancelar que no haga nada
                                    dialogo.cancel();
                                }
                            });
                            dialogo.show();

                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    editarCheck(productos.getIdProducto(), "si", position);

                }else {

                    editarCheck(productos.getIdProducto(), "no", position);

                }

            }
        });





    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener = listener;

    }

    @Override
    public void onClick(View v) {

        if (listener != null){
            listener.onClick(v);
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

//        CheckBox checkBox;
//        ImageView ivCheck;
        ImageView imgIcono;
        ImageButton ibmore;
        TextView tvnombreP, tvcantidadP, tvprecioP;
        LinearLayout linearC, linearDivisor;
        CardView cardView;
        CheckBox checkBox;

        //para el click largo falta
//        View mView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            checkBox = itemView.findViewById(R.id.Checkbox);
//            ivCheck = itemView.findViewById(R.id.ivCheck);
            imgIcono = itemView.findViewById(R.id.imgIcono);
            tvnombreP = itemView.findViewById(R.id.tvnombreProducto);
            tvnombreP = itemView.findViewById(R.id.tvnombreProducto);
            tvcantidadP = itemView.findViewById(R.id.tvcantidadProducto);
            tvprecioP = itemView.findViewById(R.id.tvprecioProducto);
            linearC = itemView.findViewById(R.id.linearC);
            linearDivisor = itemView.findViewById(R.id.linearDivisor);
            cardView = itemView.findViewById(R.id.cardView);
            ibmore = itemView.findViewById(R.id.ibmore);
            checkBox = itemView.findViewById(R.id.checkbox);


            //para click largo falta
//            mView = itemView;

        }
    }

    public void datosSqlite(){
        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(context, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        Cursor fila = baseDeDatos.rawQuery("SELECT id_usuario FROM sesion WHERE rowid="+1, null);
        if (fila.moveToFirst()) {
            id_usuario = fila.getString(0);
        }

        baseDeDatos.close();
    }

    public void guardaParametrosFrag(String idProducto, String nombreP, String precioP, String cantidadP, String notaP,
                                     String iconoP, String iconoPString, String checkP, String idLista, String id_usuarioCreador,
                                     String editable){

        //Conexion a la base de datos SQLITE
        AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(context, "BDListas", null, 1);
        SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

        ContentValues addParametro = new ContentValues();
        addParametro.put("idProducto", idProducto);
        addParametro.put("nombreP", nombreP);
        addParametro.put("precioP", precioP);
        addParametro.put("cantidadP", cantidadP);
        addParametro.put("notaP", notaP);
        addParametro.put("iconoP", iconoP);
        addParametro.put("iconoPString", iconoPString);
        addParametro.put("checkP", checkP);
        addParametro.put("idLista", idLista);
        addParametro.put("id_usuarioCreador", id_usuarioCreador);
        addParametro.put("editable", editable);

        baseDeDatos.insert("pasarValores", null, addParametro);

        baseDeDatos.close();

    }



    public void editarCheck(final String id_art, final String check, final int position) {

        String url = context.getResources().getString(R.string.urleditarCheck);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equalsIgnoreCase("Editado")) {

                    productosChange = data.get(position);

                    guardaParametrosFrag(productosChange.getIdProducto(), productosChange.getNombreP(), productosChange.getPrecioP(),
                            productosChange.getCantidadP(),productosChange.getNotaP(),productosChange.getIconoP(),
                            productosChange.getIconoPString(),check,productosChange.getIdLista(),
                            productosChange.getId_usuarioCreador(),productosChange.getEditable());

                    data.remove(position);
                    notifyDataSetChanged();


                } else {

                    Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();

                    if (check.equals("si")){
                        vholder.checkBox.setChecked(true);

                    }
                    else if (check.equals("no")){
                        vholder.checkBox.setChecked(false);
                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, context.getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();

                if (check.equals("si")){
                    vholder.checkBox.setChecked(true);

                }
                else if (check.equals("no")){
                    vholder.checkBox.setChecked(false);
                }


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

        // para cuando hago peticiones con requesstring solamente ************
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }


}
