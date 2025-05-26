package com.tsamper.apivimu.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {
    private int id;
    private String nombre;
    private String imagen;
    private Recinto recinto;
    private LocalDate fecha;
    private LocalTime hora;
    private int cantidadEntradasVip;
    private int cantidadEntradasVipVendidas;
    private double precioEntradasVip;
    private int cantidadEntradas;
    private int cantidadEntradasVendidas;
    private double precioEntradas;
    private Usuario promotor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void setRecinto(Recinto recinto) {
        this.recinto = recinto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getCantidadEntradasVip() {
        return cantidadEntradasVip;
    }

    public void setCantidadEntradasVip(int cantidadEntradasVip) {
        this.cantidadEntradasVip = cantidadEntradasVip;
    }

    public double getPrecioEntradasVip() {
        return precioEntradasVip;
    }

    public void setPrecioEntradasVip(double precioEntradasVip) {
        this.precioEntradasVip = precioEntradasVip;
    }

    public int getCantidadEntradas() {
        return cantidadEntradas;
    }

    public void setCantidadEntradas(int cantidadEntradas) {
        this.cantidadEntradas = cantidadEntradas;
    }

    public double getPrecioEntradas() {
        return precioEntradas;
    }

    public void setPrecioEntradas(double precioEntradas) {
        this.precioEntradas = precioEntradas;
    }

    public int getCantidadEntradasVipVendidas() {
        return cantidadEntradasVipVendidas;
    }

    public void setCantidadEntradasVipVendidas(int cantidadEntradasVipVendidas) {
        this.cantidadEntradasVipVendidas = cantidadEntradasVipVendidas;
    }

    public int getCantidadEntradasVendidas() {
        return cantidadEntradasVendidas;
    }

    public void setCantidadEntradasVendidas(int cantidadEntradasVendidas) {
        this.cantidadEntradasVendidas = cantidadEntradasVendidas;
    }

    public Usuario getPromotor() {
        return promotor;
    }

    public void setPromotor(Usuario promotor) {
        this.promotor = promotor;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
