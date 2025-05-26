package com.tsamper.apivimu.modelo;

public class EntradaConcierto extends Entrada{
    private Concierto concierto;

    public Concierto getConcierto() {
        return concierto;
    }

    public void setConcierto(Concierto concierto) {
        this.concierto = concierto;
    }

    @Override
    public String toString() {
        return "EntradaConcierto{" +
                super.toString() +
                "concierto=" + concierto +
                '}';
    }
}
