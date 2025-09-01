package repository.mysql;
import dao.ClienteDAO;
import entity.Cliente;
import factory.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class MySQLClienteDAOImpl implements ClienteDAO {
    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Cliente (" +
                "idCliente INT PRIMARY KEY AUTO_INCREMENT, " +
                "nombre VARCHAR(500) NOT NULL, " +
                "email VARCHAR(150))";

        // Obtenemos la conexión, pero NO la ponemos en el try-with-resources
        Connection conn = ConnectionManager.getInstance().getConnection();
        // Solo el Statement se autogestiona y cierra
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    @Override
    public void insert(Cliente cliente) throws SQLException {

    }

    @Override
    public List<Cliente> getClientesOrdenadosPorFacturacion() throws SQLException {
        return new LinkedList<>();
    }
}
