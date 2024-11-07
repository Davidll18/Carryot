package com.example.superadmin.adminrest.dto;

public class GananciaItem {
    private String title;
    private int nameImage;

    public GananciaItem(String title, int nameImage) {
        this.title = title;
        this.nameImage = nameImage;
    }

    public String getTitle() {
        return title;
    }

    public int getNameImage() {
        return nameImage;
    }
}
