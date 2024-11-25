package com.example.superadmin.adminrest.dto;

import java.io.Serializable;

public class RestaurantDTO implements Serializable {
    private String nombreRestaurante;
    private String categoriaRestaurante;
    private String razonSocial;
    private String ruc;
    private String licenciaFuncionamiento;
    private String permisoSanitario;
    private String uidCreador;
    private String nombreCreador;
    private String uidCreacion;
    private String imageUrl;
    private String descripcion;
    private String location;

    public RestaurantDTO() {
    }

    public RestaurantDTO(String nombreRestaurante, String categoriaRestaurante, String razonSocial, String ruc, String licenciaFuncionamiento, String permisoSanitario, String uidCreador, String nombreCreador, String uidCreacion, String imageUrl, String descripcion, String location) {
        this.nombreRestaurante = nombreRestaurante;
        this.categoriaRestaurante = categoriaRestaurante;
        this.ruc = ruc;
        this.razonSocial = razonSocial;
        this.licenciaFuncionamiento = licenciaFuncionamiento;
        this.permisoSanitario = permisoSanitario;
        this.uidCreador = uidCreador;
        this.nombreCreador = nombreCreador;
        this.uidCreacion = uidCreacion;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
        this.location = location;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getCategoriaRestaurante() {
        return categoriaRestaurante;
    }

    public void setCategoriaRestaurante(String categoriaRestaurante) {
        this.categoriaRestaurante = categoriaRestaurante;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
