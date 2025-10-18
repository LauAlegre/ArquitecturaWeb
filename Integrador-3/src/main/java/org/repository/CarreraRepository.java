package org.repository;

import org.dto.CarreraDTO;
import org.model.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    // f) recuperar las carreras con estudiantes inscriptos, ordenadas por cantidad de inscriptos (desc)
    @Query("""
    SELECT new org.dto.CarreraDTO(
        c.id_carrera,
        c.nombre,
        c.duracion_anios,
        COUNT(i)
    )
    FROM Carrera c
    LEFT JOIN c.inscripciones i
    GROUP BY c.id_carrera, c.nombre, c.duracion_anios
    ORDER BY COUNT(i) DESC
""")
    List<CarreraDTO> findCarrerasConInscriptosOrderByCantidadDesc();





}
