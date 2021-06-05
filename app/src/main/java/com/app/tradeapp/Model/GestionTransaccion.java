package com.app.tradeapp.Model;

import com.app.tradeapp.Adapters.IngresosAdapter;

public class GestionTransaccion {

    private String valor;

    public GestionTransaccion(){

    }

    public GestionTransaccion(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
