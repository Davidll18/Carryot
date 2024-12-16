package com.example.superadmin.dtos;

import java.util.HashMap;
import java.util.Map;

public class Pedidos {

    private String numeroPedido;
    private String direccion;
    private String uidRepartidor;
    private String uidUsuario;
    private String uidRestaurante;
    private String uidCreacion;
    private String costoTotal;
    private String estado; //Estados:
    private String uidplato1;
    private String plato1;
    private String cantidad1;
    private String uidplato2;
    private String plato2;
    private String cantidad2;
    private String uidplato3;
    private String plato3;
    private String cantidad3;
    private String imageUrl;


    public Pedidos(String numeroPedido, String direccion,
                   String uidRepartidor, String uidUsuario,
                   String uidRestaurante, String uidCreacion,
                   String costoTotal, String estado, String uidplato1,
                   String plato1, String cantidad1, String plato2,
                   String uidplato2, String cantidad2, String uidplato3,
                   String plato3, String cantidad3, String imageUrl) {
        this.numeroPedido = numeroPedido;
        this.direccion = direccion;
        this.uidRepartidor = uidRepartidor;
        this.uidUsuario = uidUsuario;
        this.uidRestaurante = uidRestaurante;
        this.uidCreacion = uidCreacion;
        this.costoTotal = costoTotal;
        this.estado = estado;
        this.uidplato1 = uidplato1;
        this.plato1 = plato1;
        this.cantidad1 = cantidad1;
        this.plato2 = plato2;
        this.uidplato2 = uidplato2;
        this.cantidad2 = cantidad2;
        this.uidplato3 = uidplato3;
        this.plato3 = plato3;
        this.cantidad3 = cantidad3;
        this.imageUrl = imageUrl;
    }

    public Pedidos() {
    }



    public String getPlato1() {
        return plato1;
    }

    public void setPlato1(String plato1) {
        this.plato1 = plato1;
    }

    public String getPlato2() {
        return plato2;
    }

    public void setPlato2(String plato2) {
        this.plato2 = plato2;
    }

    public String getPlato3() {
        return plato3;
    }

    public void setPlato3(String plato3) {
        this.plato3 = plato3;
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

    public String getUidCreacion() {
        return uidCreacion;
    }

    public void setUidCreacion(String uidCreacion) {
        this.uidCreacion = uidCreacion;
    }

    public String getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(String costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUidplato1() {
        return uidplato1;
    }

    public void setUidplato1(String uidplato1) {
        this.uidplato1 = uidplato1;
    }

    public String getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(Object cantidad1) {

        if (cantidad1 instanceof Long) {
            this.cantidad1 = String.valueOf(cantidad1);
        } else {
            this.cantidad1 = (String) cantidad1;  // Si ya es un String, asigna directamente
        }
    }

    public String getUidplato2() {
        return uidplato2;
    }

    public void setUidplato2(String uidplato2) {
        this.uidplato2 = uidplato2;
    }

    public String getCantidad2() {

        return cantidad2;
    }

    public void setCantidad2(Object cantidad2) {

        if (cantidad2 instanceof Long) {
            this.cantidad2 = String.valueOf(cantidad2);
        } else {
            this.cantidad2 = (String) cantidad1;  // Si ya es un String, asigna directamente
        }
    }

    public String getUidplato3() {
        return uidplato3;
    }

    public void setUidplato3(String uidplato3) {
        this.uidplato3 = uidplato3;
    }

    public String getCantidad3() {
        return cantidad3;
    }

    public void setCantidad3(Object cantidad3) {
        if (cantidad3 instanceof Long) {
            this.cantidad3 = String.valueOf(cantidad3);
        } else {
            this.cantidad3 = (String) cantidad3;  // Si ya es un String, asigna directamente
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
