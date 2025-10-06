package org.dao;

import org.dto.ReporteCarreraDTO;
import org.entity.Carrera;
import org.entity.Estudiante;
import org.entity.Inscripcion;

import java.time.LocalDate;
import java.util.List;

public interface InscripcionDAO {
    // b) Matricular un estudiante en una carrera con fecha de inscripción
    void matricular(int idEstudiante, int idCarrera, int fechaInscripcion,int fechaGraduado, int antiguedad);
    void alta(Inscripcion inscripcion);
    List<Object[]> getReporteCarrerasPorAnio();


}
