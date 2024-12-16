package com.example.superadmin.dtos;

import com.google.firebase.Timestamp;

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
    private String uidCreador;  // UID del creador
    private String createdBy;   // Nombre del creador
    private Timestamp createdAt; // Campo para fecha y hora de creación
    private String profileImage; // Nueva URL de la imagen de perfil

    // Constructor vacío requerido por Firestore
    public User() {
    }

    // Constructor con todos los parámetros
    public User(String name, String surname, String email, String dni, String phone, String address,
                String role, Boolean status, String uid, double latitude, double longitude,
                String uidCreador, String createdBy, Timestamp createdAt, String profileImage) {
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
        this.createdAt = createdAt;
        this.profileImage = profileImage; // Inicializar la imagen
    }

    public static User registrousuario(String name, String surname, String email, String dni, String phone,
                                       String address, String role, String uid, double latitude, double longitude,
                                       String uidCreador, String createdBy) {
        Timestamp createdAt = Timestamp.now(); // Obtener la fecha y hora actual
        return new User(name, surname, email, dni, phone, address, role, true, uid, latitude,
                longitude, uidCreador, createdBy, createdAt, null); // Sin imagen al inicio
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
