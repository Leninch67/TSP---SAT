
public class Grafo {
    private int numeroCiudades;
    private int[][] distancias;
    private int costoMaximo;

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

