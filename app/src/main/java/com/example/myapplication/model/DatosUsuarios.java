package com.example.myapplication.model;

import java.io.Serializable;

public class DatosUsuarios implements Serializable {

    private String cedula;
    private String nombre;
    private String correo;
    private String direccion;
    private String colegio;
    private String telefono;
    private String nacimiento;
    private String trabajo;
    private String nombre_papa;
    private String sexo;
    private String uuid_hijos;

    public DatosUsuarios (){

    }

    public DatosUsuarios(String cedula, String nombre, String colegio, String correo, String nombre_papa,
                         String direccion, String telefono, String nacimiento, String trabajo, String sexo, String uuid_hijos) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.colegio = colegio;
        this.correo = correo;
        this.nombre_papa = nombre_papa;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nacimiento = nacimiento;
        this.trabajo = trabajo;
        this.sexo = sexo;
        this.uuid_hijos = uuid_hijos;
    }

    public String getUuid_hijos() {
        return uuid_hijos;
    }

    public void setUuid_hijos(String uuid_hijos) {
        this.uuid_hijos = uuid_hijos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getColegio() {
        return colegio;
    }

    public void setColegio(String colegio) {
        this.colegio = colegio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(String nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(String trabajo) {
        this.trabajo = trabajo;
    }

    public String getNombre_papa() {
        return nombre_papa;
    }

    public void setNombre_papa(String nombre_papa) {
        this.nombre_papa = nombre_papa;
    }
}
