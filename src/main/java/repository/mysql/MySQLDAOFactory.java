package repository.mysql;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import factory.DAOFactory;

import java.sql.Connection;

public class MySQLDAOFactory extends DAOFactory {

    private static Connection conn;

    public MySQLDAOFactory(Connection conn) {
        this.conn = conn;
    }

    @Override
    public ClienteDAO getClienteDAO() {
        return new MySQLClienteDAOImpl(conn);
    }

    @Override
    public ProductoDAO getProductoDAO() {
        return new MySQLproductoDAO(conn);
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return new MySQLfacturaDAO(conn);
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new MySQLfacturaProductoDao(conn);
    }
}
