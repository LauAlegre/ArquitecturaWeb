package org.factory;

import org.repository.CarreraRepository;
import org.repository.EstudianteRepository;
import org.repository.InscripcionRepository;
import org.repository.CarreraRepositoryImpl;
import org.repository.EstudianteRepositoryImpl;
import org.repository.InscripcionRepositoryImpl;


import javax.persistence.EntityManager;

public class DAOFactory {

    private final EntityManager conn;

    public DAOFactory(EntityManager conn) {
        this.conn = conn;
    }


    public EstudianteRepository getEstudianteDAO() {
        return  EstudianteRepositoryImpl.getInstance(conn);
    }


    public CarreraRepository getCarreraDAO() {
        return  CarreraRepositoryImpl.getInstance(conn);
    }


    public InscripcionRepository getInscripcionDAO() {
        return  InscripcionRepositoryImpl.getInstance(conn);
    }
}