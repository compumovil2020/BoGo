package com.example.bogo.Entidades;

public class Resenia {
    double calificacion;
    String texto;
    String UID;
    String username;
    String titulo;

    public Resenia() {

    }
    public Resenia(double calificacion, String texto, String UID, String username, String titulo) {
        this.calificacion = calificacion;
        this.texto = texto;
        this.UID = UID;
        this.username = username;
        this.titulo = titulo;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
