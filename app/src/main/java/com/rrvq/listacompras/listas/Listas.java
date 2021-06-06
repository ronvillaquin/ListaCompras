package com.rrvq.listacompras.listas;

public class Listas {

    public String idLista;
    public String nombreLista;
    public String id_usuario;
    public String totalP;
    public String checkP;
    public String cantida_amigos;

    public Listas(String idLista, String nombreLista, String id_usuario, String totalP, String checkP, String cantida_amigos) {
        this.idLista = idLista;
        this.nombreLista = nombreLista;
        this.id_usuario = id_usuario;
        this.totalP = totalP;
        this.checkP = checkP;
        this.cantida_amigos = cantida_amigos;
    }

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }

    public String getNombreLista() {
        return nombreLista;
    }

    public void setNombreLista(String nombreLista) {
        this.nombreLista = nombreLista;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTotalP() {
        return totalP;
    }

    public void setTotalP(String totalP) {
        this.totalP = totalP;
    }

    public String getCheckP() {
        return checkP;
    }

    public void setCheckP(String checkP) {
        this.checkP = checkP;
    }

    public String getCantida_amigos() {
        return cantida_amigos;
    }

    public void setCantida_amigos(String cantida_amigos) {
        this.cantida_amigos = cantida_amigos;
    }
}
