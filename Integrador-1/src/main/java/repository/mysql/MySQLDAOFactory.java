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
        return  MySQLClienteDAOImpl.getInstance();
    }

    @Override
    public ProductoDAO getProductoDAO() {
        return MySQLproductoDAO.getInstance();
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return MySQLfacturaDAO.getInstance();
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return  MySQLfacturaProductoDao.getInstance();
    }
}
