package com.Mantenimiento.service;


import com.Mantenimiento.cliente.MonopatinesClient;
import com.Mantenimiento.dto.MantenimientoDTO;
import com.Mantenimiento.mapper.MantenimientoMapper;
import com.Mantenimiento.model.Mantenimiento;
import com.Mantenimiento.repository.MantenimientoRepository;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class MantenimientoService {

    private final MantenimientoRepository repo;
    private final MantenimientoMapper mapper;
    private final MonopatinesClient monopatinesClient;

    public MantenimientoService(MantenimientoRepository repo,
                                MantenimientoMapper mapper,
                                MonopatinesClient monopatinesClient) {
        this.repo = repo;
        this.mapper = mapper;
        this.monopatinesClient = monopatinesClient;
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

    public void eliminar(Long id) {
        Mantenimiento m = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mantenimiento no encontrado"));

        if (m.getFin() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No se puede eliminar un mantenimiento activo");
        }

        repo.deleteById(id);
    }


    public MantenimientoDTO iniciar(Long monopatinId, String descripcion, String responsable, Double costo) {
        if (repo.existsByMonopatinIdAndFinIsNull(monopatinId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un mantenimiento activo para ese monopatín");
        }

        // Cambiar estado a EN_MANTENIMIENTO
        monopatinesClient.cambiarEstado(monopatinId, "EN_MANTENIMIENTO");

        Mantenimiento m = new Mantenimiento();
        m.setMonopatinId(monopatinId);
        m.setInicio(LocalDateTime.now());
        m.setDescripcion(descripcion);
        m.setResponsable(responsable);
        m.setCosto(costo);

        return mapper.toDTO(repo.save(m));
    }



    public MantenimientoDTO finalizar(Long mantenimientoId, String observaciones) {
        Mantenimiento m = repo.findById(mantenimientoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mantenimiento no encontrado"));

        // Si ya está finalizado → idempotente, no haga nada
        if (m.getFin() != null) {
            return mapper.toDTO(m);
        }


        m.setFin(LocalDateTime.now());
        m.setObservaciones(observaciones);

        // Volver a estado EN_PARADA (con paradaId opcional)
        monopatinesClient.cambiarEstado(m.getMonopatinId(), "DISPONIBLE");

        return mapper.toDTO(repo.save(m));
    }


    public List<MantenimientoDTO> activos() {
        return repo.findByFinIsNull()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

}
