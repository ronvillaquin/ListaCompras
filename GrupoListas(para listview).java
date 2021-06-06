package com.rrvq.listacompras.listas;

import android.widget.ProgressBar;

public class GrupoListas {

    public int idLista;
    public String nombreLista;
    public String cantidadProductos;
    public int progressBar;

    public GrupoListas(int idLista, String nombreLista, String cantidadProductos, int progressBar) {
        this.idLista = idLista;
        this.nombreLista = nombreLista;
        this.cantidadProductos = cantidadProductos;
        this.progressBar = progressBar;
    }


    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public String getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(String cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public int getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(int progressBar) {
        this.progressBar = progressBar;
    }
}
