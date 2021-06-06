package com.rrvq.listacompras.productos;

public class Iconos {

    String icono;
    String nombreC;
    String iconString;

    public Iconos(String icono, String nombreC, String iconString) {
        this.icono = icono;
        this.nombreC = nombreC;
        this.iconString = iconString;
    }


    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getNombreC() {
        return nombreC;
    }

    public void setNombreC(String nombreC) {
        this.nombreC = nombreC;
    }

    public String getIconString() {
        return iconString;
    }

    public void setIconString(String iconString) {
        this.iconString = iconString;
    }
}
