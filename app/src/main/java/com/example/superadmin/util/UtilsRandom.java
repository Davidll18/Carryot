package com.example.superadmin.util;

import java.text.DecimalFormat;
import java.util.Random;

public class UtilsRandom {

    // Función genérica para generar un número aleatorio entre un rango (min y max)
    public static String generateRandomCost(int min, int max) {
        Random random = new Random();
        // Generar un número aleatorio entre min y max (incluidos)
        int randomValue = random.nextInt(max - min + 1) + min;
        // Convertir a formato decimal
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(randomValue);
    }

    public static int generateRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Fórmula para limitar el rango
    }
}
