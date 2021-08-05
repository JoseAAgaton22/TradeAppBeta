package com.app.tradeapp.Model;

import android.media.Image;

public class Perfil {

    private String nombres;
    private String apellidos;
    private String correo;
    private String imagen;
    private String bio;
    private String cuenta;

    public Perfil() {

    }

    public Perfil(String nombres, String apellidos, String correo, String imagen, String bio, String cuenta) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.imagen = imagen;
        this.bio = bio;
        this.cuenta = cuenta;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }
}
