package com.example.superadmin.dtos;

import com.example.superadmin.util.Constants;
import com.google.android.gms.common.api.internal.IStatusCallback;

public class User {
    private String name;
    private String surname;
    private String email;
    private String dni;
    private String phone;
    private String address;
    private String role;
    private Boolean status;
    private String uid;


    // Constructor vacío requerido por Firestore
    public User() {
    }

    // Constructor con todos los parámetros
    public User(String name, String surname, String email, String dni, String phone, String address, String role, Boolean status, String uid) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.status = (status != null) ? status : true;  // Si no se pasa un valor, se asigna true
        this.uid = uid;
    }



    // Método estático para el registro de cliente (solo cliente la cual proviene de registro al inicio de la app)-
    public static User registrousuario(String name, String surname, String email, String dni, String phone, String address, String uid) {
        return new User(name, surname, email, dni, phone, address, Constants.ROLE_CLIENTE, true, uid);
    }

    // Getters y setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}