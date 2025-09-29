package repository.mysql;

import dao.ClienteDAO;
import entity.Cliente;
import entity.ClienteFacturacion;
import factory.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLClienteDAOImpl implements ClienteDAO {

    private static MySQLClienteDAOImpl instance;
    private Connection conn;

    private MySQLClienteDAOImpl() {
        this.conn = ConnectionManager.getInstance().getConnection();
    }

    public static MySQLClienteDAOImpl getInstance() {
        if (instance == null) {
            instance = new MySQLClienteDAOImpl();
        }
        return instance;
    }



    @Override
    public void insert(Cliente c) throws SQLException {
        String sql = "INSERT INTO Cliente (nombre, email) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public List<ClienteFacturacion> getClientesOrdenadosPorFacturacion() throws SQLException {
        String sql = """
           SELECT c.idCliente, c.nombre, c.email,
                              COALESCE(SUM(fp.cantidad * p.valor), 0) AS total
                       FROM Cliente c
                       LEFT JOIN Factura f           ON f.idCliente = c.idCliente
                       LEFT JOIN Factura_Producto fp ON fp.idFactura = f.idFactura
                       LEFT JOIN Producto p          ON p.idProducto = fp.idProducto
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
