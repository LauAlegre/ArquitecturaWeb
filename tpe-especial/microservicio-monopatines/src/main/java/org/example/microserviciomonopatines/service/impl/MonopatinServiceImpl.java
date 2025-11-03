package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.dto.MonopatinDTO;
import org.example.microserviciomonopatines.model.EstadoMonopatin;
import org.example.microserviciomonopatines.model.Monopatin;
import org.example.microserviciomonopatines.repository.MonopatinRepository;
import org.example.microserviciomonopatines.service.MonopatinService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonopatinServiceImpl implements MonopatinService {

    private final MonopatinRepository repository;

    public MonopatinServiceImpl(MonopatinRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MonopatinDTO> listar() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MonopatinDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));
    }

    @Override
    public MonopatinDTO crear(MonopatinDTO dto) {
        Monopatin monopatin = toEntity(dto);
        Monopatin guardado = repository.save(monopatin);
        return toDTO(guardado);
    }

    @Override
    public MonopatinDTO actualizar(Long id, MonopatinDTO dto) {
        Monopatin existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));

        existente.setEstado(dto.getEstado());
        existente.setLatitud(dto.getLatitud());
        existente.setLongitud(dto.getLongitud());
        existente.setTotalKm(dto.getTotalKm());
        existente.setTotalTiempoUso(dto.getTotalTiempoUso());
        existente.setParadaId(dto.getParadaId());

        Monopatin actualizado = repository.save(existente);
        return toDTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public MonopatinDTO cambiarEstado(Long id, String estado) {
        Monopatin monopatin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));

        monopatin.setEstado(parseEstado(estado)); // <- enum
        Monopatin actualizado = repository.save(monopatin);
        return toDTO(actualizado);
    }

    @Override
    public MonopatinDTO actualizarUbicacion(Long id, Double latitud, Double longitud) {
        Monopatin monopatin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));

        monopatin.setLatitud(latitud);
        monopatin.setLongitud(longitud);
        Monopatin actualizado = repository.save(monopatin);
        return toDTO(actualizado);
    }

    @Override
    public List<MonopatinDTO> listarCercanos(Double latitud, Double longitud, Double radio) {
        // TODO: implementar lógica con GeoUtils (distancia)
        List<Monopatin> todos = repository.findAll();
        return todos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> obtenerDisponibilidad() {
        List<Monopatin> todos = repository.findAll();

        long enMantenimiento = todos.stream()
                .filter(m -> m.getEstado() == EstadoMonopatin.EN_MANTENIMIENTO) // <- comparar enums
                .count();

        long enOperacion = todos.size() - enMantenimiento;

        Map<String, Long> resultado = new HashMap<>();
        resultado.put("enOperacion", enOperacion);
        resultado.put("enMantenimiento", enMantenimiento);
        return resultado;
    }

    // --- Métodos auxiliares de mapeo ---

    private MonopatinDTO toDTO(Monopatin m) {
        return new MonopatinDTO(
                m.getId(),
                m.getEstado(),
                m.getLatitud(),
                m.getLongitud(),
                m.getTotalKm(),
                m.getTotalTiempoUso(),
                m.getParadaId());
    }

    private Monopatin toEntity(MonopatinDTO dto) {
        Monopatin m = new Monopatin();
        m.setEstado(dto.getEstado());
        m.setLatitud(dto.getLatitud());
        m.setLongitud(dto.getLongitud());
        m.setTotalKm(dto.getTotalKm());
        m.setTotalTiempoUso(dto.getTotalTiempoUso());
        m.setParadaId(dto.getParadaId());
        return m;
    }

    private EstadoMonopatin parseEstado(String raw) {
        if (raw == null)
            return null;
        try {
            return EstadoMonopatin.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Estado inválido: " + raw);
        }
    }
}
