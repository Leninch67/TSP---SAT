package com.mycompany.tsp.sat;

public class Grafo {
    private final int numeroCiudades;
    private final int[][] distancias;
    private final int costoMaximo;

    public Grafo(int numeroCiudades, int[][] distancias, int costoMaximo) {
        this.numeroCiudades = numeroCiudades;
        this.distancias = distancias;
        this.costoMaximo = costoMaximo;
    }

    public int getNumeroCiudades() {
        return numeroCiudades;
    }

    public int[][] getDistancias() {
        return distancias;
    }

    public int getCostoMaximo() {
        return costoMaximo;
    }
}
