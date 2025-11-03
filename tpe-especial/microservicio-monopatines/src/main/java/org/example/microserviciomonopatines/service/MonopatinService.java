package org.example.microserviciomonopatines.service;

import org.example.microserviciomonopatines.dto.MonopatinDTO;
import java.util.List;
import java.util.Map;

public interface MonopatinService {

    List<MonopatinDTO> listar();

    MonopatinDTO buscarPorId(Long id);

    MonopatinDTO crear(MonopatinDTO dto);

    MonopatinDTO actualizar(Long id, MonopatinDTO dto);

    void eliminar(Long id);

    // --- Operaciones específicas ---

    MonopatinDTO cambiarEstado(Long id, String estado);

    MonopatinDTO actualizarUbicacion(Long id, Double latitud, Double longitud);

    List<MonopatinDTO> listarCercanos(Double latitud, Double longitud, Double radio);

    Map<String, Long> obtenerDisponibilidad();
}
