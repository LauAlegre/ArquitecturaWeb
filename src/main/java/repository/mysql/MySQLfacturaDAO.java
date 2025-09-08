package repository.mysql;

import dao.FacturaDAO;
import entity.Factura;
import factory.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLfacturaDAO implements FacturaDAO {

    private static Connection conn;

    public MySQLfacturaDAO(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Factura (" +
                "idFactura INT PRIMARY KEY AUTO_INCREMENT, " +
                "idCliente INT NOT NULL, " +
                "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente))";

        // Obtenemos la conexión, pero NO la ponemos en el try-with-resources
        Connection conn = ConnectionManager.getInstance().getConnection();
        // Solo el Statement se cierra automáticamente
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
    @Override
    public void insert(Factura f) throws SQLException {
        String sql = "INSERT INTO Factura (idFactura, idCliente) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, f.getIdFactura());
            ps.setInt(2, f.getIdCliente());
            ps.executeUpdate();
        }
    }
}
