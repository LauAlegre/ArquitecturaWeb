package dao;

import entity.Cliente;
import entity.ClienteFacturacion;

import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    void createTable() throws SQLException;
    /**
     * Inserta un nuevo cliente en la base de datos.
     * @param cliente el objeto Cliente a insertar.
     */
    void insert(Cliente cliente) throws SQLException;

    /**
     * Tarea 4: Devuelve una lista de clientes ordenada por el total de su facturación.
     *
     * @return una lista de DTOs con la información del cliente y su facturación total.
     */
    List<ClienteFacturacion> getClientesOrdenadosPorFacturacion() throws SQLException;
}
