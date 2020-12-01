package com.example.bogo.Entidades;

public class Cupon {
   private String nombre;
    private int valorEnPuntos;
    Long fechaLimite;
    String id;

    public Cupon()
    {

    }

    public Cupon(int valorEnPuntos, Long fechaLimite,String nombre,String id) {
        this.valorEnPuntos = valorEnPuntos;
        this.fechaLimite = fechaLimite;
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getValorEnPuntos() {
        return valorEnPuntos;
    }

    public void setValorEnPuntos(int valorEnPuntos) {
        this.valorEnPuntos = valorEnPuntos;
    }

    public Long getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Long fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}
