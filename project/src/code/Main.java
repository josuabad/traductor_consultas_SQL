package code;

import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        try { // En caso de no encontrar el archivo buscado -> try-catch exception
            // Crear el analizador léxico
            Flex flex = new Flex(new FileReader("src/code/Input.txt"));

            // Crear el analizador sintáctico
            Parser parser = new Parser(flex);

            // Incluir la opción de que el parser de error, para que no se ejecute el query
            parser.parse();
            System.out.println("\n");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error durante el análisis: " + e.getMessage());
        }

        String[] query = {Flex.texto.substring(4, Flex.texto.length())};
        JDBC.main(query);

    }
}
