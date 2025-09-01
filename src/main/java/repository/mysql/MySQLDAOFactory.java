package repository.mysql;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import factory.DAOFactory;

public class MySQLDAOFactory extends DAOFactory {
    @Override
    public ClienteDAO getClienteDAO() {
        return new MySQLClienteDAOImpl();
    }

    @Override
    public ProductoDAO getProductoDAO() {
        return new MySQLproductoDAO();
    }

    @Override
    public FacturaDAO getFacturaDAO() {
        return new MySQLfacturaDAO();
    }

    @Override
    public FacturaProductoDAO getFacturaProductoDAO() {
        return new MySQLfacturaProductoDao();
    }
}
