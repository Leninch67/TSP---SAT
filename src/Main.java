
package com.mycompany.tsp.sat;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        try {
            System.out.print("Ingrese el numero de ciudades: ");
            int n = entrada.nextInt();

            if (n < 2) {
                System.out.println("Debe ingresar al menos 2 ciudades.");
                return;
            }

            int[][] distancias = new int[n][n];

            System.out.println("Ingrese la matriz de distancias:");

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    distancias[i][j] = entrada.nextInt();

                    if (distancias[i][j] < 0) {
                        System.out.println(
                                "Las distancias no pueden ser negativas.");
                        return;
                    }
                }
            }

            System.out.print("Ingrese el costo maximo K: ");
            int k = entrada.nextInt();

            if (k < 0) {
                System.out.println("K no puede ser negativo.");
                return;
            }

            Grafo grafo = new Grafo(n, distancias, k);

            GeneradorCNF generador = new GeneradorCNF();
            generador.generar(grafo, "tsp.cnf");

            System.out.println("Archivo CNF generado: tsp.cnf");

            SolucionadorSAT solucionador = new SolucionadorSAT();
            solucionador.resolver("tsp.cnf", grafo);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            entrada.close();
        }
    }
}

