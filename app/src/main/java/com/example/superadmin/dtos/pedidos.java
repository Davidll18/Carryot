package com.example.superadmin.dtos;

import java.util.HashMap;
import java.util.Map;

public class pedidos {

    private String numeroPedido;
    private String direccion;
    private String uidRepartidor;
    private String uidUsuario;
    private String uidRestaurante;
    private int costoTotal;

    // Mapa para productos (UID del plato -> cantidad)
    private Map<String, Integer> productos;

    public pedidos() {
        this.productos = new HashMap<>();
    }

    public pedidos(String numeroPedido, String direccion, String uidRepartidor, String uidUsuario, String uidRestaurante, int costoTotal) {
        this.numeroPedido = numeroPedido;
        this.direccion = direccion;
        this.uidRepartidor = uidRepartidor;
        this.uidUsuario = uidUsuario;
        this.uidRestaurante = uidRestaurante;
        this.costoTotal = costoTotal;
        this.productos = new HashMap<>();
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUidRepartidor() {
        return uidRepartidor;
    }

    public void setUidRepartidor(String uidRepartidor) {
        this.uidRepartidor = uidRepartidor;
    }

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public String getUidRestaurante() {
        return uidRestaurante;
    }

    public void setUidRestaurante(String uidRestaurante) {
        this.uidRestaurante = uidRestaurante;
    }

    public int getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(int costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Map<String, Integer> getProductos() {
        return productos;
    }

    public void setProductos(Map<String, Integer> productos) {
        this.productos = productos;
    }

    public void agregarProducto(String uidPlato, int cantidad) {
        // Actualizar cantidad si ya existe
        if (productos.containsKey(uidPlato)) {
            productos.put(uidPlato, productos.get(uidPlato) + cantidad);
        } else {
            productos.put(uidPlato, cantidad);
        }
    }
}
