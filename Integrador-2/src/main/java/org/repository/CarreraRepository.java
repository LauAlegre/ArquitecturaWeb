package org.repository;

import java.util.List;

import org.dto.CarreraDTO;
import org.entity.Carrera;

public interface CarreraRepository {
    // f) Carreras con estudiantes inscriptos, ordenadas por cantidad desc.
    // Cada elemento: [org.example.Carrera carrera, Long cantidadInscriptos]
    List<CarreraDTO>findCarrerasConInscriptosOrdenadasPorCantidadDesc();
    void alta (Carrera carrera);
    void cargarCarrera(int id_carrera, String nombre, int duracion_anios);
    CarreraDTO getCarreraById(int idCarrera);
}


