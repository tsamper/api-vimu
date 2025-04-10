package com.tsamper.apivimu.modelo;

import com.tsamper.apivimu.modelo.enums.Privilegios;

public class GrupoUsuarios {
    private int id;
    private Privilegios tipo;

    public GrupoUsuarios(Privilegios tipo) {
        this.tipo = tipo;
    }

    public GrupoUsuarios() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Privilegios getTipo() {
        return tipo;
    }

    public void setTipo(Privilegios tipo) {
        this.tipo = tipo;
    }
}
