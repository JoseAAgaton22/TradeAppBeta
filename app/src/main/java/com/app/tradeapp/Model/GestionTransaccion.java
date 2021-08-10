package com.app.tradeapp.Model;

import com.app.tradeapp.Adapters.IngresosAdapter;

public class GestionTransaccion {

    private String categoria, descripcion, fecha_de_transaccion, id, valor;

    public GestionTransaccion(){

    }

    public GestionTransaccion(String categoria, String descripcion, String fecha_de_transaccion, String id, String valor) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.fecha_de_transaccion = fecha_de_transaccion;
        this.id = id;
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_de_transaccion() {
        return fecha_de_transaccion;
    }

    public void setFecha_de_transaccion(String fecha_de_transaccion) {
        this.fecha_de_transaccion = fecha_de_transaccion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
