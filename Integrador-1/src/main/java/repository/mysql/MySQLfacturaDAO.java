package repository.mysql;

import dao.FacturaDAO;
import entity.Factura;
import factory.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLfacturaDAO implements FacturaDAO {

    private static MySQLfacturaDAO instance;
    private Connection conn;

    private MySQLfacturaDAO() {
        this.conn = ConnectionManager.getInstance().getConnection();
    }

    public static MySQLfacturaDAO getInstance() {
        if (instance == null) {
            instance = new MySQLfacturaDAO();
        }
        return instance;
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
