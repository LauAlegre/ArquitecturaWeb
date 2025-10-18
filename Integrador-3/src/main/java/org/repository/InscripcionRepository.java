package org.repository;

import org.model.Inscripcion;
import org.model.InscripcionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface InscripcionRepository  extends JpaRepository<Inscripcion, InscripcionId> {
//h) generar un reporte de las carreras, que para cada carrera incluya información de los inscriptos y egresados por año.
// Se deben ordenar las carreras alfabéticamente, y presentar los años de manera cronológica.
    @Query("""
    SELECT 
        c.nombre AS carrera,
        i.fechaInscripcion AS anio,
        COUNT(i) AS inscriptos,
        SUM(CASE WHEN i.fechaGraduado > 0 THEN 1 ELSE 0 END) AS egresados
    FROM Inscripcion i
    JOIN i.carrera c
    GROUP BY c.nombre, i.fechaInscripcion
    ORDER BY c.nombre ASC, i.fechaInscripcion ASC
""")
    List<Object[]> getReporteCarrerasPorAnio();



}
