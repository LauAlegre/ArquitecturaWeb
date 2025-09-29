package utils.CSVImporter;

import dao.*;
import factory.*;
import repository.mysql.CsvLoader;

import java.sql.Connection;

public class CSVImporter {

    private CSVImporter() {
        // 🔒 Constructor privado para que no se instancie esta clase utilitaria
    }

    public static void importarCSV(Connection conn) {
        try {
            // Obtener DAOs desde la factory
            DAOFactory factory = DAOFactory.getDAOFactory(DBType.MYSQL, conn);
            ClienteDAO clienteDAO = factory.getClienteDAO();
            ProductoDAO productoDAO = factory.getProductoDAO();
            FacturaDAO facturaDAO = factory.getFacturaDAO();
            FacturaProductoDAO facturaProductoDAO = factory.getFacturaProductoDAO();

            // Instanciar el CsvLoader
            CsvLoader loader = new CsvLoader(clienteDAO, productoDAO, facturaDAO, facturaProductoDAO);

            // Importar todos los archivos
            loader.importAll(
                    "src/main/java/resources/clientes.csv",
                    "src/main/java/resources/productos.csv",
                    "src/main/java/resources/facturas.csv",
                    "src/main/java/resources/facturas-productos.csv"
            );

            System.out.println("✔ Importación CSV completada con éxito.");

        } catch (Exception e) {
            System.err.println("❌ Error al importar CSV:");
            e.printStackTrace();
        }
    }
}
