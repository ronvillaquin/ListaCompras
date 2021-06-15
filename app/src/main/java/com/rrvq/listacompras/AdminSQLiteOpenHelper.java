package com.rrvq.listacompras;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {


    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase baseDeDatos) {

        baseDeDatos.execSQL("CREATE TABLE sesion(id_usuario text, email_usu text, nombre_usu text, apellido_usu text)");
        baseDeDatos.execSQL("CREATE TABLE pasarValores(idProducto text, nombreP text, precioP text, cantidadP text," +
                "notaP text, iconoP text, iconoPString text, checkP text, idLista text, id_usuarioCreador text, editable text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


    /*//Conexion a la base de datos SQLITE
    AdminSQLiteOpenHelper adminDB = new AdminSQLiteOpenHelper(this, "BDCartera", null, 1);
    SQLiteDatabase baseDeDatos = adminDB.getWritableDatabase();

// recupero el tipo de moneda
        fila = baseDeDatos.rawQuery("SELECT tipo_moneda FROM preferencias", null);
                if (fila.moveToFirst()) {
                tipo_moneda = fila.getString(0);
                }*/
