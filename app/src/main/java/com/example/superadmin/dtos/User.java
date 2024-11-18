package com.example.superadmin.dtos;

import com.example.superadmin.util.Constants;

public class User {
    private String name;
    private String surname;
    private String email;
    private String dni;
    private String phone;
    private String address;
    private String role;
    private String uid;
    private String recoveryCode;
    private long recoveryCodeTimestamp; // Momento en que se generó el código
    private long recoveryCodeValidity; // Tiempo de validez en milisegundos

    // Constructor vacío requerido por Firestore
    public User() {
    }

    // Constructor con todos los parámetros
    public User(String name, String surname, String email, String dni, String phone, String address, String role, String uid, String recoveryCode, long recoveryCodeTimestamp, long recoveryCodeValidity) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.uid = uid;
        this.recoveryCode = recoveryCode;
        this.recoveryCodeTimestamp = recoveryCodeTimestamp;
        this.recoveryCodeValidity = recoveryCodeValidity;
    }

    // Método estático para el registro de usuario
    public static User registrousuario(String name, String surname, String email, String dni, String phone, String address, String uid) {
        return new User(name, surname, email, dni, phone, address, Constants.ROLE_CLIENTE, uid, null, 0, 0);
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

    public String getRecoveryCode() {
        return recoveryCode;
    }

    public void setRecoveryCode(String recoveryCode) {
        this.recoveryCode = recoveryCode;
    }

    public long getRecoveryCodeTimestamp() {
        return recoveryCodeTimestamp;
    }

    public void setRecoveryCodeTimestamp(long recoveryCodeTimestamp) {
        this.recoveryCodeTimestamp = recoveryCodeTimestamp;
    }

    public long getRecoveryCodeValidity() {
        return recoveryCodeValidity;
    }

    public void setRecoveryCodeValidity(long recoveryCodeValidity) {
        this.recoveryCodeValidity = recoveryCodeValidity;
    }
}