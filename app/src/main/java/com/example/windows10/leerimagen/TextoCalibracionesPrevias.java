package com.example.windows10.leerimagen;

public class TextoCalibracionesPrevias {
    private String micro;
    private String objetivo;
    private String patron;
    private String escala;
    public TextoCalibracionesPrevias(String micro, String objetivo, String patron, String escala){
        this.micro = micro;
        this.objetivo = objetivo;
        this.patron = patron;
        this.escala = escala;
    }

    public String getMicro() {
        return micro;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public String getPatron() {
        return patron;
    }

    public String getEscala() {
        return escala;
    }
}
