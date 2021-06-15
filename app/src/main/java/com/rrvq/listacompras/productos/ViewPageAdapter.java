package com.rrvq.listacompras.productos;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rrvq.listacompras.Constantes;

public class ViewPageAdapter extends FragmentStateAdapter {

    private final String responseDATA;

    public ViewPageAdapter(String responseDATA, @NonNull  FragmentManager fragmentManager, @NonNull  Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

        this.responseDATA = responseDATA;

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();

        switch (position){
            case 0:

                bundle.putString(Constantes.KEY_FRAGMET, responseDATA);
                fragment = new CheckFragment();

                fragment.setArguments(bundle);
                break;
            case 1:

                bundle.putString(Constantes.KEY_FRAGMET, responseDATA);
                fragment = new NoCheckFragment();

                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        // retorno la cantidad de tabs que cree
        return 2;
    }


}
