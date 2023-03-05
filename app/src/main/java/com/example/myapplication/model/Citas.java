package com.example.myapplication.model;

import java.io.Serializable;

public class Citas implements Serializable {

    private String peso;
    private String edad;
    private String circunferencia_brazo;
    private String talla;
    private String imc; //peso/estatura^2
    private String estatura;
    private String ganancia_peso;
    private String fecha;
    private boolean embarazada;
    private String uuid;
    private String semanas;

    public Citas (){
    }

    public Citas(String edad, String peso, String talla, String circunferencia_brazo, String imc,
                 String estatura, String ganancia_peso, String fecha, String uuid, boolean embarazada, String semanas) {
        this.edad = edad;
        this.peso = peso;
        this.circunferencia_brazo = circunferencia_brazo;
        this.talla = talla;
        this.imc = imc;
        this.estatura = estatura;
        this.ganancia_peso = ganancia_peso;
        this.embarazada = embarazada;
        this.fecha = fecha;
        this.uuid = uuid;
        this.semanas = semanas;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setImc(String imc) {
        this.imc = imc;
    }

    public String getImc() {
        return imc;
    }

    public String getEstatura() {
        return estatura;
    }

    public String getSemanas() {
        return semanas;
    }

    public void setSemanas(String semanas) {
        this.semanas = semanas;
    }

    public void setEstatura(String estatura) {
        this.estatura = estatura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public boolean isEmbarazada() {
        return embarazada;
    }

    public void setEmbarazada(boolean embarazada) {
        this.embarazada = embarazada;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCircunferencia_brazo() {
        return circunferencia_brazo;
    }

    public void setCircunferencia_brazo(String circunferencia_brazo) {
        this.circunferencia_brazo = circunferencia_brazo;
    }

    public String getGanancia_peso() {
        return ganancia_peso;
    }

    public void setGanancia_peso(String ganancia_peso) {
        this.ganancia_peso = ganancia_peso;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

}
