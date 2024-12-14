package com.example.superadmin.dtos;

public class PlatoDTO {
    private String nombrePlato;
    private String categoriaPlato;
    private String descripcion;
    private String precio;
    private int cantidad;
    private boolean disponible;
    private String uidCreador;
    private String nombreCreador;
    private String uidCreacion;
    private String uidRestaurante;
    private String imageUrl;

    public PlatoDTO(int cantidad, String categoriaPlato, String descripcion,
                    boolean disponible, String imageUrl, String nombreCreador,
                    String uidCreacion, String nombrePlato, String precio, String uidCreador, String uidRestaurante) {
        this.cantidad = cantidad;
        this.categoriaPlato = categoriaPlato;
        this.descripcion = descripcion;
        this.disponible = disponible;
        this.imageUrl = imageUrl;
        this.nombreCreador = nombreCreador;
        this.uidCreacion = uidCreacion;
        this.nombrePlato = nombrePlato;
        this.precio = precio;
        this.uidCreador = uidCreador;
        this.uidRestaurante = uidRestaurante;
    }

    public String getUidRestaurante() {
        return uidRestaurante;
    }

    public void setUidRestaurante(String uidRestaurante) {
        this.uidRestaurante = uidRestaurante;
    }

    public String getUidCreacion() {
        return uidCreacion;
    }

    public void setUidCreacion(String uidCreacion) {
        this.uidCreacion = uidCreacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoriaPlato() {
        return categoriaPlato;
    }

    public void setCategoriaPlato(String categoriaPlato) {
        this.categoriaPlato = categoriaPlato;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNombreCreador() {
        return nombreCreador;
    }

    public void setNombreCreador(String nombreCreador) {
        this.nombreCreador = nombreCreador;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getUidCreador() {
        return uidCreador;
    }

    public void setUidCreador(String uidCreador) {
        this.uidCreador = uidCreador;
    }
}
