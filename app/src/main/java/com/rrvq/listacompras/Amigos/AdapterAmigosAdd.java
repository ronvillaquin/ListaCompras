package com.rrvq.listacompras.Amigos;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.rrvq.listacompras.AmigosDraw.AmigosDraw;
import com.rrvq.listacompras.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterAmigosAdd extends RecyclerView.Adapter<AdapterAmigosAdd.ViewHolder> implements View.OnClickListener {

    private LayoutInflater layoutInflater;
    private List<AmigosAdd> dataAmigos;
    private Context context;

    private ProgressDialog progressDialog;

    private View.OnClickListener listener;

    public AdapterAmigosAdd(Context context, List<AmigosAdd> dataAmigos){
        this.layoutInflater = LayoutInflater.from(context);
        this.dataAmigos = dataAmigos;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterAmigosAdd.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recyclerview_amigos_add, parent, false);
        //con este escucha los envento de la lista
        view.setOnClickListener(this);

        return new AdapterAmigosAdd.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAmigosAdd.ViewHolder holder, final int position) {


        final AmigosAdd amigos = dataAmigos.get(position);

        //para no reciclar las vistas y que no se cambien de posicion los elementos
        //l desabilito
        holder.setIsRecyclable(false);

        String nombre = amigos.getNombre_usu() + " " + amigos.getApellido_usu();
        holder.tvnombreAmigo.setText(nombre);

        holder.tvemailAmigo.setText(amigos.getEmail_usu());


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
        ImageButton ibAmigoAdd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvnombreAmigo = itemView.findViewById(R.id.tvNombreAmigo);
            tvemailAmigo = itemView.findViewById(R.id.tvEmailAmigo);
            ibAmigoAdd = itemView.findViewById(R.id.ibmore);


        }
    }

}