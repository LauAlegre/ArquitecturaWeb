package repository.mysql;
import dao.ClienteDAO;
import entity.Cliente;
import entity.ClienteFacturacion;
import factory.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLClienteDAOImpl implements ClienteDAO {


    private static Connection conn;

    public MySQLClienteDAOImpl(Connection conn) {
        this.conn = conn;
    }


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
    public void insert(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getIdCliente());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public List<ClienteFacturacion> getClientesOrdenadosPorFacturacion() throws SQLException {
        String sql = """
            SELECT c.idCliente, c.nombre, c.email,
                   COALESCE(SUM(fp.cantidad * p.valor), 0) AS total
            FROM cliente c
            LEFT JOIN factura f           ON f.idCliente = c.idCliente
            LEFT JOIN factura_producto fp ON fp.idFactura = f.idFactura
            LEFT JOIN producto p          ON p.idProducto = fp.idProducto
            GROUP BY c.idCliente, c.nombre, c.email
            ORDER BY total DESC, c.idCliente ASC
        """;

        List<ClienteFacturacion> out = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new ClienteFacturacion(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getFloat("total")
                ));
            }
        }
        return out;
    }
}
