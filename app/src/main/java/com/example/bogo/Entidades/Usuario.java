package com.example.bogo.Entidades;

import java.util.ArrayList;

public class Usuario
{
    private double latitud;
    private double longitud;
    private String nombre;
    private String nombreUsuario;
    private int puntos;
    private ArrayList<String> cupones;

    public Usuario() {
        this.cupones = new ArrayList<>();
    }

    public Usuario(double latitud, double longitud, String nombre, String nombreUsuario, int puntos,ArrayList<String>cupones) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.nombreUsuario = nombreUsuario;
        this.puntos = puntos;
        this.cupones = cupones;
    }

    public ArrayList<String> getCupones() {
        return cupones;
    }

    public void setCupones(ArrayList<String> cupones) {
        this.cupones = cupones;
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
