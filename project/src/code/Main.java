package code;

import javax.swing.*;
import com.mysql.cj.jdbc.result.ResultSetMetaData;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main extends JFrame {
    private JTextField cuadroInput;
    private JButton botonRun;
    private JTextArea areaOutput;

    // Dejamos las variables privadas para protejer la integridad de nuestros datos
    private static final String URL = "jdbc:mysql://localhost:3306/traductor_consultas_sql";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "josu-mySQL";

    public Main() {
        // Configurar la ventana
        setTitle("Interfaz Gráfica");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar en la pantalla

        // Inicializar componentes
        cuadroInput = new JTextField(10);
        cuadroInput.setFont(new Font("Arial", Font.PLAIN, 18)); // Ajustar la fuente y el tamaño aquí
        botonRun = new JButton("Run");
        areaOutput = new JTextArea(15, 20);
        areaOutput.setFont(new Font("Arial", Font.PLAIN, 16)); // Ajustar la fuente y el tamaño aquí

        // Configurar el área de texto de salida como no editable
        areaOutput.setEditable(false);

        // Configurar el diseño del contenedor principal (BoxLayout)
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Crear paneles para organizar la interfaz
        JPanel panelInput = new JPanel();
        JPanel panelBoton = new JPanel();
        JPanel panelOutput = new JPanel();

        // Configurar el cuadro de entrada
        panelInput.setLayout(new BoxLayout(panelInput, BoxLayout.Y_AXIS)); // Usar BoxLayout para organizar verticalmente

        // Agregar espacio en la parte superior
        panelInput.add(Box.createVerticalGlue());

        // Agregar la etiqueta "Input:"
        JLabel etiquetaInput = new JLabel("Input");
        etiquetaInput.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInput.add(etiquetaInput);

        // Agregar el cuadro de texto de entrada
        panelInput.add(cuadroInput);

        // Agregar espacio en la parte inferior
        panelInput.add(Box.createVerticalGlue());

        // Configurar el botón "Run"
        panelBoton.add(botonRun);

        // Configurar el área de texto de salida
        panelOutput.setLayout(new BoxLayout(panelOutput, BoxLayout.Y_AXIS));

        // Agregar espacio en la parte superior
        panelOutput.add(Box.createVerticalGlue());

        // Agregar la etiqueta "Output:"
        JLabel etiquetaOutput = new JLabel("Output");
        etiquetaOutput.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelOutput.add(etiquetaOutput);

        // Agregar el área de texto de salida
        panelOutput.add(new JScrollPane(areaOutput));

        // Agregar espacio en la parte inferior
        panelOutput.add(Box.createVerticalGlue());

        // Agregar paneles a la ventana principal
        add(panelInput);
        add(panelBoton);
        add(panelOutput);

        // Agregar un ActionListener al botón
        botonRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarPrograma();
            }
        });

        // Agregar un KeyListener al cuadro de texto de entrada para detectar la tecla "ENTER"
        cuadroInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // No se necesita implementar keyTyped para este caso
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ejecutarPrograma();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // No se necesita implementar keyReleased para este caso
            }
        });
    }

    private void ejecutarPrograma() {
        // Lógica para ejecutar el programa y actualizar el área de texto de salida
        String input = cuadroInput.getText();
        // Debemos guardar el input en un archivo para que el flex no de error
        convertidorTXT(input);
        try {
            // Crear el analizador léxico
            Flex flex = new Flex(new FileReader("src/code/Input.txt"));

            // Crear el analizador sintáctico
            Parser parser = new Parser(flex);

            parser.parse();

            String query = Flex.texto.substring(4, Flex.texto.length());
            query = query.trim();
            connectSQL(query);
        } catch (Exception e) {
            areaOutput.setText("Error sintáctico: \n" + e.getMessage() + "\nPruebe con: \"Elegir * de libros;\"");
        }        
    }

    public void convertidorTXT(String texto) {
        String path = "src/code/Input.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(texto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectSQL(String query) {
        // Ejecutar consulta SQL con el input proporcionado
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
             PreparedStatement consulta = conexion.prepareStatement(query)) {

            try (ResultSet resultado = consulta.executeQuery()) {
                // Procesar y mostrar resultados en el área de texto de salida
                mostrarResultados(query, resultado);
            }

        } catch (SQLException e) {
            mostrarResultados(e);
        }
    }

    private void mostrarResultados(String query, ResultSet resultado) throws SQLException {
        StringBuilder outputText = new StringBuilder();

        ResultSetMetaData metaData = (ResultSetMetaData) resultado.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultado.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String nombreColumna = metaData.getColumnName(i);
                Object valorColumna = resultado.getObject(i);

                // Agregar los valores al texto de salida
                outputText.append(nombreColumna).append(": ").append(valorColumna).append("\n");
            }
            outputText.append("-----------------\n");
        }

        // Actualizar el área de texto de salida
        areaOutput.setText(query + "\n\n" + outputText.toString());
    }

    private void mostrarResultados(SQLException error) {
        areaOutput.setText("Error en acceso a base de datos SQL.\n" + error.getMessage() + "\n\n--> Por favor, revise los datos de su consulta\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
