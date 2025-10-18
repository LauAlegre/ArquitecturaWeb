package org.repository;

import org.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // c) todos los estudiantes ordenados por apellido ASC
    @Query("SELECT e FROM Estudiante e ORDER BY e.apellido ASC")
    List<Estudiante> findAllByOrderByApellidoAsc();

    // d) por número de libreta universitaria
    @Query("SELECT e FROM Estudiante e WHERE e.numeroDeLibreta = :nroLibreta")
    Optional<Estudiante> findByNroLibreta(@Param("nroLibreta") Long nroLibreta);

    // e) por género (case-insensitive)
    @Query("SELECT e FROM Estudiante e WHERE LOWER(e.genero) = LOWER(:genero)")
    List<Estudiante> findByGeneroIgnoreCase(@Param("genero") String genero);

    // g) por carrera (id) y ciudad de residencia
    @Query("""
        SELECT e FROM Estudiante e 
        JOIN e.inscripciones i 
        WHERE i.carrera.id_carrera = :idCarrera 
        AND e.ciudadResidencia = :ciudadResidencia
    """)
    List<Estudiante> findByCarreraIdAndCiudadResidencia(@Param("idCarrera") Long idCarrera,
                                                        @Param("ciudadResidencia") String ciudadResidencia);
}
