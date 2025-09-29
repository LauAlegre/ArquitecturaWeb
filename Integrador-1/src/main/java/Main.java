import utils.SchemaGenerator.SchemaGenerator;
import utils.CSVImporter.CSVImporter;
import repository.mysql.MySQLproductoDAO;
import repository.mysql.MySQLClienteDAOImpl;
import entity.ProductoRecaudado;
import entity.ClienteFacturacion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

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

            // =============================
            // 📌 USO DE LOS MÉTODOS DAO
            // =============================

            // 1. Producto con mayor recaudación
            ProductoRecaudado pr = MySQLproductoDAO.getInstance().getProductoConMayorRecaudacion();
            if (pr != null) {
                System.out.println("\n💰 Producto con mayor recaudación:");
                System.out.println("ID: " + pr.getIdProducto());
                System.out.println("Nombre: " + pr.getNombre());
                System.out.println("Recaudación: $" + pr.getRecaudacion());
            } else {
                System.out.println("\n⚠️ No hay productos con recaudación.");
            }

            // 2. Clientes ordenados por facturación
            System.out.println("\n📊 Clientes ordenados por facturación:");
            List<ClienteFacturacion> clientes = MySQLClienteDAOImpl.getInstance().getClientesOrdenadosPorFacturacion();
            for (ClienteFacturacion c : clientes) {
                System.out.println("ID: " + c.getIdCliente() +
                        " | Nombre: " + c.getNombre() +
                        " | Email: " + c.getEmail() +
                        " | Total facturado: $" + c.getTotal());
            }

        } catch (Exception e) {
            System.err.println("❌ Error en la aplicación:");
            e.printStackTrace();
        }

        System.out.println("\nProceso finalizado.");
    }
}
