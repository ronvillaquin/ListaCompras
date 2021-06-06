package com.rrvq.listacompras.Amigos;


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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
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

public class AdapterAmigos extends RecyclerView.Adapter<AdapterAmigos.ViewHolder> implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private final List<Amigos> dataAmigos;
    private final Context context;

    private ProgressDialog progressDialog;

    private String id_usuario;

    private View.OnClickListener listener;

    public AdapterAmigos(Context context, List<Amigos> dataAmigos){
        this.layoutInflater = LayoutInflater.from(context);
        this.dataAmigos = dataAmigos;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recyclerview_amigos_chek, parent, false);
        //con este escucha los envento de la lista
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAmigos.ViewHolder holder, final int position) {


        datosSqlite();

        final Amigos amigos = dataAmigos.get(position);

        //para no reciclar las vistas y que no se cambien de posicion los elementos
        //l desabilito
        holder.setIsRecyclable(false);

        String nombre = amigos.getNombre_usu() + " " + amigos.getApellido_usu();
        holder.tvnombreAmigo.setText(nombre);

        holder.tvemailAmigo.setText(amigos.getEmail_usu());



        //si no es el creador no puede eliminar
        if (amigos.getEnableIbMore().equals("no")){
            holder.ibmore.setVisibility(View.INVISIBLE);
            holder.aSwitch.setVisibility(View.GONE);
        }
        else if (amigos.getEnableIbMore().equals("si")){
            holder.aSwitch.setVisibility(View.VISIBLE);
        }

        /*else {
            holder.aSwitch.setVisibility(View.VISIBLE);
        }*/

        if (amigos.getId_usuario().equals(id_usuario)){
            holder.ibmore.setVisibility(View.VISIBLE);
        }

        if (amigos.getEditable_shared().equals("si")){
            holder.aSwitch.setChecked(true);
        }else {
            holder.aSwitch.setChecked(false);
        }

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    holder.aSwitch.setEnabled(false);

                    String url = context.getResources().getString(R.string.urlEditarSwitch);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equalsIgnoreCase("editado")) {

                                Toast.makeText(context, context.getResources().getString(R.string.puedeEditar), Toast.LENGTH_SHORT).show();

                            }

                            holder.aSwitch.setEnabled(true);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            holder.aSwitch.setEnabled(true);
                            Toast.makeText(context, context.getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> parametros = new HashMap<String, String>();

                            parametros.put("id_usu_share", amigos.getId_usuario());
                            parametros.put("id_lista", amigos.getId_lista());
                            parametros.put("editable", "si");


                            return parametros;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);


                }
                else{

                    holder.aSwitch.setEnabled(false);

                    String url = context.getResources().getString(R.string.urlEditarSwitch);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (response.equalsIgnoreCase("editado")) {

                                Toast.makeText(context, context.getResources().getString(R.string.nopuedeEditar), Toast.LENGTH_SHORT).show();

                            }

                            holder.aSwitch.setEnabled(true);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            holder.aSwitch.setEnabled(true);
                            Toast.makeText(context, context.getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> parametros = new HashMap<String, String>();

                            parametros.put("id_usu_share", amigos.getId_usuario());
                            parametros.put("id_lista", amigos.getId_lista());
                            parametros.put("editable", "no");


                            return parametros;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    requestQueue.add(stringRequest);

                }
            }
        });

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

                                    String url = context.getResources().getString(R.string.urlEliminarAmigo);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            if (response.equalsIgnoreCase("Eliminado")) {

                                                Toast.makeText(context, context.getResources().getString(R.string.amigoEliminado), Toast.LENGTH_SHORT).show();

                                                dataAmigos.remove(position);
                                                notifyDataSetChanged();

                                                progressDialog.dismiss();

                                                if (amigos.getId_usuario().equals(id_usuario)){

                                                    Intent intent = new Intent(context, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    context.startActivity(intent);
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

                                            parametros.put("id_usu_share", amigos.getId_usuario());
                                            parametros.put("id_lista", amigos.getId_lista());


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
        Switch aSwitch;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvnombreAmigo = itemView.findViewById(R.id.tvNombreAmigo);
            tvemailAmigo = itemView.findViewById(R.id.tvEmailAmigo);
            ibmore = itemView.findViewById(R.id.ibmore);
            aSwitch = itemView.findViewById(R.id.switch1);


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
