package org.factory;

import org.repository.CarreraRepository;
import org.repository.EstudianteRepository;
import org.repository.InscripcionRepository;
import org.repository.CarreraRepositoryImpl;
import org.repository.EstudianteRepositoryImpl;
import org.repository.InscripcionRepositoryImpl;


import javax.persistence.EntityManager;

public class Factory {

    private final EntityManager conn;

    public Factory(EntityManager conn) {
        this.conn = conn;
    }


    public EstudianteRepository getEstudianteRepositoryImpl() {
        return  EstudianteRepositoryImpl.getInstance(conn);
    }


    public CarreraRepository getCarreraRepositoryImpl() {
        return  CarreraRepositoryImpl.getInstance(conn);
    }


    public InscripcionRepository getInscripcionRepositoryImpl() {
        return  InscripcionRepositoryImpl.getInstance(conn);
    }
}