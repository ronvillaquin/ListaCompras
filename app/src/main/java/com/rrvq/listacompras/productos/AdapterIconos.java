package com.rrvq.listacompras.productos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rrvq.listacompras.R;


import java.util.List;

public class AdapterIconos extends RecyclerView.Adapter<AdapterIconos.ViewHolder> implements View.OnClickListener {

    private final LayoutInflater layoutInflater;
    private final List<Iconos> dataIconos;
    private Context context;

    private View.OnClickListener listener;

    public AdapterIconos(Context context, List<Iconos> dataIconos){
        this.layoutInflater = LayoutInflater.from(context);
        this.dataIconos = dataIconos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.recyclerview_iconos, parent, false);
        //con este escucha los envento de la lista
        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        final Iconos iconos = dataIconos.get(position);

        //para no reciclar las vistas y que no se cambien de posicion los elementos
        //l desabilito
        holder.setIsRecyclable(false);

//        int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());

        holder.icono.setImageResource(Integer.parseInt(iconos.getIcono()));
        holder.nombreC.setText(iconos.getNombreC());


    }

    @Override
    public int getItemCount() {
        return dataIconos.size();
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

        ImageView icono;
        TextView nombreC;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icono = itemView.findViewById(R.id.imgIcono);
            nombreC = itemView.findViewById(R.id.tvnombreProductoIcono);


        }
    }


}
