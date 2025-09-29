package repository.mysql;

import dao.FacturaProductoDAO;
import entity.FacturaProducto;
import factory.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLfacturaProductoDao implements FacturaProductoDAO {

    private static MySQLfacturaProductoDao instance;
    private Connection conn;

    // Constructor privado para evitar instancias externas
    private MySQLfacturaProductoDao() {
        this.conn = ConnectionManager.getInstance().getConnection();
    }

    // Método estático para obtener la instancia única (Singleton)
    public static MySQLfacturaProductoDao getInstance() {
        if (instance == null) {
            instance = new MySQLfacturaProductoDao();
        }
        return instance;
    }



    @Override
    public void insert(FacturaProducto fp) throws SQLException {
        String sql = "INSERT INTO Factura_Producto (idFactura, idProducto, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fp.getIdFactura());
            ps.setInt(2, fp.getIdProducto());
            ps.setInt(3, fp.getCantidad());
            ps.executeUpdate();
        }
    }
}
