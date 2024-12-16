package com.example.superadmin.dtos;

public class UserProductInCar {
    private String productUID;  // UID del producto en Firebase
    private String name;        // Nombre del producto
    private int quantity;       // Cantidad del producto
    private double priceTotal;  // Precio total del producto
    private int imageResId;     // ID de la imagen

    // Constructor
    public UserProductInCar(String productUID, String name, int quantity, double priceTotal, int imageResId) {
        this.productUID = productUID;
        this.name = name;
        this.quantity = quantity;
        this.priceTotal = priceTotal;
        this.imageResId = imageResId;
    }

    // Getters y Setters
    public String getProductUID() {
        return productUID;
    }

    public void setProductUID(String productUID) {
        this.productUID = productUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
