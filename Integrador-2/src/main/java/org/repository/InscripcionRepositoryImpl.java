package org.repository;

import org.entity.Carrera;
import org.entity.Estudiante;
import org.entity.Inscripcion;
import org.entity.InscripcionId;

import javax.persistence.EntityManager;
import java.util.List;

public class InscripcionRepositoryImpl implements InscripcionRepository {

    private static volatile InscripcionRepositoryImpl INSTANCE;

    private final EntityManager em;

    private InscripcionRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public static InscripcionRepositoryImpl getInstance(EntityManager em) {
        if (INSTANCE == null) {
            synchronized (InscripcionRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new InscripcionRepositoryImpl(em);
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public void matricular(int idEstudiante, int idCarrera, int fechaInscripcion, int fechaGraduado, int antiguedad) {
        // Construir clave compuesta
        InscripcionId key = new InscripcionId();
        key.setIdEstudiante(idEstudiante);
        key.setIdCarrera(idCarrera);

        // Verificar si ya existe una inscripción con esa clave
        Inscripcion existente = em.find(Inscripcion.class, key);
        if (existente != null) {
            // Actualizar datos (reglas simples: setear año de inscripción y graduación si viene > 0)
            if (fechaInscripcion > 0) {
                existente.setFechaInscripcion(fechaInscripcion);
            }
            if (fechaGraduado > 0) {
                existente.setFechaGraduado(fechaGraduado);
            }
            // No persistir una nueva; 'existente' ya está gestionada
            return;
        }

        // Crear nueva inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(key);

        // Asociaciones con proxies para evitar SELECT
        Estudiante estRef = em.getReference(Estudiante.class, idEstudiante);
        Carrera carRef = em.getReference(Carrera.class, idCarrera);
        inscripcion.setEstudiante(estRef);
        inscripcion.setCarrera(carRef);
        inscripcion.setAntiguedad(antiguedad);

        // Años (0 implica "sin graduación")
        inscripcion.setFechaInscripcion(fechaInscripcion);
        inscripcion.setFechaGraduado(fechaGraduado > 0 ? fechaGraduado : 0);

        alta(inscripcion);
    }

    @Override
    public void alta(Inscripcion inscripcion) {
        // Usar merge para soportar tanto alta como actualización sin lanzar EntityExistsException
        em.merge(inscripcion);
    }
    @Override
    public List<Object[]> getReporteCarrerasPorAnio() {
        return em.createQuery(
                "SELECT c.nombre, i.fechaInscripcion AS anio, " +
                        "COUNT(i) AS inscriptos, " +
                        "SUM(CASE WHEN i.fechaGraduado > 0 THEN 1 ELSE 0 END) AS egresados " +
                        "FROM Inscripcion i " +
                        "JOIN i.carrera c " +
                        "GROUP BY c.id_carrera, c.nombre, i.fechaInscripcion " +
                        "ORDER BY c.nombre ASC, i.fechaInscripcion",
                Object[].class
        ).getResultList();
    }


}
