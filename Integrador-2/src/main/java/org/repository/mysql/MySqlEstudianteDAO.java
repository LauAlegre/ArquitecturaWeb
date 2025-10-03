package org.repository.mysql;

import org.dao.EstudianteDAO;
import org.entity.Estudiante;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class MySqlEstudianteDAO implements EstudianteDAO {

    private final EntityManager em;

    public MySqlEstudianteDAO(Connection em) {
        this.em = em;
    }

    @Override
    public Estudiante alta(Estudiante estudiante) {
        // Requiere transacción activa
        em.persist(estudiante);
        return estudiante;
    }

    @Override
    public List<Estudiante> findAllOrderByNombreAsc() {
        TypedQuery<Estudiante> q = em.createQuery(
                "SELECT e FROM Estudiante e ORDER BY e.nombre ASC",
                Estudiante.class
        );
        return q.getResultList();
    }

    @Override
    public Optional<Estudiante> findByNroLibreta(String nroLibreta) {
        List<Estudiante> r = em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.numeroDeLibreta = :nro",
                Estudiante.class
        ).setParameter("nro", nroLibreta)
         .setMaxResults(1)
         .getResultList();
        return r.isEmpty() ? Optional.empty() : Optional.of(r.get(0));
    }

    @Override
    public List<Estudiante> findByGenero(String genero) {
        return em.createQuery(
                "SELECT e FROM Estudiante e WHERE e.genero = :genero ORDER BY e.nombre ASC",
                Estudiante.class
        ).setParameter("genero", genero)
         .getResultList();
    }

    @Override
    public List<Estudiante> findByCarreraAndCiudad(long idCarrera, String ciudadResidencia) {
        return em.createQuery(
                "SELECT e " +
                "FROM Inscripcion i " +
                "JOIN i.estudiante e " +
                "JOIN i.carrera c " +
                "WHERE c.id_carrera = :idCarrera " +
                "AND e.ciudadResidencia = :ciudad " +
                "ORDER BY e.nombre ASC",
                Estudiante.class
        ).setParameter("idCarrera", idCarrera)
         .setParameter("ciudad", ciudadResidencia)
         .getResultList();
    }
}
