package org.repository;

import org.dao.*;
import org.factory.DAOFactory;
import org.repository.mysql.MySqlCarreraDAO;
import org.repository.mysql.MySqlEstudianteDAO;
import org.repository.mysql.MySqlIncripcionDAO;

import java.sql.Connection;

public class MySQLDaoFactory extends DAOFactory {

    private final Connection conn;

    public MySQLDaoFactory(Connection conn) {
        this.conn = conn;
    }

    @Override
    public EstudianteDAO getEstudianteDAO() {
        return new MySqlEstudianteDAO(conn);
    }

    @Override
    public CarreraDAO getCarreraDAO() {
        return new MySqlCarreraDAO(conn);
    }

    @Override
    public InscripcionDAO getInscripcionDAO() {
        return new MySqlIncripcionDAO(conn);
    }
}
