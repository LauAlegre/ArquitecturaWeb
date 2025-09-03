package repository.mysql;
import dao.ProductoDAO;
import entity.Producto;
import entity.ProductoRecaudado;
import factory.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MySQLproductoDAO implements ProductoDAO {

    private  static Connection conn;

    public MySQLproductoDAO(Connection conn) {
        this.conn = conn;
    }

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
    public void insert(Producto p) throws SQLException {
        String sql = "INSERT INTO producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, p.getIdProducto());
            ps.setString(2, p.getNombre());
            ps.setFloat(3, p.getValor()); // mejor BigDecimal que float
            ps.executeUpdate();
        }
    }

    @Override
    public ProductoRecaudado getProductoConMayorRecaudacion() throws SQLException {
        String sql = """
            SELECT p.idProducto, p.nombre,
                   SUM(fp.cantidad * p.valor) AS recaudacion
            FROM factura_producto fp
            JOIN producto p ON p.idProducto = fp.idProducto
            GROUP BY p.idProducto, p.nombre
            ORDER BY recaudacion DESC
            LIMIT 1
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            return new ProductoRecaudado(
                    rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getFloat("recaudacion")
            );
        }
    }

    @Override
    public List<Producto> getAll() throws SQLException {
        String sql = "SELECT idProducto, nombre, valor FROM producto ORDER BY idProducto";
        List<Producto> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre"),
                        rs.getFloat("valor")
                ));
            }
        }
        return out;
    }
}
