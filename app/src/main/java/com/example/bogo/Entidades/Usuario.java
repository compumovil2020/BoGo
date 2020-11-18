package com.example.bogo.Entidades;

public class Usuario
{
    private double latitud;
    private double longitud;
    private String nombre;
    private String nombreUsuario;
    private int puntos;

    public Usuario() {
    }

    public Usuario(double latitud, double longitud, String nombre, String nombreUsuario, int puntos) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.puntos = puntos;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
