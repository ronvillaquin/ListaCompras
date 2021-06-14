package com.rrvq.listacompras.productos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rrvq.listacompras.R;

public class NoCheckFragment extends Fragment {

    View view;

    public NoCheckFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_no_check, container, false);

        Toast.makeText(getContext(), "Hola prueba 2", Toast.LENGTH_SHORT).show();



        return view;
    }
}