package com.tsamper.apivimu.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Entrada {
    private int id;
    private double precio;
    private Usuario usuario;
    private String tipo;
    private LocalDateTime fechaCompra;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    @Override
    public String toString() {
        return "Entrada{" +
                "id=" + id +
                ", precio=" + precio +
                ", usuario=" + usuario +
                ", tipo='" + tipo + '\'' +
                ", fechaCompra=" + fechaCompra +
                '}';
    }
}
