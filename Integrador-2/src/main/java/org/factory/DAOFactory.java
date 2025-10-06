package org.factory;

import org.dao.CarreraDAO;
import org.dao.EstudianteDAO;
import org.dao.InscripcionDAO;
import org.repository.CarreraRepository;
import org.repository.EstudianteRepository;
import org.repository.InscripcionRepository;


import javax.persistence.EntityManager;

public class DAOFactory {

    private final EntityManager conn;

    public DAOFactory(EntityManager conn) {
        this.conn = conn;
    }


    public EstudianteDAO getEstudianteDAO() {
        return  EstudianteRepository.getInstance(conn);
    }


    public CarreraDAO getCarreraDAO() {
        return  CarreraRepository.getInstance(conn);
    }


    public InscripcionDAO getInscripcionDAO() {
        return  InscripcionRepository.getInstance(conn);
    }
}