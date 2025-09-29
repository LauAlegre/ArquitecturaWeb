package utils.SchemaGenerator;

import dao.*;
import factory.*;
import utils.SchemaManager.SchemaManagerCliente;
import utils.SchemaManager.SchemaManagerFactura;
import utils.SchemaManager.SchemaManagerFacturaProducto;
import utils.SchemaManager.SchemaManagerProducto;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase de utilidad para crear el esquema de la base de datos.
 * Llama a los métodos createTable() de cada DAO en el orden correcto.
 */
public class SchemaGenerator {

    private SchemaGenerator() {
        // 🔒 Constructor privado para que no se instancie esta clase utilitaria
    }

    public static void createSchema(Connection conn) {
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
            System.out.println("Creando tabla Cliente...");
            SchemaManagerCliente.createTable(conn); // Usamos el SchemaManagerCliente para crear la tabla Cliente

            System.out.println("Creando tabla Producto...");
            SchemaManagerProducto.createTable(conn);

            System.out.println("Creando tabla Factura (depende de Cliente)...");
            SchemaManagerFactura.createTable(conn); // Aseguramos que la tabla Cliente esté creada antes de Factura

            System.out.println("Creando tabla Factura_Producto (depende de Factura y Producto)...");
            SchemaManagerFacturaProducto.createTable(conn); // Aseguramos que la tabla Producto esté creada antes de Factura_Producto

            System.out.println("¡Esquema creado o verificado con éxito!");

        } catch (SQLException e) {
            System.err.println("Error al crear el esquema de la base de datos.");
            e.printStackTrace();
        }
    }
}
