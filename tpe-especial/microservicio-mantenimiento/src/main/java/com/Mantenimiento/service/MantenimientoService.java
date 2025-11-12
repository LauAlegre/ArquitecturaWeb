package com.Mantenimiento.service;

import com.Mantenimiento.dto.MantenimientoDTO;
import com.Mantenimiento.mapper.MantenimientoMapper;
import com.Mantenimiento.model.Mantenimiento;
import com.Mantenimiento.repository.MantenimientoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MantenimientoService {

    private final MantenimientoRepository repo;
    private final MantenimientoMapper mapper;
    private final RestTemplate rest;

    public MantenimientoService(MantenimientoRepository repo,
                                MantenimientoMapper mapper,
                                RestTemplate restTemplate) {
        this.repo = repo;
        this.mapper = mapper;
        this.rest = restTemplate;
    }

    // ===== CRUD =====

    public List<MantenimientoDTO> listar() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public MantenimientoDTO buscarPorId(Long id) {
        Mantenimiento m = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mantenimiento no encontrado"));
        return mapper.toDTO(m);
    }

    public MantenimientoDTO actualizar(Long id, MantenimientoDTO dto) {
        Mantenimiento existente = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mantenimiento no encontrado"));

        existente.setDescripcion(dto.getDescripcion());
        existente.setResponsable(dto.getResponsable());
        existente.setCosto(dto.getCosto());
        existente.setObservaciones(dto.getObservaciones());

        return mapper.toDTO(repo.save(existente));
    }

    // ===== Lógica de dominio =====

    /** Inicia un mantenimiento y pone el monopatín EN_MANTENIMIENTO en el otro micro */
    public MantenimientoDTO iniciar(Long monopatinId, String descripcion, String responsable, Double costo) {
        if (repo.existsByMonopatinIdAndFinIsNull(monopatinId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un mantenimiento activo para ese monopatín");
        }

        // Aviso al micro de Monopatines
        rest.put("/monopatines/{id}/estado?valor=EN_MANTENIMIENTO", null, monopatinId);

        Mantenimiento m = new Mantenimiento();
        m.setMonopatinId(monopatinId);
        m.setInicio(LocalDateTime.now());
        m.setDescripcion(descripcion);
        m.setResponsable(responsable);
        m.setCosto(costo);

        return mapper.toDTO(repo.save(m));
    }

    /** Finaliza mantenimiento y vuelve el monopatín a EN_PARADA (opcional: paradaId) */
    public MantenimientoDTO finalizar(Long mantenimientoId, String observaciones, Long paradaId) {
        Mantenimiento m = repo.findById(mantenimientoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mantenimiento no encontrado"));

        if (m.getFin() != null) {
            // idempotencia
            return mapper.toDTO(m);
        }

        m.setFin(LocalDateTime.now());
        m.setObservaciones(observaciones);

        if (paradaId == null) {
            rest.put("/monopatines/{id}/estado?valor=EN_PARADA", null, m.getMonopatinId());
        } else {
            rest.put("/monopatines/{id}/estado?valor=EN_PARADA&paradaId={paradaId}",
                    null, m.getMonopatinId(), paradaId);
        }

        return mapper.toDTO(repo.save(m));
    }

    public List<MantenimientoDTO> activos() {
        return repo.findByFinIsNull()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
