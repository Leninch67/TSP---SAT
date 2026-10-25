
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorCNF {
    private int numeroVariables;
    private int numeroClausulas;
    private StringBuilder clausulas;

    public GeneradorCNF() {
        numeroVariables = 0;
        numeroClausulas = 0;
        clausulas = new StringBuilder();
    }

    private int nuevaVariable() {
        numeroVariables++;
        return numeroVariables;
    }

    private void agregarClausula(int... literales) {
        for (int literal : literales) {
            clausulas.append(literal).append(" ");
        }
        clausulas.append("0\n");
        numeroClausulas++;
    }

    public void generar(Grafo grafo, String archivo) throws IOException {
        int n = grafo.getNumeroCiudades();
        int[][] distancias = grafo.getDistancias();
        int costoMaximo = grafo.getCostoMaximo();

        int[][] x = new int[n][n];

        for (int ciudad = 0; ciudad < n; ciudad++) {
            for (int paso = 0; paso < n; paso++) {
                x[ciudad][paso] = nuevaVariable();
            }
        }

        for (int ciudad = 0; ciudad < n; ciudad++) {
            agregarClausula(x[ciudad]);
            for (int paso1 = 0; paso1 < n; paso1++) {
                for (int paso2 = paso1 + 1; paso2 < n; paso2++) {
                    agregarClausula(
                        -x[ciudad][paso1],
                        -x[ciudad][paso2]
                    );
                }
            }
        }

        for (int paso = 0; paso < n; paso++) {
            int[] clausula = new int[n];
            for (int ciudad = 0; ciudad < n; ciudad++) {
                clausula[ciudad] = x[ciudad][paso];
            }
            agregarClausula(clausula);

            for (int ciudad1 = 0; ciudad1 < n; ciudad1++) {
                for (int ciudad2 = ciudad1 + 1; ciudad2 < n; ciudad2++) {
                    agregarClausula(
                        -x[ciudad1][paso],
                        -x[ciudad2][paso]
                    );
                }
            }
        }

        for (int paso = 0; paso < n; paso++) {
            int siguientePaso = (paso + 1) % n;

            for (int ciudad1 = 0; ciudad1 < n; ciudad1++) {
                for (int ciudad2 = 0; ciudad2 < n; ciudad2++) {
                    if (distancias[ciudad1][ciudad2] > costoMaximo) {
                        agregarClausula(
                            -x[ciudad1][paso],
                            -x[ciudad2][siguientePaso]
                        );
                    }
                }
            }
        }

        try (BufferedWriter escritor = new BufferedWriter(
                new FileWriter(archivo))) {
            escritor.write("p cnf " + numeroVariables + " "
                    + numeroClausulas + "\n");
            escritor.write(clausulas.toString());
        }
    }
}

