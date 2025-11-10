package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.dto.ParadaDTO;
import org.example.microserviciomonopatines.model.Parada;
import org.example.microserviciomonopatines.repository.ParadaRepository;
import org.example.microserviciomonopatines.service.ParadaService;
import org.springframework.stereotype.Service;
import org.example.microserviciomonopatines.mapper.ParadaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParadaServiceImpl implements ParadaService {

    private final ParadaRepository repository;
    private final ParadaMapper mapper;

    public ParadaServiceImpl(ParadaRepository repository, ParadaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<ParadaDTO> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ParadaDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Parada no encontrada con id " + id));
    }

    @Override
    public ParadaDTO crear(ParadaDTO dto) {
        Parada parada = mapper.toEntity(dto);
        Parada guardada = repository.save(parada);
        return mapper.toDTO(guardada);
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
        return mapper.toDTO(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

}
