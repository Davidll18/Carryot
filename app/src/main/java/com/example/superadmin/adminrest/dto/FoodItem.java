package com.example.superadmin.adminrest.dto;

public class FoodItem {
    private String name;    // Nombre del plato
    private String price;   // Precio del plato
    private String stock;   // Cantidad disponible

    // Constructor
    public FoodItem(String name, String price, String stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Métodos getter
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }
}

