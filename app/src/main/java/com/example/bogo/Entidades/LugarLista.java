package com.example.bogo.Entidades;

public class LugarLista {

    Lugar lugar = new Lugar();
    String id;

    public LugarLista() {
    }

    public LugarLista(Lugar lugar, String id) {
        this.lugar = lugar;
        this.id = id;
    }

    public Lugar getLugar() {
        return lugar;
    }

    public String getId() {
        return id;
    }

    public void setLugar(Lugar lugar) {
        this.lugar = lugar;
    }

    public void setId(String id) {
        this.id = id;
    }
}
