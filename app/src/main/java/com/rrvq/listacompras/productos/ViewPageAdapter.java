package com.rrvq.listacompras.productos;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageAdapter extends FragmentStateAdapter {

    public ViewPageAdapter(@NonNull  FragmentManager fragmentManager, @NonNull  Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new Fragment();
        switch (position){
            case 0:
                fragment = new CheckFragment();
                break;
            case 1:
                fragment = new NoCheckFragment();
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
