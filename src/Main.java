
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        System.out.print("Ingrese el numero de ciudades: ");
        int n = entrada.nextInt();

        int[][] distancias = new int[n][n];

        System.out.println("Ingrese la matriz de distancias:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = entrada.nextInt();
            }
        }

        System.out.print("Ingrese el costo maximo K: ");
        int k = entrada.nextInt();

        Grafo grafo = new Grafo(n, distancias, k);

        try {
            GeneradorCNF generador = new GeneradorCNF();
            generador.generar(grafo, "tsp.cnf");

            SolucionadorSAT solucionador = new SolucionadorSAT();
            solucionador.resolver("tsp.cnf", grafo);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        entrada.close();
    }
}

