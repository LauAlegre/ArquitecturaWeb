package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URI = "jdbc:mysql://localhost:3306/nombre_de_tu_bd";
    private static final String USER = "tu_usuario";
    private static final String PASS = "tu_contraseña";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URI, USER, PASS);
    }
}
