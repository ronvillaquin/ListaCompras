package com.rrvq.listacompras.productos;


public class Productos {

    String idProducto;
    String nombreP;
    String precioP;
    String cantidadP;
    String notaP;
    String iconoP;
    String iconoPString;
    String checkP;
    String idLista;
    String id_usuarioCreador;
    String editable;

    public Productos(String idProducto, String nombreP, String precioP, String cantidadP, String notaP, String iconoP, String iconoPString, String checkP, String idLista, String id_usuarioCreador, String editable) {
        this.idProducto = idProducto;
        this.nombreP = nombreP;
        this.precioP = precioP;
        this.cantidadP = cantidadP;
        this.notaP = notaP;
        this.iconoP = iconoP;
        this.iconoPString = iconoPString;
        this.checkP = checkP;
        this.idLista = idLista;
        this.id_usuarioCreador = id_usuarioCreador;
        this.editable = editable;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreP() {
        return nombreP;
    }

    public void setNombreP(String nombreP) {
        this.nombreP = nombreP;
    }

    public String getPrecioP() {
        return precioP;
    }

    public void setPrecioP(String precioP) {
        this.precioP = precioP;
    }

    public String getCantidadP() {
        return cantidadP;
    }

    public void setCantidadP(String cantidadP) {
        this.cantidadP = cantidadP;
    }

    public String getNotaP() {
        return notaP;
    }

    public void setNotaP(String notaP) {
        this.notaP = notaP;
    }

    public String getIconoP() {
        return iconoP;
    }

    public void setIconoP(String iconoP) {
        this.iconoP = iconoP;
    }

    public String getIconoPString() {
        return iconoPString;
    }

    public void setIconoPString(String iconoPString) {
        this.iconoPString = iconoPString;
    }

    public String getCheckP() {
        return checkP;
    }

    public void setCheckP(String checkP) {
        this.checkP = checkP;
    }

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }

    public String getId_usuarioCreador() {
        return id_usuarioCreador;
    }

    public void setId_usuarioCreador(String id_usuarioCreador) {
        this.id_usuarioCreador = id_usuarioCreador;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }
}
