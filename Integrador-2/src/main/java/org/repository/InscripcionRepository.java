package org.repository;

import org.entity.Inscripcion;

import java.util.List;

public interface InscripcionRepository {
    // b) Matricular un estudiante en una carrera con fecha de inscripción
    void matricular(int idEstudiante, int idCarrera, int fechaInscripcion,int fechaGraduado, int antiguedad);
    void alta(Inscripcion inscripcion);
    List<Object[]> getReporteCarrerasPorAnio();


}
