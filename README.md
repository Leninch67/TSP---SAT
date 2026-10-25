# TSP---SAT
# Proyecto TSP-SAT

## 1. Descripción
Este proyecto busca determinar si existe una ruta para visitar todas las ciudades una sola vez, regresar a la ciudad inicial y no superar un costo máximo K. Para resolverlo, se utiliza Java y el solucionador SAT4J.

## 2. Requisitos
- Java JDK 11 o superior.
- Apache NetBeans.
- Maven para administrar las dependencias.

## 3. Dependencias
El proyecto utiliza la biblioteca SAT4J, que permite resolver las restricciones lógicas generadas por el programa.
La dependencia se configura en el archivo pom.xml:

xml
<dependency>
    <groupId>org.ow2.sat4j</groupId>
    <artifactId>org.ow2.sat4j.core</artifactId>
    <version>2.3.6</version>
</dependency>

Maven descarga automáticamente esta biblioteca al compilar el proyecto.

## 4. Compilar el proyecto
1. Abrir el proyecto en Apache NetBeans.
2. Hacer clic derecho sobre el proyecto.
3. Seleccionar Clean and Build luego Run para comprobar su correcto funcionamiento.
4. Comprobar que aparezca el mensaje BUILD SUCCESS y se ejecute correctamente.

## 5. Ejecutar el programa
1. Ejecutar la clase Main.java.
2. Ingresar el número de ciudades.
3. Introducir la matriz de distancias.
4. Ingresar el costo máximo K.
5. El programa genera automáticamente el archivo (tsp.cnf) y utiliza SAT4J para buscar una solución.

## 6. Ejemplo de prueba
Número de ciudades:
3
Matriz de distancias:
0 2 9
2 0 3
9 3 0
Costo máximo K:
14
Resultado esperado:
Archivo CNF generado: tsp.cnf
Ruta encontrada: 2 -> 3 -> 1 -> 2
Ruta encontrada2: 1 -> 2 -> 3 -> 1
Costo total: 14
La ruta puede variar, pero su costo debe ser menor o igual a K.


