package utils.SchemaManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SchemaManagerCliente {



    public static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Cliente (" +
                "idCliente INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre VARCHAR(500) NOT NULL, " +
                "email VARCHAR(150))";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
