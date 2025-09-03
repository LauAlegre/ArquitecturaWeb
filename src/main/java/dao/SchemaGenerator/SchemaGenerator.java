package dao.SchemaGenerator;




import dao.*;
import factory.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase de utilidad para crear el esquema de la base de datos.
 * Llama a los métodos createTable() de cada DAO en el orden correcto.
 */
public class SchemaGenerator {

    private static Connection conn = null;

    public SchemaGenerator(Connection conn) {
        this.conn = conn;
    }

    public static void createSchema() {
        // 1. Obtener la fábrica para la base de datos que vamos a usar (MySQL).
        DAOFactory mySQLFactory = DAOFactory.getDAOFactory(DBType.MYSQL, conn);

        // 2. Obtener una instancia de cada DAO a través de la fábrica.
        ClienteDAO clienteDAO = mySQLFactory.getClienteDAO();
        ProductoDAO productoDAO = mySQLFactory.getProductoDAO();
        FacturaDAO facturaDAO = mySQLFactory.getFacturaDAO();
        FacturaProductoDAO facturaProductoDAO = mySQLFactory.getFacturaProductoDAO();

        try {
            System.out.println("Iniciando la creación del esquema...");

            // 3. Llamar a los métodos createTable() en el orden de dependencia.
            // Primero las tablas que no dependen de ninguna otra.
            System.out.println("Creando tabla Cliente...");
            clienteDAO.createTable();

            System.out.println("Creando tabla Producto...");
            productoDAO.createTable();

            // Luego, las tablas que dependen de las anteriores.
            System.out.println("Creando tabla Factura (depende de Cliente)...");
            facturaDAO.createTable();

            System.out.println("Creando tabla Factura_Producto (depende de Factura y Producto)...");
            facturaProductoDAO.createTable();

            System.out.println("¡Esquema creado o verificado con éxito!");

        } catch (SQLException e) {
            System.err.println("Error al crear el esquema de la base de datos.");
            e.printStackTrace();
        }
    }
}

