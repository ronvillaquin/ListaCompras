package com.rrvq.listacompras.productos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rrvq.listacompras.Constantes;
import com.rrvq.listacompras.R;


import java.util.ArrayList;

public class CheckFragment extends Fragment {

    View view;
    RecyclerView recyclerViewCheck;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Productos> data = new ArrayList<>();
    AdapterProductos adapterProductos;
    String responseDATA;

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


        return view;
    }

    private void casting(){

        recyclerViewCheck = view.findViewById(R.id.recyclerviewCheck);
        swipeRefreshLayout = view.findViewById(R.id.refreshRecycler);

    }



}