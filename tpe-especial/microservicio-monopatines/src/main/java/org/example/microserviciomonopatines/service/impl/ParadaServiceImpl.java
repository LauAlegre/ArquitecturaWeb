package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.dto.ParadaDTO;
import org.example.microserviciomonopatines.model.Parada;
import org.example.microserviciomonopatines.repository.ParadaRepository;
import org.example.microserviciomonopatines.service.ParadaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParadaServiceImpl implements ParadaService {

    private final ParadaRepository repository;

    public ParadaServiceImpl(ParadaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ParadaDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParadaDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Parada no encontrada con id " + id));
    }

    @Override
    public ParadaDTO crear(ParadaDTO dto) {
        Parada parada = toEntity(dto);
        Parada guardada = repository.save(parada);
        return toDTO(guardada);
    }

    @Override
    public ParadaDTO actualizar(Long id, ParadaDTO dto) {
        Parada existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parada no encontrada con id " + id));

        existente.setNombre(dto.getNombre());
        existente.setLatitud(dto.getLatitud());
        existente.setLongitud(dto.getLongitud());
        existente.setCapacidad(dto.getCapacidad());

        Parada actualizada = repository.save(existente);
        return toDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // --- Métodos auxiliares de mapeo ---

    private ParadaDTO toDTO(Parada p) {
        return new ParadaDTO(
                p.getId(),
                p.getNombre(),
                p.getLatitud(),
                p.getLongitud(),
                p.getCapacidad());
    }

    private Parada toEntity(ParadaDTO dto) {
        Parada p = new Parada();
        p.setNombre(dto.getNombre());
        p.setLatitud(dto.getLatitud());
        p.setLongitud(dto.getLongitud());
        p.setCapacidad(dto.getCapacidad());
        return p;
    }
}
