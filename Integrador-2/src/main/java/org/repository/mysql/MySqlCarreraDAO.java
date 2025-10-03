package org.repository.mysql;

import org.dao.CarreraDAO;

import javax.persistence.EntityManager;
import java.util.List;

public class MySqlCarreraDAO implements CarreraDAO {

    private final EntityManager em;

    public MySqlCarreraDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Object[]> findCarrerasConInscriptosOrdenadasPorCantidadDesc() {
        return em.createQuery(
                "SELECT c, COUNT(i) FROM Carrera c, Inscripcion i " +
                "WHERE i.id = c.inscripciones " +
                "GROUP BY c " +
                "ORDER BY COUNT(i) DESC",
                Object[].class
        ).getResultList();
    }
}



