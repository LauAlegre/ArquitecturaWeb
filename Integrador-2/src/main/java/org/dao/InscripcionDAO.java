package org.dao;

import java.time.LocalDate;

public interface InscripcionDAO {
    // b) Matricular un estudiante en una carrera con fecha de inscripción
    void matricular(long idEstudiante, long idCarrera, LocalDate fechaInscripcion);
}
