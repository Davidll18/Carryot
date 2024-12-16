package com.example.superadmin.adminrest.dto;

public class Plato {
    private String nombrePlato;
    private String cantidad;

    public Plato(String nombrePlato, String cantidad) {
        this.nombrePlato = nombrePlato;
        this.cantidad = cantidad;
    }

    public String getNombrePlato() {
        return nombrePlato;
    }

    public void setNombrePlato(String nombrePlato) {
        this.nombrePlato = nombrePlato;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
    public int getCantidadAsInt() {
        try {
            return Integer.parseInt(cantidad); // Convierte cantidad de String a int
        } catch (NumberFormatException e) {
            return 0; // Si la cantidad no es un número válido, devuelve 0
        }
    }
}
