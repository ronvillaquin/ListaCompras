package com.rrvq.listacompras.listas;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.rrvq.listacompras.AdminSQLiteOpenHelper;
import com.rrvq.listacompras.Constantes;
import com.rrvq.listacompras.Flavor;
import com.rrvq.listacompras.MainActivity;
import com.rrvq.listacompras.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterListas extends RecyclerView.Adapter<AdapterListas.ViewHolder> implements View.OnClickListener{

    private LayoutInflater layoutInflater;
    private List<Listas> data;
    private Context context;

    private ProgressDialog progressDialog;

    private String etnombre;
    private String id_usuario;

    private View.OnClickListener listener;

    private View view;

    public AdapterListas(Context context, List<Listas> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recyclerview_listas, parent, false);
        //con este escucha los envento de la lista
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        datosSqlite();
        final Listas listas = data.get(position);
        //para no reciclar las vistas y que no se cambien de posicion los elementos
        //l desabilito
        holder.setIsRecyclable(false);

        if (Integer.parseInt(listas.getCantida_amigos()) > 0){
            holder.linearCamigos.setVisibility(View.VISIBLE);
            holder.tvAmigos.setText(listas.getCantida_amigos());
        }

        holder.tvnombreLista.setText(listas.getNombreLista());

        String pc = listas.getCheckP()+"/"+listas.getTotalP();
        holder.tvcantidadProductos.setText(pc);

        //aqui parseo a entero para el progress Bar colocar los productos que ya se checkiaron
//        final String checkP = listas.getCheckP();
        holder.progressBar.setProgress(Integer.parseInt(listas.getCheckP()));
        //colocar el contador de los producto totaltel que ya esten chequeados
        holder.progressBar.setMax(Integer.parseInt(listas.getTotalP()));

        if (!listas.getId_usuario().equals(id_usuario)){

            holder.ibmore.setVisibility(View.INVISIBLE);
        }

        holder.ibmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.itemDuplicar:
                                final AlertDialog.Builder dialogoD = new AlertDialog.Builder(context);

                                dialogoD.setTitle(context.getResources().getString(R.string.duplicar));
                                dialogoD.setMessage(context.getResources().getString(R.string.segurodeduplicar));

                                dialogoD.setPositiveButton(context.getResources().getString(R.string.si), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage(context.getResources().getString(R.string.cargando));
                                        progressDialog.show();

                                        String url = context.getResources().getString(R.string.urlDuplicarlista);
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                if (response.equalsIgnoreCase("Duplicado")){

                                                    Toast.makeText(context, context.getResources().getString(R.string.listaduplicada), Toast.LENGTH_SHORT).show();


                                                    // para istanciar un metodo de una clase o actividad desde Adapter
                                                    if (context instanceof MainActivity){
                                                        ((MainActivity)context).obtenerLista();
                                                    }

                                                    if (context instanceof MainActivity){
                                                        ((MainActivity)context).setIntersticial();
                                                    }

//                                                    data.remove(position);
//                                                    notifyDataSetChanged();

                                                    progressDialog.dismiss();

                                                }else {
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
                                        }){

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {

                                                Map<String, String> parametros = new HashMap<String, String>();

                                                parametros.put("id_lista", listas.getIdLista());


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
                                });

                                dialogoD.setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                dialogoD.show();

                                break;
                            case R.id.itemEditar:
//
                                //*********************** DIALOGO   *********************************/
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);


                                dialog.setTitle(context.getResources().getString(R.string.editar));
//                                dialog.setCancelable(false);

                                // mejor inflarlo por xml
                                final EditText etdialog = new EditText(context);
                                etdialog.setInputType(InputType.TYPE_CLASS_TEXT);
                                etdialog.setEms(10);
                                etdialog.setText(listas.getNombreLista());

                                dialog.setView(etdialog);


                                //para el bootn aceptar del dialogo
                                dialog.setPositiveButton(context.getResources().getString(R.string.editar), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo, int id) {

                                        etnombre = etdialog.getText().toString();

                                        progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage(context.getResources().getString(R.string.cargando));
                                        progressDialog.show();

                                        String url = context.getResources().getString(R.string.urlEditarLista);
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                if (response.equalsIgnoreCase("Editado")){

                                                    listas.setNombreLista(etnombre);
                                                    notifyDataSetChanged();


                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, context.getResources().getString(R.string.nombreEditado), Toast.LENGTH_SHORT).show();

//                                                    verificarFlavor();
                                                    // para istanciar un metodo de una clase o actividad desde Adapter
                                                    if (context instanceof MainActivity){
                                                        ((MainActivity)context).setIntersticial();
                                                    }



                                                }else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                                progressDialog.dismiss();
                                                Toast.makeText(context, context.getResources().getString(R.string.conexion), Toast.LENGTH_SHORT).show();


                                            }
                                        }){

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {

                                                Map<String, String> parametros = new HashMap<String, String>();

                                                parametros.put("id_lista", listas.getIdLista());
                                                parametros.put("nombre_lis", etnombre);


                                                return parametros;
                                            }
                                        };

                                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                                        requestQueue.add(stringRequest);


                                    }
                                });
                                // para el boton cancelar del dialogo
                                dialog.setNegativeButton(context.getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo, int id) {
                                        // accion si da cancelar que no haga nada
                                        dialogo.cancel();
                                    }
                                });
                                dialog.show();

                                break;
                            case R.id.itemEliminar:


                                //*********************** DIALOGO   *********************************/
                                final AlertDialog.Builder dialogo = new AlertDialog.Builder(context);

                                dialogo.setTitle(context.getResources().getString(R.string.eliminar));
                                dialogo.setMessage(context.getResources().getString(R.string.segurodeeliminar));
//                                dialogo.setCancelable(false);

                                //para el bootn aceptar del dialogo
                                dialogo.setPositiveButton(context.getResources().getString(R.string.si), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogo, int id) {

                                        progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage(context.getResources().getString(R.string.cargando));
                                        progressDialog.show();

                                        String url = context.getResources().getString(R.string.urlEliminarLista);
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                if (response.equalsIgnoreCase("Eliminado")){

                                                    Toast.makeText(context, context.getResources().getString(R.string.listaEliminada), Toast.LENGTH_SHORT).show();

                                                    data.remove(position);
                                                    notifyDataSetChanged();

                                                    progressDialog.dismiss();

                                                    if (context instanceof MainActivity){
                                                        ((MainActivity)context).setIntersticial();
                                                    }

                                                }else {
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
                                        }){

                                            @Override
                                            protected Map<String, String> getParams() throws AuthFailureError {

                                                Map<String, String> parametros = new HashMap<String, String>();

                                                parametros.put("id_lista", listas.getIdLista());


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


                                break;

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

    public static class ViewHolder extends  RecyclerView.ViewHolder {

        TextView tvnombreLista;
        TextView tvcantidadProductos;
        ImageButton ibmore;
        ProgressBar progressBar;
        LinearLayout linearCamigos;
        TextView tvAmigos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvnombreLista = itemView.findViewById(R.id.tvnombreLista);
            tvcantidadProductos = itemView.findViewById(R.id.tvcantidadProductos);
            ibmore = itemView.findViewById(R.id.ibmore);
            progressBar = itemView.findViewById(R.id.progressBar);
            linearCamigos = itemView.findViewById(R.id.linearCamigos);
            tvAmigos = itemView.findViewById(R.id.tvamigos);

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
