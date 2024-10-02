package com.example.superadmin.model;

import android.content.Intent;

public class user {
    private String name;
    private String lastName;     // Estilo de nombres en camelCase
    private String  DNI;            // Si el DNI es numérico
    private String correo;
    private String numberPhone;  // Estilo de nombres en camelCase
    private String direction;    // Corregido el nombre a "direction"
    private String rol;
    private boolean status;      // Cambiado a boolean para activo/inactivo

    public user(String name, String lastName, String  DNI, String correo, String numberPhone, String direction, String rol, boolean status) {
        this.name = name;
        this.lastName = lastName;
        this.DNI = DNI;
        this.correo = correo;
        this.numberPhone = numberPhone;
        this.direction = direction;
        this.rol = rol;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String  getDNI() {
        return DNI;
    }

    public String getCorreo() {
        return correo;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public String getDirection() {
        return direction;
    }

    public String getRol() {
        return rol;
    }

    public boolean isStatus() {
        return status;
    }
}

