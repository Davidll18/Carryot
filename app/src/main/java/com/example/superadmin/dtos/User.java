package com.example.superadmin.dtos;

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
    private double latitude;
    private double longitude;
    private String uidCreador;  // Nuevo campo para l uid del creador
    private String createdBy;   // Nuevo campo para el nombre completo del creador

    // Constructor vacío requerido por Firestore
    public User() {
    }

    // Constructor con todos los parámetros
    public User(String name, String surname, String email, String dni, String phone, String address, String role, Boolean status, String uid, double latitude, double longitude, String uidCreador, String createdBy) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.address = address;
        this.role = role;
        this.status = (status != null) ? status : true;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uidCreador = uidCreador;
        this.createdBy = createdBy;
    }

    public static User registrousuario(String name, String surname, String email, String dni, String phone, String address, String role, String uid, double latitude, double longitude, String uidCreador, String createdBy) {
        return new User(name, surname, email, dni, phone, address, role, true, uid, latitude, longitude, uidCreador, createdBy);
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = (status != null) ? status : true;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getUidCreador() {
        return uidCreador;
    }

    public void setUidCreador(String uidCreador) {
        this.uidCreador = uidCreador;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
