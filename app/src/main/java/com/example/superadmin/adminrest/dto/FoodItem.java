package com.example.superadmin.adminrest.dto;

public class FoodItem {
    private String name;    // Nombre del plato
    private String price;   // Precio del plato
    private String stock;   // Cantidad disponible
    private String desc;
    private Boolean available;
    private String imageUrl;

    // Constructor


    public FoodItem(String name, String desc, Boolean available,
                    String price, String stock, String imageUrl) {
        this.name = name;
        this.desc = desc;
        this.available = available;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}

