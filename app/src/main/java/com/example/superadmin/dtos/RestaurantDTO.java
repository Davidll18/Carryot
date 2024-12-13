package com.example.superadmin.dtos;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class RestaurantDTO {
    private String nombreRestaurante;
    private String categoria;
    private String razonSocial;
    private String ruc;
    private String licenciaFuncionamiento;
    private String permisoSanitario;
    private String uidCreador;  // UID de la persona que crea el restaurante
    private String nombreCreador; // Nombre de la persona que crea el restaurante
    private String uidCreacion;  // UID de la creación (por ejemplo, el UID del restaurante)
    private String descripcion; // Descripción del restaurante
    private double latitud; // Latitud seleccionada en el mapa
    private double longitud; // Longitud seleccionada en el mapa
    private String imageUrl;
    // Constructor vacío requerido por Firestore
    public RestaurantDTO() {
    }

    // Constructor con parámetros


    public RestaurantDTO(String nombreRestaurante, String categoria,
                         String razonSocial, String ruc,
                         String licenciaFuncionamiento,
                         String permisoSanitario, String descripcion,
                         double latitud, double longitud,
                         String uidCreador, String nombreCreador,
                         String uidCreacion, String imageUrl) {
        this.nombreRestaurante = nombreRestaurante;
        this.categoria = categoria;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.licenciaFuncionamiento = licenciaFuncionamiento;
        this.permisoSanitario = permisoSanitario;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.uidCreador = uidCreador;
        this.nombreCreador = nombreCreador;
        this.uidCreacion = uidCreacion;
        this.imageUrl = imageUrl;
    }

    // Getters y Setters

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getLicenciaFuncionamiento() {
        return licenciaFuncionamiento;
    }

    public void setLicenciaFuncionamiento(String licenciaFuncionamiento) {
        this.licenciaFuncionamiento = licenciaFuncionamiento;
    }

    public String getPermisoSanitario() {
        return permisoSanitario;
    }

    public void setPermisoSanitario(String permisoSanitario) {
        this.permisoSanitario = permisoSanitario;
    }

    public String getUidCreador() {
        return uidCreador;
    }

    public void setUidCreador(String uidCreador) {
        this.uidCreador = uidCreador;
    }

    public String getNombreCreador() {
        return nombreCreador;
    }

    public void setNombreCreador(String nombreCreador) {
        this.nombreCreador = nombreCreador;
    }

    public String getUidCreacion() {
        return uidCreacion;
    }

    public void setUidCreacion(String uidCreacion) {
        this.uidCreacion = uidCreacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "RestaurantDTO{" +
                "nombreRestaurante='" + nombreRestaurante + '\'' +
                ", categoria='" + categoria + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", ruc='" + ruc + '\'' +
                ", licenciaFuncionamiento='" + licenciaFuncionamiento + '\'' +
                ", permisoSanitario='" + permisoSanitario + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", uidCreador='" + uidCreador + '\'' +
                ", nombreCreador='" + nombreCreador + '\'' +
                ", uidCreacion='" + uidCreacion + '\'' +
                '}';
    }
}
