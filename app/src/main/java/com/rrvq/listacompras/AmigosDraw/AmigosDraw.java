package com.rrvq.listacompras.AmigosDraw;

public class AmigosDraw {

    String id_usuario;
    String email_usu;
    String nombre_usu;
    String apellido_usu;

    public AmigosDraw(String id_usuario, String email_usu, String nombre_usu, String apellido_usu) {
        this.id_usuario = id_usuario;
        this.email_usu = email_usu;
        this.nombre_usu = nombre_usu;
        this.apellido_usu = apellido_usu;
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
}
