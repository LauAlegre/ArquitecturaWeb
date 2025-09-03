package factory;

import dao.ClienteDAO;
import dao.FacturaDAO;
import dao.FacturaProductoDAO;
import dao.ProductoDAO;
import repository.mysql.MySQLDAOFactory;

import java.sql.Connection;

public abstract class DAOFactory {
    public abstract ClienteDAO getClienteDAO();
    public abstract ProductoDAO getProductoDAO();
    public abstract FacturaDAO getFacturaDAO();
    public abstract FacturaProductoDAO getFacturaProductoDAO();

    public static DAOFactory getDAOFactory(DBType type, Connection conn) {
        switch (type) {
            case MYSQL:
                return new MySQLDAOFactory(conn);
            // Aquí irían los casos para POSTGRES, etc.
            default:
                throw new IllegalArgumentException("Base de datos no soportada");
        }
    }
}