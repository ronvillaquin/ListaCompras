package com.rrvq.listacompras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constantes {

    Context context;

    public static final String INTERSTICIAL = "ca-app-pub-3019606122027900/8749526208"; // este es mio
//    public static final String INTERSTICIAL = "ca-app-pub-3940256099942544/1033173712";  // este es de prueba

    public static final String KEY_FRAGMET = "DATA";

    //PARA EL PASE DE PARAMETROS DE ACTIVITY A FRAGMENT O FRAGMENT A FRAGMENT
    public static final String KEY_ID_LISTA = "KEY_ID_LISTA";
    public static final String KEY_ADD_EDIT = "KEY_ADD_EDIT";
    public static final String KEY_ID_P = "KEY_ID_P";
    public static final String KEY_NOMBRE_P = "KEY_NOMBRE_P";
    public static final String KEY_PRECIO_P = "KEY_PRECIO_P";
    public static final String KEY_CANTIDAD_P = "KEY_CANTIDAD_P";
    public static final String KEY_NOTA_P = "KEY_NOTA_P";
    public static final String KEY_ICONO_P = "KEY_ICONO_P";


    public Constantes (Context context){
        this.context = context;
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


}
