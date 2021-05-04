package com.app.tradeapp.Model;

public class User {

    private String id;
    private String nombres;
    private String apellidos;
    private String imgURL;
    private String correo;

    public User (String id, String nombres, String apellidos, String imgURL, String correo){
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this. imgURL = imgURL;
        this.correo = correo;
    }

    public User(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
