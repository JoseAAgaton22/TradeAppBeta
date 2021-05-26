package com.app.tradeapp.Model;

public class Categorias {

    private int id;
    private  int categoria;
    private String nombre;

    public Categorias(int id, int categoria, String nombre) {
        this.id = id;
        this.categoria = categoria;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
