package com.rrvq.listacompras.listas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rrvq.listacompras.R;

import java.util.List;

public class AdapterListas extends BaseAdapter {

    Context context;
    List<GrupoListas> lista;

    public AdapterListas(Context context, List<GrupoListas> listview_listas) {
        this.context = context;
        this.lista = listview_listas;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView tvnombreLista;
        TextView tvcantidadProductos;
        ProgressBar progressBar;

        GrupoListas grupoListas = lista.get(position);

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_lista, null);

            tvnombreLista = convertView.findViewById(R.id.tvnombreLista);
            tvcantidadProductos = convertView.findViewById(R.id.tvcantidadProductos);
            progressBar = convertView.findViewById(R.id.progressBar);

            tvnombreLista.setText(grupoListas.nombreLista);
            tvcantidadProductos.setText(grupoListas.cantidadProductos);
            progressBar.setProgress(grupoListas.progressBar);
        }



        return convertView;
    }
}
