package org.dao;



import org.dto.EstudianteDTO;
import org.entity.Estudiante;

import java.util.List;
import java.util.Optional;

public interface EstudianteDAO {
    // a) Dar de alta un estudiante
    Estudiante alta(Estudiante estudiante);
    void cargarEstudiante(int nroLibreta, String nombre, String apellido, String genero, String ciudadResidencia,
                         int nroDocumento);

    // c) Recuperar todos los estudiantes, ordenados (por nombre ascendente)
    List<EstudianteDTO> findAllOrderByNombreAsc();

    // d) Recuperar un estudiante por número de libreta universitaria
    EstudianteDTO findByNroLibreta(int nroLibreta);

    // e) Recuperar todos los estudiantes por género
    List<EstudianteDTO> findByGenero(String genero);

    // g) Estudiantes de una carrera filtrados por ciudad de residencia
    List<EstudianteDTO> findByCarreraAndCiudad(int idCarrera, String ciudadResidencia);

    EstudianteDTO findById(int id);
}
