package com.example.bogo.Entidades;

public class Lugar {
    String correo;
    String descripción;
    String direccion;
    String horaAper;
    String horaCierre;
    double latitud;
    double longitud;
    String nombre;
    double promedio;
    long telefono;
    String tipo;

    public Lugar()
    {

    }

    public Lugar(String correo, String descripción, String direccion, String horaAper, String horaCierre, double latitud, double longitud, String nombre, double promedio, long telefono, String tipo) {
        this.correo = correo;
        this.descripción = descripción;
        this.direccion = direccion;
        this.horaAper = horaAper;
        this.horaCierre = horaCierre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.promedio = promedio;
        this.telefono = telefono;
        this.tipo = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDescripción() {
        return descripción;
    }

    public void setDescripción(String descripción) {
        this.descripción = descripción;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHoraAper() {
        return horaAper;
    }

    public void setHoraAper(String horaAper) {
        this.horaAper = horaAper;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
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

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
