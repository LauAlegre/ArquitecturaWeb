package org.repository;

import org.dao.CarreraDAO;
import org.dto.CarreraDTO;
import org.entity.Carrera;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class CarreraRepository implements CarreraDAO {

    private static volatile CarreraRepository INSTANCE;

    private final EntityManager em;

    private CarreraRepository(EntityManager em) {
        this.em = em;
    }

    public static CarreraRepository getInstance(EntityManager em) {
        if (INSTANCE == null) {
            synchronized (CarreraRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CarreraRepository(em);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public List<CarreraDTO>findCarrerasConInscriptosOrdenadasPorCantidadDesc(){
        String jpql = "SELECT new org.dto.CarreraDTO(c.id_carrera, c.nombre, COUNT(i)) " +
                "FROM Carrera c LEFT JOIN c.inscripciones i " +
                "GROUP BY c.id_carrera, c.nombre " +
                "ORDER BY COUNT(i) DESC";
        return em.createQuery(jpql, CarreraDTO.class).getResultList();
    }


    public  void alta(Carrera carrera) {
        em.persist(carrera);
    }

    @Override
    public void cargarCarrera(int id_carrera, String nombre, int duracion_anios) {
        // 🔹 Verificar si ya existe una carrera con el mismo ID o nombre
        boolean existe = !em.createQuery(
                        "SELECT c FROM Carrera c " +
                                "WHERE c.id_carrera = :id OR UPPER(c.nombre) = :nombre",
                        Carrera.class
                )
                .setParameter("id", id_carrera)
                .setParameter("nombre", nombre.trim().toUpperCase())
                .getResultList()
                .isEmpty();

        if (existe) {
            System.out.printf("⚠️ Carrera duplicada detectada → ID: %d | Nombre: %s%n", id_carrera, nombre);
            return; // ❌ No se vuelve a cargar
        }

        // 🔹 Si no existe, la persistimos
        Carrera c = new Carrera();
        c.setId_carrera(id_carrera);
        c.setNombre(nombre.trim());
        c.setDuracion_anios(duracion_anios);

        alta(c);
        System.out.printf("✅ Carrera agregada → ID: %d | Nombre: %s | Duración: %d años%n",
                id_carrera, nombre, duracion_anios);
    }


    @Override
    public CarreraDTO getCarreraById(int idCarrera) {
        try {
            return em.createQuery(
                            "SELECT new org.dto.CarreraDTO(c.id_carrera, c.nombre, 0L) " +
                                    "FROM Carrera c WHERE c.id_carrera = :idCarrera",
                            CarreraDTO.class
                    )
                    .setParameter("idCarrera", idCarrera)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }



}
