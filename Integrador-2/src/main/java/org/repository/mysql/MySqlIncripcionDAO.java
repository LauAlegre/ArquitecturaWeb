package org.repository.mysql;

import org.dao.InscripcionDAO;
import org.entity.Carrera;
import org.entity.Estudiante;
import org.entity.Inscripcion;
import org.entity.InscripcionId;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.sql.Date;

public class MySqlIncripcionDAO implements InscripcionDAO {

    private final EntityManager em;

    public MySqlIncripcionDAO(EntityManager em) {
        this.em = em;
    }

    @Override
    public void matricular(long idEstudiante, long idCarrera, LocalDate fechaInscripcion) {
        // Requiere transacción activa
        Inscripcion inscripcion = new Inscripcion();

        // Clave compuesta
        InscripcionId key = new InscripcionId();
        key.setIdEstudiante((int) idEstudiante);
        key.setIdCarrera((int) idCarrera);
        inscripcion.setId(key);

        // Asociaciones (evita SELECT usando referencias proxy)
        Estudiante estRef = em.getReference(Estudiante.class, (int) idEstudiante);
        Carrera carRef = em.getReference(Carrera.class, (int) idCarrera);
        inscripcion.setEstudiante(estRef);
        inscripcion.setCarrera(carRef);

        // Fecha de inscripción (java.time -> java.util.Date)
        inscripcion.setFechaInscripcion(Date.valueOf(fechaInscripcion));

        em.persist(inscripcion);
    }
}
