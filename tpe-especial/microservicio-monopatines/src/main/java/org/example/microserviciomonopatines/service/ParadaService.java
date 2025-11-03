package org.example.microserviciomonopatines.service;

import org.example.microserviciomonopatines.dto.ParadaDTO;
import java.util.List;

public interface ParadaService {

    List<ParadaDTO> listar();

    ParadaDTO buscarPorId(Long id);

    ParadaDTO crear(ParadaDTO dto);

    ParadaDTO actualizar(Long id, ParadaDTO dto);

    void eliminar(Long id);
}
