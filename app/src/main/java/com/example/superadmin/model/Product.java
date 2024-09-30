package com.example.superadmin.model;

public class Product {
    private String name;
    private String model;
    private String price;
    private int imageResourceId;

    public Product(String name, String model, String price, int imageResourceId) {
        this.name = name;
        this.model = model;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
