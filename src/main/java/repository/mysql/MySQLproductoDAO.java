package repository.mysql;
import dao.ProductoDAO;
import entity.Producto;
import factory.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class MySQLproductoDAO implements ProductoDAO {
    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Producto (" +
                "idProducto INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre VARCHAR(45) NOT NULL, " +
                "valor FLOAT NOT NULL)";

        // Obtenemos la conexión, pero NO la ponemos en el try-with-resources
        Connection conn = ConnectionManager.getInstance().getConnection();
        // Solo el Statement se autogestiona y cierra
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    @Override
    public void insert(Producto producto) throws SQLException {

    }

    @Override
    public Producto getProductoConMayorRecaudacion() throws SQLException {
        return null;
    }

    @Override
    public List<Producto> getAll() throws SQLException {
        return  new LinkedList<>();
    }
}
