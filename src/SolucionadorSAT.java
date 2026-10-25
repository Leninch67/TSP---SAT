package com.mycompany.tsp.sat;

import java.util.HashSet;
import java.util.Set;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;

import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

public class SolucionadorSAT {

    public void resolver(String archivoCNF, Grafo grafo) throws Exception {
        ISolver solver = SolverFactory.newDefault();
        DimacsReader lector = new DimacsReader(solver);
        //IProblem problema = lector.parseInstance(new File(archivoCNF));
        IProblem problema = lector.parseInstance(archivoCNF);

        if (!problema.isSatisfiable()) {
            System.out.println(
                    "NO existe ninguna ruta con costo menor o igual a K");
            return;
        }

        Set<Integer> verdaderas = new HashSet<>();

        for (int literal : problema.model()) {
            if (literal > 0) {
                verdaderas.add(literal);
            }
        }

        int n = grafo.getNumeroCiudades();
        int[][] distancias = grafo.getDistancias();
        int[] ruta = new int[n];

        for (int paso = 0; paso < n; paso++) {
            ruta[paso] = -1;

            for (int ciudad = 0; ciudad < n; ciudad++) {
                int variable = 2 + ciudad * n + paso;

                if (verdaderas.contains(variable)) {
                    ruta[paso] = ciudad;
                    break;
                }
            }

            if (ruta[paso] == -1) {
                throw new IllegalStateException(
                        "No se pudo reconstruir la ruta");
            }
        }

        long costo = 0;

        System.out.print("Ruta encontrada: ");

        for (int paso = 0; paso < n; paso++) {
            System.out.print(ruta[paso] + 1);

            if (paso < n - 1) {
                System.out.print(" -> ");
            }

            costo += distancias[ruta[paso]][ruta[(paso + 1) % n]];
        }

        System.out.println(" -> " + (ruta[0] + 1));
        System.out.println("Costo total: " + costo);
    }
}

