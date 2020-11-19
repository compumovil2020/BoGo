package com.example.bogo.Entidades;

import com.google.firebase.database.Exclude;

public class Lugar {
    String correoElectronico;
    String descripcion;
    String direccion;
    String horaApertura;
    String horaCierre;
    double latitud;
    double longitud;
    String nombre;
    double promedio;
    long telefono;
    String tipo;
    int precioMaximo;
    int precioMinimo;

    public Lugar()
    {

    }

    public Lugar(String correoElectronico, String descripcion, String direccion, String horaApertura, String horaCierre, double latitud, double longitud, String nombre, double promedio, long telefono, String tipo, int precioMaximo, int precioMinimo) {
        this.correoElectronico = correoElectronico;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombre = nombre;
        this.promedio = promedio;
        this.telefono = telefono;
        this.tipo = tipo;
        this.precioMaximo = precioMaximo;
        this.precioMinimo = precioMinimo;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
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

    public int getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(int precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public int getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(int precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    @Override
    @Exclude
    public String toString() {
        return "Lugar{" +
                "correo='" + correoElectronico + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", horaApertura='" + horaApertura + '\'' +
                ", horaCierre='" + horaCierre + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", nombre='" + nombre + '\'' +
                ", promedio=" + promedio +
                ", telefono=" + telefono +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
