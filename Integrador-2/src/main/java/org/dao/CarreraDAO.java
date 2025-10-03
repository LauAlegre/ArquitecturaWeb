package org.dao;

import java.util.List;
import org.entity.Carrera;

public interface CarreraDAO {
    // f) Carreras con estudiantes inscriptos, ordenadas por cantidad desc.
    // Cada elemento: [org.example.Carrera carrera, Long cantidadInscriptos]
    List<Object[]> findCarrerasConInscriptosOrdenadasPorCantidadDesc();
    void alta (Carrera carrera);
}


