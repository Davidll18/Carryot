package com.example.superadmin.adminrest.dto;

public class PedidosItem {

    private String numeroOrden;
    private String status;
    private int backgroungStatus;
    private String descripcion;
    private int platoImagen;
    private int iconoVista;
    private String precio;

    public PedidosItem(String numeroOrden, String status, int backgroungStatus, String descripcion, int platoImagen, int iconoVista, String precio) {
        this.numeroOrden = numeroOrden;
        this.status = status;
        this.backgroungStatus = backgroungStatus;
        this.descripcion = descripcion;
        this.platoImagen = platoImagen;
        this.iconoVista = iconoVista;
        this.precio = precio;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public String getStatus() {
        return status;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getBackgroungStatus() {
        return backgroungStatus;
    }

    public int getIconoVista() {
        return iconoVista;
    }

    public int getPlatoImagen() {
        return platoImagen;
    }

    public String getPrecio() {
        return precio;
    }
}
