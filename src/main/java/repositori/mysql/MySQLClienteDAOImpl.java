package repositori.mysql;

import dao.ClienteDAO;
import entity.Cliente;

import java.sql.SQLException;
import java.util.List;

public class MySQLClienteDAOImpl implements ClienteDAO {
    @Override
    public void insert(Cliente cliente) throws SQLException {

    }

    @Override
    public List<Cliente> getClientesOrdenadosPorFacturacion() throws SQLException {
        return List.of();
    }
}
