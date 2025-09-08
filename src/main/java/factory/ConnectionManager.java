package factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    // 1. La única instancia de la clase.
    private static ConnectionManager instance;

    // 2. La conexión es un atributo de la instancia.
    private Connection connection;

    // Constantes de conexión
    private static final String URI = "jdbc:mysql://localhost:3306/practico"; // Reemplazar
    private static final String USER = "lautaro"; // Reemplazar
    private static final String PASS = "1234"; // Reemplazar

    // 3. El constructor es PRIVADO. Solo se puede llamar desde esta misma clase.
    private ConnectionManager() {
        try {
            // La conexión se crea UNA SOLA VEZ, cuando se crea la instancia.
            this.connection = DriverManager.getConnection(URI, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            // En una app real, aquí se manejaría el error de forma más robusta.
        }
    }

    // 4. El método público para obtener la única instancia.
    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    // 5. El método para obtener la conexión ya creada.
    public Connection getConnection() {
        return this.connection;
    }
}
