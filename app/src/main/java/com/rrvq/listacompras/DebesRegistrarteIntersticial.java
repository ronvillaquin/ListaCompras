package com.rrvq.listacompras;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;

import com.rrvq.listacompras.sesion.Registrar;


public class DebesRegistrarteIntersticial {

    Activity activity;

    public DebesRegistrarteIntersticial(Activity activity){
        this.activity = activity;
    }

    public void dialogoRegistrarce(){

        final AlertDialog.Builder dialogo = new AlertDialog.Builder(activity);

        dialogo.setTitle(activity.getResources().getString(R.string.debesregistrarte));
        dialogo.setMessage(activity.getResources().getString(R.string.desbesbody));
//                            dialogo.setCancelable(false);

        //para el bootn aceptar del dialogo
        dialogo.setPositiveButton(activity.getResources().getString(R.string.si), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {

                //Conexion a la base de datos SQLITE
                AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(activity, "BDListas", null, 1);
                SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();


                baseDeDatos.delete("sesion", "rowid="+ 1,null);
                baseDeDatos.close();

                Intent intent = new Intent(activity, Registrar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                activity.finish();

            }
        });
        // para el boton cancelar del dialogo
        dialogo.setNegativeButton(activity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo, int id) {
                // accion si da cancelar que no haga nada
                dialogo.cancel();
            }
        });
        dialogo.show();

    }


}
