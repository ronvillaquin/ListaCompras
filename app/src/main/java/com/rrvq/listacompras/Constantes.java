package com.rrvq.listacompras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constantes {

    Context context;

    public static final String INTERSTICIAL = "ca-app-pub-3019606122027900/8749526208"; // este es mio
//    public static final String INTERSTICIAL = "ca-app-pub-3940256099942544/1033173712";  // este es de prueba

    public static final String KEY_FRAGMET = "DATA";

    public Constantes (Context context){
        this.context = context;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


}
