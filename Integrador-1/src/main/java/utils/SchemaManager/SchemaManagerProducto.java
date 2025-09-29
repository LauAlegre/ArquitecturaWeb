package utils.SchemaManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaManagerProducto {
    public static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Producto (" +
                "idProducto INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre VARCHAR(45) NOT NULL, " +
                "valor FLOAT NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
