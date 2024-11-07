package com.example.superadmin.adminrest.dto;

public class PedidosStatusItem {
    private String titlePP;
    private String cantidadPP;
    private String titlePA;
    private String cantidadPA;

    public PedidosStatusItem(String titlePP, String cantidadPP, String titlePA, String cantidadPA) {
        this.titlePP = titlePP;
        this.cantidadPP = cantidadPP;
        this.titlePA = titlePA;
        this.cantidadPA = cantidadPA;
    }

    public String getTitlePP() {
        return titlePP;
    }

    public String getCantidadPP() {
        return cantidadPP;
    }

    public String getTitlePA() {
        return titlePA;
    }

    public String getCantidadPA() {
        return cantidadPA;
    }
}
