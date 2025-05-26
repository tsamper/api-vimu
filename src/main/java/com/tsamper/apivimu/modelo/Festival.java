package com.tsamper.apivimu.modelo;

import java.time.LocalDate;
import java.util.HashSet;

public class Festival extends Evento{
    private HashSet<Grupo> grupos;

    public HashSet<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(HashSet<Grupo> grupos) {
        this.grupos = grupos;
    }
}
