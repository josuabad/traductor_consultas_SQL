package code;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandUser {
    public static void main(String[] args) throws Exception {
        
        String ruta1 = "src/Code/ConfigFlex.flex";
        String ruta2 = "src/Code/ConfigCup.flex";
        String[] rutaS = {"-parser", "Sintax", "src/Code/Syntax.cup"};
        generar(ruta1, ruta2, rutaS);
    }


    public static void generar(String ruta1, String ruta2, String[] rutaS) throws IOException, Exception{
    
        String[] os = verificarOS();
    	System.out.println(prepararFlex(os[0], os[1], ruta1));
        System.out.println(prepararFlex(os[0], os[1], ruta2));
        java_cup.Main.main(rutaS);
        
        Path rutaSym = Paths.get("src/Code/sym.java");
        if (Files.exists(rutaSym)) {
            Files.delete(rutaSym);
            }
        Files.move(
                Paths.get("sym.java"), 
                Paths.get("src/Code/sym.java")
            );
        Path rutaSin = Paths.get("src/Code/Sintax.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
            }
        Files.move(
                Paths.get("Sintax.java"), 
                Paths.get("src/Code/Sintax.java")
            );
    }


    public static String[] verificarOS() {

    	String[] params = new String[2];
        String sistemaOperativo = System.getProperty("os.name");
        if (sistemaOperativo.toLowerCase().contains("windows")) {
        	params[0] = "cmd";
        	params[1] = "/c";
        } else {
        	params[0] = "bash";
        	params[1] = "-c";
        }
        
        return params;
    }


    public static String access(String terminal, String command, String execute) throws IOException {
	    	
    	ProcessBuilder processBuilder = new ProcessBuilder(terminal, command, execute);
        processBuilder.redirectErrorStream(true);

        Process proceso = processBuilder.start(); // Declaration try-catch
        InputStream inputStream = proceso.getInputStream();
        BufferedReader lector = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder resultado = new StringBuilder();
        String linea;
        while ((linea = lector.readLine()) != null) { // Declaration try-catch
            resultado.append(linea).append("\n");
        }

        try {
            int exitCode = proceso.waitFor();
            if (exitCode != 0) {
                System.err.println("Error: El comando terminó con un código de salida no exitoso: " + exitCode);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return resultado.toString();
    }


    public static String prepararFlex(String terminal, String command, String file) throws IOException {
    	
    	String execute = "java -jar lib/jflex-full-1.9.1.jar " + file;
    	return access(terminal, command, execute);
    }
}
