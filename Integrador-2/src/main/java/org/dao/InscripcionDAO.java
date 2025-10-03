package org.dao;

import org.entity.Inscripcion;

import java.time.LocalDate;

public interface InscripcionDAO {
    // b) Matricular un estudiante en una carrera con fecha de inscripción
    void matricular(long idEstudiante, long idCarrera, LocalDate fechaInscripcion);
    void alta(Inscripcion inscripcion);
}
