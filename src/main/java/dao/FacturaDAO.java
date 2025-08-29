package dao;

import entity.Factura;

import java.sql.SQLException;

public interface FacturaDAO {

    /**
     * Inserta una nueva factura en la base de datos.
     * @param factura el objeto Factura a insertar.
     * @return el ID autogenerado de la factura insertada.
     */
    int insert(Factura factura) throws SQLException;
}
