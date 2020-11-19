package com.example.bogo.Entidades;

public class Mensaje{
    String texto;
    String remitente;
    Long fechaHoraEnviado;

    public Mensaje(){

    }
    public Mensaje(String texto, String remitente, Long fechaHoraEnviado) {
        this.texto = texto;
        this.remitente = remitente;
        this.fechaHoraEnviado = fechaHoraEnviado;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public Long getFechaHoraEnviado() {
        return fechaHoraEnviado;
    }

    public void setFechaHoraEnviado(Long fechaHoraEnviado) {
        this.fechaHoraEnviado = fechaHoraEnviado;
    }
}