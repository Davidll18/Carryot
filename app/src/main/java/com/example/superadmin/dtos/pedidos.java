package com.example.superadmin.dtos;

import com.google.type.DateTime;

import java.lang.reflect.Array;

public class pedidos {

    private String NumeroPedido ;
    private int cantidad1;
    private int cantidad2;
    private int cantidad3;

    private String direccion;

    private Array estado;

    private DateTime hora;

    private String uidRepartidor;
    private String uidUsuario;

    private String uidplato1;
    private String uidplato2;
    private String uidplato3;

    private int costototal;

    public pedidos(int costototal, String uidplato3, String uidplato2, String uidplato1, String uidUsuario, String uidRepartidor, DateTime hora, Array estado, String direccion, int cantidad3, int cantidad2, int cantidad1, String numeroPedido) {
        this.costototal = costototal;
        this.uidplato3 = uidplato3;
        this.uidplato2 = uidplato2;
        this.uidplato1 = uidplato1;
        this.uidUsuario = uidUsuario;
        this.uidRepartidor = uidRepartidor;
        this.hora = hora;
        this.estado = estado;
        this.direccion = direccion;
        this.cantidad3 = cantidad3;
        this.cantidad2 = cantidad2;
        this.cantidad1 = cantidad1;
        NumeroPedido = numeroPedido;
    }

    public String getNumeroPedido() {
        return NumeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        NumeroPedido = numeroPedido;
    }

    public int getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(int cantidad1) {
        this.cantidad1 = cantidad1;
    }

    public int getCantidad2() {
        return cantidad2;
    }

    public void setCantidad2(int cantidad2) {
        this.cantidad2 = cantidad2;
    }

    public int getCantidad3() {
        return cantidad3;
    }

    public void setCantidad3(int cantidad3) {
        this.cantidad3 = cantidad3;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Array getEstado() {
        return estado;
    }

    public void setEstado(Array estado) {
        this.estado = estado;
    }

    public DateTime getHora() {
        return hora;
    }

    public void setHora(DateTime hora) {
        this.hora = hora;
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

    public String getUidplato1() {
        return uidplato1;
    }

    public void setUidplato1(String uidplato1) {
        this.uidplato1 = uidplato1;
    }

    public String getUidplato2() {
        return uidplato2;
    }

    public void setUidplato2(String uidplato2) {
        this.uidplato2 = uidplato2;
    }

    public String getUidplato3() {
        return uidplato3;
    }

    public void setUidplato3(String uidplato3) {
        this.uidplato3 = uidplato3;
    }

    public int getCostototal() {
        return costototal;
    }

    public void setCostototal(int costototal) {
        this.costototal = costototal;
    }
}
