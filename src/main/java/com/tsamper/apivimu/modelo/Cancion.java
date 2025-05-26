package com.tsamper.apivimu.modelo;

public class Cancion {
    private int id;
    private String titulo;
    private Grupo grupo;
    private String enlaceYoutube;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getEnlaceYoutube() {
        return enlaceYoutube;
    }

    public void setEnlaceYoutube(String enlaceYoutube) {
        this.enlaceYoutube = enlaceYoutube;
    }
}
