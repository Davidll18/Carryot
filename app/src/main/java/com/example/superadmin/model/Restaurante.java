package com.example.superadmin.model;

public class Restaurante {
    private String name;
    private int imageResource;

    public Restaurante(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}
