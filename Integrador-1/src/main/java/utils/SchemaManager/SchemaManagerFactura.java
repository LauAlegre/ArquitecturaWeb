package utils.SchemaManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaManagerFactura {

    public static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Factura (" +
                "idFactura INT PRIMARY KEY AUTO_INCREMENT, " +
                "idCliente INT NOT NULL, " +
                "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente))";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
