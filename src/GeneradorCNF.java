
package com.mycompany.tsp.sat;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntPredicate;

public class GeneradorCNF {
    private int variables;
    private final List<int[]> clausulas = new ArrayList<>();
    private int falso;

    private int nuevaVariable() {
        return ++variables;
    }

    private void agregar(int... literales) {
        clausulas.add(literales);
    }

    private int puerta(int[] entradas, IntPredicate funcion) {
        int salida = nuevaVariable();
        int combinaciones = 1 << entradas.length;

        for (int mascara = 0; mascara < combinaciones; mascara++) {
            int[] clausula = new int[entradas.length + 1];

            for (int i = 0; i < entradas.length; i++) {
                boolean valor = (mascara & (1 << i)) != 0;
                clausula[i] = valor ? -entradas[i] : entradas[i];
            }

            clausula[entradas.length] =
                    funcion.test(mascara) ? salida : -salida;

            agregar(clausula);
        }

        return salida;
    }

    private int and(int a, int b) {
        return puerta(new int[]{a, b}, m ->
                (m & 1) != 0 && (m & 2) != 0);
    }

    private int and(int a, int b, int c) {
        return puerta(new int[]{a, b, c}, m ->
                (m & 1) != 0 && (m & 2) != 0 && (m & 4) != 0);
    }

    private int or(int a, int b) {
        return puerta(new int[]{a, b}, m ->
                (m & 1) != 0 || (m & 2) != 0);
    }

    private int xor(int a, int b) {
        return puerta(new int[]{a, b}, m ->
                ((m & 1) != 0) != ((m & 2) != 0));
    }

    private int igual(int a, int b) {
        return puerta(new int[]{a, b}, m ->
                ((m & 1) != 0) == ((m & 2) != 0));
    }

    private int[][] sumar(int[][] acumulado, int transicion, int costo) {
        int bits = acumulado.length;
        int[] resultado = new int[bits];
        int acarreo = falso;

        for (int bit = 0; bit < bits; bit++) {
            int sumando = ((costo >>> bit) & 1) != 0
                    ? transicion : falso;

            int parcial = xor(acumulado[bit][0], sumando);
            resultado[bit] = xor(parcial, acarreo);

            int c1 = and(acumulado[bit][0], sumando);
            int c2 = and(parcial, acarreo);
            acarreo = or(c1, c2);
        }

        int[][] nuevo = new int[bits][1];

        for (int bit = 0; bit < bits; bit++) {
            nuevo[bit][0] = resultado[bit];
        }

        return nuevo;
    }

    public void generar(Grafo grafo, String archivo) throws IOException {
        variables = 0;
        clausulas.clear();

        falso = nuevaVariable();
        agregar(-falso);

        int n = grafo.getNumeroCiudades();
        int[][] distancias = grafo.getDistancias();
        int k = grafo.getCostoMaximo();

        int[][] x = new int[n][n];

        for (int ciudad = 0; ciudad < n; ciudad++) {
            for (int paso = 0; paso < n; paso++) {
                x[ciudad][paso] = nuevaVariable();
            }
        }

        for (int ciudad = 0; ciudad < n; ciudad++) {
            int[] alMenosUna = new int[n];

            for (int paso = 0; paso < n; paso++) {
                alMenosUna[paso] = x[ciudad][paso];
            }

            agregar(alMenosUna);

            for (int a = 0; a < n; a++) {
                for (int b = a + 1; b < n; b++) {
                    agregar(-x[ciudad][a], -x[ciudad][b]);
                }
            }
        }

        for (int paso = 0; paso < n; paso++) {
            int[] alMenosUna = new int[n];

            for (int ciudad = 0; ciudad < n; ciudad++) {
                alMenosUna[ciudad] = x[ciudad][paso];
            }

            agregar(alMenosUna);

            for (int a = 0; a < n; a++) {
                for (int b = a + 1; b < n; b++) {
                    agregar(-x[a][paso], -x[b][paso]);
                }
            }
        }

        int bits = 64;
        int[][] total = new int[bits][1];

        for (int bit = 0; bit < bits; bit++) {
            total[bit][0] = falso;
        }

        for (int paso = 0; paso < n; paso++) {
            int siguiente = (paso + 1) % n;

            for (int ciudadA = 0; ciudadA < n; ciudadA++) {
                for (int ciudadB = 0; ciudadB < n; ciudadB++) {
                    int viaje = and(
                            x[ciudadA][paso],
                            x[ciudadB][siguiente]
                    );

                    total = sumar(total, viaje,
                            distancias[ciudadA][ciudadB]);
                }
            }
        }

        int menor = falso;
        int igual = -falso;

        for (int bit = bits - 1; bit >= 0; bit--) {
            int bitK = ((k >>> bit) & 1) != 0 ? -falso : falso;
            int bitTotal = total[bit][0];

            int noTotal = -bitTotal;
            int candidato = and(igual, noTotal, bitK);
            menor = or(menor, candidato);
            igual = and(igual, igual(bitTotal, bitK));
        }

        agregar(menor, igual);

        try (BufferedWriter escritor =
                new BufferedWriter(new FileWriter(archivo))) {
            escritor.write("p cnf " + variables + " "
                    + clausulas.size());
            escritor.newLine();

            for (int[] clausula : clausulas) {
                for (int literal : clausula) {
                    escritor.write(literal + " ");
                }

                escritor.write("0");
                escritor.newLine();
            }
        }
    }
}
