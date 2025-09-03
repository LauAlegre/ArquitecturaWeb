package dao;

import entity.Factura;

import java.sql.SQLException;

public interface FacturaDAO {
    void createTable() throws SQLException;
    /**
     * Inserta una nueva factura en la base de datos.
     *
     * @param factura el objeto Factura a insertar.
     */
    void insert(Factura factura) throws SQLException;
}
