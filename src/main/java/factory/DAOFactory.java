package factory;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import repository.mysql.MySQLDAOFactory;

public abstract class DAOFactory {
    public abstract ClienteDAO getClienteDAO();
    public abstract ProductoDAO getProductoDAO();
    public abstract FacturaDAO getFacturaDAO();
    public abstract FacturaProductoDAO getFacturaProductoDAO();

    public static DAOFactory getDAOFactory(DBType type) {
        switch (type) {
            case MYSQL:
                return new MySQLDAOFactory();
            // Aquí irían los casos para POSTGRES, etc.
            default:
                throw new IllegalArgumentException("Base de datos no soportada");
        }
    }
}