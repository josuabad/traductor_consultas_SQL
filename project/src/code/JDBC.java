package code;

import java.sql.*;

public class JDBC {

    // Dejamos las variables privadas para protejer la integridad de nuestros datos
    private static final String URL = "";
    private static final String USUARIO = "";
    private static final String CONTRASENA = "";

    public static void main(String[] args) {
        Connection conexion = null;
        PreparedStatement consulta = null;
        ResultSet resultado = null;

        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

            // Tu consulta SQL con un marcador de posición para la condición
            String sql = args[0];
            consulta = conexion.prepareStatement(sql);

            resultado = consulta.executeQuery();

            // Obtener metadatos del ResultSet para obtener nombres de columnas
            ResultSetMetaData metaData = resultado.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultado.next()) {
                // Iterar sobre las columnas y obtener sus valores
                for (int i = 1; i <= columnCount; i++) {
                    String nombreColumna = metaData.getColumnName(i);
                    Object valorColumna = resultado.getObject(i);

                    // Hacer algo con los valores obtenidos
                    System.out.println(nombreColumna + ": " + valorColumna);
                }
                System.out.println("-----------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) resultado.close();
                if (consulta != null) consulta.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}