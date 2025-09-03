package dao;

import entity.Producto;
import entity.ProductoRecaudado;

import java.sql.SQLException;
import java.util.List;

public interface ProductoDAO {
    void createTable() throws SQLException;
    /**
     * Inserta un nuevo producto en la base de datos.
     * @param producto el objeto Producto a insertar.
     */
    void insert(Producto producto) throws SQLException;

    /**
     * Tarea 3: Devuelve el producto que más ha recaudado.
     *
     * @return un DTO con la información del producto y su recaudación total.
     */
    ProductoRecaudado getProductoConMayorRecaudacion() throws SQLException;

    /**
     * Obtiene todos los productos de la base de datos.
     * @return una lista con todos los productos.
     */
    List<Producto> getAll() throws SQLException;
}
