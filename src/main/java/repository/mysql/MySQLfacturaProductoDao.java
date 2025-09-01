package repository.mysql;
import dao.FacturaProductoDAO;
import factory.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLfacturaProductoDao implements FacturaProductoDAO {
    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Factura_Producto (" +
                "idFactura INT, " +
                "idProducto INT, " +
                "cantidad INT, " +
                "PRIMARY KEY (idFactura, idProducto), " +
                "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura), " +
                "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto))";

        // Obtenemos la conexión, pero NO la ponemos en el try-with-resources
        Connection conn = ConnectionManager.getInstance().getConnection();
        // Solo el Statement se cierra automáticamente
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    @Override
    public void insert(entity.FacturaProducto facturaProducto) {

    }
}
