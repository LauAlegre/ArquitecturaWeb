package org.repository;


import org.entity.Estudiante;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import org.dto.EstudianteDTO;

public class EstudianteRepositoryImpl implements EstudianteRepository {

    private static EstudianteRepositoryImpl INSTANCE;

    private final EntityManager em;

    private EstudianteRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    public static EstudianteRepositoryImpl getInstance(EntityManager em) {
        if (INSTANCE == null) {
            synchronized (EstudianteRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE = new EstudianteRepositoryImpl(em);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public Estudiante alta(Estudiante estudiante) {
        // Requiere transacción activa
        em.persist(estudiante);
        return estudiante;
    }

    @Override
    public void cargarEstudiante(int nroLibreta, String nombre, String apellido, String genero, String ciudadResidencia, int nroDocumento) {

        // 🔹 Verificar si ya existe un estudiante con el mismo nroDocumento o nroLibreta
        boolean existe = !em.createQuery(
                        "SELECT e FROM Estudiante e " +
                                "WHERE e.nroDocumento = :dni OR e.numeroDeLibreta = :libreta",
                        Estudiante.class
                )
                .setParameter("dni", nroDocumento)
                .setParameter("libreta", nroLibreta)
                .getResultList()
                .isEmpty();

        if (existe) {
            System.out.printf("⚠️ Estudiante duplicado detectado → DNI: %d | LU: %d | %s %s%n",
                    nroDocumento, nroLibreta, nombre, apellido);
            return; // ❌ No lo cargamos otra vez
        }

        // 🔹 Si no existe, lo creamos y persistimos
        Estudiante e = new Estudiante();
        e.setNroLibretaUniversitaria(nroLibreta);
        e.setNombre(nombre + " " + apellido);
        e.setGenero(genero);
        e.setNroDocumento(nroDocumento);
        e.setCiudadResidencia(ciudadResidencia);

        alta(e);
        System.out.printf("✅ Estudiante agregado → DNI: %d | LU: %d | %s %s%n",
                nroDocumento, nroLibreta, nombre, apellido);
    }


    @Override
    public List<EstudianteDTO> findAllOrderByNombreAsc() {
        String jpql = "SELECT new org.dto.EstudianteDTO(e.nroDocumento, e.nombre, '', e.ciudadResidencia, e.genero, e.numeroDeLibreta) " +
                      "FROM Estudiante e " +
                      "ORDER BY e.nombre ASC";
        TypedQuery<EstudianteDTO> query = em.createQuery(jpql, EstudianteDTO.class);
        return query.getResultList();
    }



    @Override
    public EstudianteDTO findByNroLibreta(int nroLibreta) {
        try {
            return em.createQuery(
                    "SELECT new org.dto.EstudianteDTO(e.nroDocumento, e.nombre, '', e.ciudadResidencia, e.genero, e.numeroDeLibreta) " +
                    "FROM Estudiante e WHERE e.numeroDeLibreta = :nroLibreta",
                    EstudianteDTO.class
            ).setParameter("nroLibreta", nroLibreta)
             .getSingleResult();
        } catch (NoResultException e) {
            return null; // O manejarlo de otra manera según tus necesidades
        }
    }


    @Override
    public List<EstudianteDTO> findByGenero(String genero) {
        String jpql = "SELECT new org.dto.EstudianteDTO(e.nroDocumento, e.nombre, '', e.ciudadResidencia, e.genero, e.numeroDeLibreta) " +
                      "FROM Estudiante e " +
                      "WHERE e.genero = :genero " +
                      "ORDER BY e.nombre ASC";
        TypedQuery<EstudianteDTO> query = em.createQuery(jpql, EstudianteDTO.class);
        query.setParameter("genero", genero);
        return query.getResultList();
    }


    @Override
    public EstudianteDTO findById(int id) {
        try {
            return em.createQuery(
                    "SELECT new org.dto.EstudianteDTO(e.nroDocumento, e.nombre, '', e.ciudadResidencia, e.genero, e.numeroDeLibreta) " +
                    "FROM Estudiante e WHERE e.nroDocumento = :id",
                    EstudianteDTO.class
            ).setParameter("id", id)
             .getSingleResult();
        } catch (NoResultException e) {
            return null; // O manejarlo de otra manera según tus necesidades
        }
    }


    // g) Estudiantes de una carrera filtrados por ciudad de residencia

    @Override
    public List<EstudianteDTO> findByCarreraAndCiudad(int idCarrera, String ciudad) {
        String jpql =
                "SELECT new org.dto.EstudianteDTO(" +
                        " e.nroDocumento, e.nombre, '', e.ciudadResidencia, e.genero, e.numeroDeLibreta) " +
                        "FROM Inscripcion i " +
                        "JOIN i.estudiante e " +
                        "WHERE i.carrera.id_carrera = :idCarrera " +
                        "AND UPPER(e.ciudadResidencia) = :ciudad " +
                        "ORDER BY e.nombre ASC";

        return em.createQuery(jpql, EstudianteDTO.class)
                .setParameter("idCarrera", idCarrera)
                .setParameter("ciudad", ciudad.trim().toUpperCase())
                .getResultList();
    }

}
