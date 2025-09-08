import utils.SchemaGenerator.SchemaGenerator;
import utils.CSVImporter.CSVImporter;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando aplicación...");

        String url = "jdbc:mysql://localhost:3306/practico";
        String user = "lautaro";
        String pass = "1234";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // Crear esquema
            SchemaGenerator.createSchema(conn);

            // Importar CSV
            CSVImporter.importarCSV(conn);
        } catch (Exception e) {
            System.err.println("❌ Error de conexión:");
            e.printStackTrace();
        }

        System.out.println("Proceso finalizado.");
    }
}

