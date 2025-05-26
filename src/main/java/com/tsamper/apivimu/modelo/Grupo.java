package com.tsamper.apivimu.modelo;

public class Grupo {
    private int id;
    private String nombre;
    private String descripcion;
    private String genero;
    private String ciudad;
    private String pais;
    private String imagen;
    private String perfilSpotify;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPerfilSpotify() {
        return perfilSpotify;
    }

    public void setPerfilSpotify(String perfilSpotify) {
        this.perfilSpotify = perfilSpotify;
    }
}
