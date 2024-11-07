package com.example.superadmin.dtos;

public class User {
    private String name;
    private String surname;
    private String email;
    private String dni;
    private String phone;
    private String address;

    // Constructor vacío requerido por Firestore
    public User() {
    }

    // Constructor con parámetros (sin contraseña)
    public User(String name, String surname, String email, String dni, String phone, String address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.address = address;
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
}
