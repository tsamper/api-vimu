package com.tsamper.apivimu.modelo;

import java.time.LocalDate;

public class Concierto extends Evento{
    private Grupo grupo;

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }



}
