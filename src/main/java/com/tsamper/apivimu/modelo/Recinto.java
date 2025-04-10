package com.tsamper.apivimu.modelo;

public class Recinto {
    private int id;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String telefono;
    private String email;
    private String enlaceMaps;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEnlaceMaps() {
        return enlaceMaps;
    }

    public void setEnlaceMaps(String enlaceMaps) {
        this.enlaceMaps = enlaceMaps;
    }
}
