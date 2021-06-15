package com.rrvq.listacompras.productos;

public class ComunicacionFrag {

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

    public ComunicacionFrag(String idProducto, String nombreP, String precioP, String cantidadP, String notaP, String iconoP, String iconoPString, String checkP, String idLista, String id_usuarioCreador, String editable) {
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
}
