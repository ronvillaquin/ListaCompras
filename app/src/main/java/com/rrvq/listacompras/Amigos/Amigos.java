package com.rrvq.listacompras.Amigos;

public class Amigos {

    String id_usuario;
    String email_usu;
    String nombre_usu;
    String apellido_usu;
    String id_lista;
    String id_usuarioActivo;
    String editable_shared;
    String enableIbMore;

    public Amigos(String id_usuario, String email_usu, String nombre_usu, String apellido_usu, String id_lista, String id_usuarioActivo, String editable_shared, String enableIbMore) {
        this.id_usuario = id_usuario;
        this.email_usu = email_usu;
        this.nombre_usu = nombre_usu;
        this.apellido_usu = apellido_usu;
        this.id_lista = id_lista;
        this.id_usuarioActivo = id_usuarioActivo;
        this.editable_shared = editable_shared;
        this.enableIbMore = enableIbMore;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getEmail_usu() {
        return email_usu;
    }

    public void setEmail_usu(String email_usu) {
        this.email_usu = email_usu;
    }

    public String getNombre_usu() {
        return nombre_usu;
    }

    public void setNombre_usu(String nombre_usu) {
        this.nombre_usu = nombre_usu;
    }

    public String getApellido_usu() {
        return apellido_usu;
    }

    public void setApellido_usu(String apellido_usu) {
        this.apellido_usu = apellido_usu;
    }

    public String getId_lista() {
        return id_lista;
    }

    public void setId_lista(String id_lista) {
        this.id_lista = id_lista;
    }

    public String getId_usuarioActivo() {
        return id_usuarioActivo;
    }

    public void setId_usuarioActivo(String id_usuarioActivo) {
        this.id_usuarioActivo = id_usuarioActivo;
    }

    public String getEditable_shared() {
        return editable_shared;
    }

    public void setEditable_shared(String editable_shared) {
        this.editable_shared = editable_shared;
    }

    public String getEnableIbMore() {
        return enableIbMore;
    }

    public void setEnableIbMore(String enableIbMore) {
        this.enableIbMore = enableIbMore;
    }
}
