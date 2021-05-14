package com.app.tradeapp.Model;

public class Subvalor {

    private String subvalor_nombre;
    private double subvalor_valor;

    public Subvalor(String subvalor_nombre, double subvalor_valor) {
        this.subvalor_nombre = subvalor_nombre;
        this.subvalor_valor = subvalor_valor;
    }

    public Subvalor() {
    }

    public String getSubvalor_nombre() {
        return subvalor_nombre;
    }

    public void setSubvalor_nombre(String subvalor_nombre) {
        this.subvalor_nombre = subvalor_nombre;
    }

    public double getSubvalor_valor() {
        return subvalor_valor;
    }

    public void setSubvalor_valor(double subvalor_valor) {
        this.subvalor_valor = subvalor_valor;
    }
}
