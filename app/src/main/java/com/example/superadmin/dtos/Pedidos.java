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
    private String plato1;
    private String cantidad1;
    private String plato2;
    private String cantidad2;
    private String plato3;
    private String cantidad3;


    public Pedidos(String numeroPedido, String direccion, String uidRepartidor,
                   String uidUsuario, String uidRestaurante, String uidCreacion,
                   String costoTotal, String estado, String plato1,
                   String cantidad1, String plato2, String cantidad2,
                   String plato3, String cantidad3) {
        this.numeroPedido = numeroPedido;
        this.direccion = direccion;
        this.uidRepartidor = uidRepartidor;
        this.uidUsuario = uidUsuario;
        this.uidRestaurante = uidRestaurante;
        this.uidCreacion = uidCreacion;
        this.costoTotal = costoTotal;
        this.estado = estado;
        this.plato1 = plato1;
        this.cantidad1 = cantidad1;
        this.plato2 = plato2;
        this.cantidad2 = cantidad2;
        this.plato3 = plato3;
        this.cantidad3 = cantidad3;
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

    public String getPlato1() {
        return plato1;
    }

    public void setPlato1(String plato1) {
        this.plato1 = plato1;
    }

    public String getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(String cantidad1) {
        this.cantidad1 = cantidad1;
    }

    public String getPlato2() {
        return plato2;
    }

    public void setPlato2(String plato2) {
        this.plato2 = plato2;
    }

    public String getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(String cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public String getPlato3() {
        return plato3;
    }

    public void setPlato3(String plato3) {
        this.plato3 = plato3;
    }

    public String getCantidad3() {
        return cantidad3;
    }

    public void setCantidad3(String cantidad3) {
        this.cantidad3 = cantidad3;
    }
}
