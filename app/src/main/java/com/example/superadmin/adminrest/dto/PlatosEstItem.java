package com.example.superadmin.adminrest.dto;

public class PlatosEstItem {
    private String title;
    private int platoImage1;
    private String tvPlato1;

    private int platoImage2;
    private String tvPlato2;

    private int platoImage3;
    private String tvPlato3;

    public PlatosEstItem(String title, int platoImage1, String tvPlato1, int platoImage2, String tvPlato2, int platoImage3, String tvPlato3) {
        this.title = title;
        this.platoImage1 = platoImage1;
        this.tvPlato1 = tvPlato1;
        this.platoImage2 = platoImage2;
        this.tvPlato2 = tvPlato2;
        this.platoImage3 = platoImage3;
        this.tvPlato3 = tvPlato3;
    }

    public String getTitle() {
        return title;
    }

    public int getPlatoImage1() {
        return platoImage1;
    }

    public String getTvPlato1() {
        return tvPlato1;
    }

    public int getPlatoImage2() {
        return platoImage2;
    }

    public String getTvPlato2() {
        return tvPlato2;
    }

    public String getTvPlato3() {
        return tvPlato3;
    }

    public int getPlatoImage3() {
        return platoImage3;
    }
}
