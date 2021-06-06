package com.rrvq.listacompras.AmigosDraw;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.MainActivity;
import com.rrvq.listacompras.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterAmigosDraw extends RecyclerView.Adapter<com.rrvq.listacompras.AmigosDraw.AdapterAmigosDraw.ViewHolder> implements View.OnClickListener {

    private LayoutInflater layoutInflater;
    private List<AmigosDraw> dataAmigos;
    private Context context;

    private ProgressDialog progressDialog;

    private String id_usuario;

    private View.OnClickListener listener;

    public AdapterAmigosDraw(Context context, List<AmigosDraw> dataAmigos){
        this.layoutInflater = LayoutInflater.from(context);
        this.dataAmigos = dataAmigos;
        this.context = context;
    }

    @NonNull
    @Override
    public com.rrvq.listacompras.AmigosDraw.AdapterAmigosDraw.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recyclerview_amigos, parent, false);
        //con este escucha los envento de la lista
        view.setOnClickListener(this);

        return new com.rrvq.listacompras.AmigosDraw.AdapterAmigosDraw.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final com.rrvq.listacompras.AmigosDraw.AdapterAmigosDraw.ViewHolder holder, final int position) {


        datosSqlite();

        final AmigosDraw amigos = dataAmigos.get(position);

        //para no reciclar las vistas y que no se cambien de posicion los elementos
        //l desabilito
        holder.setIsRecyclable(false);

        String nombre = amigos.getNombre_usu() + " " + amigos.getApellido_usu();
        holder.tvnombreAmigo.setText(nombre);

        holder.tvemailAmigo.setText(amigos.getEmail_usu());

        if (amigos.getId_usuario().equals(id_usuario)){

            holder.ibmore.setVisibility(View.VISIBLE);
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

                                    String url = context.getResources().getString(R.string.urlEliminarAmigoDraw);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            if (response.equalsIgnoreCase("Eliminado")) {

                                                Toast.makeText(context, context.getResources().getString(R.string.amigoEliminado), Toast.LENGTH_SHORT).show();

                                                dataAmigos.remove(position);
                                                notifyDataSetChanged();

                                                progressDialog.dismiss();

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

                                            parametros.put("id_usu_amigo", amigos.getId_usuario());
                                            parametros.put("id_usuario", id_usuario);


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


    }

    @Override
    public int getItemCount() {
        return dataAmigos.size();
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

        TextView tvnombreAmigo, tvemailAmigo;
        ImageButton ibmore;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvnombreAmigo = itemView.findViewById(R.id.tvNombreAmigo);
            tvemailAmigo = itemView.findViewById(R.id.tvEmailAmigo);
            ibmore = itemView.findViewById(R.id.ibmore);


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
    }

}