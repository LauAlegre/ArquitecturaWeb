package dao;

import entity.FacturaProducto;

import java.sql.SQLException;

public interface FacturaProductoDAO {

    /**
     * Inserta una nueva relación factura-producto en la base de datos.
     * @param facturaProducto el objeto que representa la relación.
     */
    void insert(FacturaProducto facturaProducto) throws SQLException;
}
