package org.dao;



import org.entity.Estudiante;

import java.util.List;
import java.util.Optional;

public interface EstudianteDAO {
    // a) Dar de alta un estudiante
    Estudiante alta(Estudiante estudiante);

    // c) Recuperar todos los estudiantes, ordenados (por nombre ascendente)
    List<Estudiante> findAllOrderByNombreAsc();

    // d) Recuperar un estudiante por número de libreta universitaria
    Optional<Estudiante> findByNroLibreta(String nroLibreta);

    // e) Recuperar todos los estudiantes por género
    List<Estudiante> findByGenero(String genero);

    // g) Estudiantes de una carrera filtrados por ciudad de residencia
    List<Estudiante> findByCarreraAndCiudad(long idCarrera, String ciudadResidencia);
}
