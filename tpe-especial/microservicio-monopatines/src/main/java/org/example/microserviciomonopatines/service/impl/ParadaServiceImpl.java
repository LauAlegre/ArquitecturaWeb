package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.client.UsuarioClientMonopatines;
import org.example.microserviciomonopatines.dto.ParadaDTO;
import org.example.microserviciomonopatines.model.Parada;
import org.example.microserviciomonopatines.repository.ParadaRepository;
import org.example.microserviciomonopatines.service.ParadaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.microserviciomonopatines.mapper.ParadaMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParadaServiceImpl implements ParadaService {

    private final ParadaRepository repository;
    private final ParadaMapper mapper;
    private final UsuarioClientMonopatines usuarioClient;

    public ParadaServiceImpl(ParadaRepository repository, ParadaMapper mapper, UsuarioClientMonopatines usuarioClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioClient = usuarioClient;
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
    @Transactional(readOnly = false)
    public ParadaDTO crear(ParadaDTO dto,Long idAdmin) {
        if (!usuarioClient.esAdmin(idAdmin)) {
            throw new IllegalArgumentException("El usuario no es administrador");
        }
        Parada parada = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(parada));
    }

    @Override
    @Transactional(readOnly = false)
    public ParadaDTO actualizar(Long id, ParadaDTO dto) {
        Parada existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parada no encontrada con id " + id));

        existente.setNombre(dto.getNombre());
        existente.setLatitud(dto.getLatitud());
        existente.setLongitud(dto.getLongitud());
        existente.setCapacidad(dto.getCapacidad());

        return mapper.toDTO(repository.save(existente));
    }

    @Override
    @Transactional(readOnly = false)
    public void eliminar(Long id, Long idAdmin) {
        if (!usuarioClient.esAdmin(idAdmin)) {
            throw new IllegalArgumentException("El usuario no es administrador");
        }
        repository.deleteById(id);
    }
}
