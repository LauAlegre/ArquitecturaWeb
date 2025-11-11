package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.dto.MonopatinDTO;
import org.example.microserviciomonopatines.dto.MonopatinReporteDTO;
import org.example.microserviciomonopatines.model.EstadoMonopatin;
import org.example.microserviciomonopatines.model.Monopatin;
import org.example.microserviciomonopatines.repository.MonopatinRepository;
import org.example.microserviciomonopatines.service.GeoService;
import org.example.microserviciomonopatines.service.MonopatinService;
import org.springframework.stereotype.Service;
import org.example.microserviciomonopatines.mapper.MonopatinMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonopatinServiceImpl implements MonopatinService {

    private final MonopatinRepository repository;
    private final GeoService geoService;
    private final MonopatinMapper mapper;

    public MonopatinServiceImpl(MonopatinRepository repository, GeoService geoService, MonopatinMapper mapper) {
        this.repository = repository;
        this.geoService = geoService;
        this.mapper = mapper;
    }

    @Override
    public List<MonopatinDTO> listar() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MonopatinDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));
    }

    @Override
    public MonopatinDTO crear(MonopatinDTO dto) {
        Monopatin monopatin = mapper.toEntity(dto);
        Monopatin guardado = repository.save(monopatin);
        return mapper.toDTO(guardado);
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
        return mapper.toDTO(actualizado);
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
        return mapper.toDTO(actualizado);
    }

    @Override
    public MonopatinDTO darDeBaja(Long id) {
        Monopatin monopatin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));
        monopatin.setEstado(EstadoMonopatin.DADO_DE_BAJA);
        Monopatin actualizado = repository.save(monopatin);
        return mapper.toDTO(actualizado);
    }

    @Override
    public MonopatinDTO reactivar(Long id) {
        Monopatin monopatin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));
        monopatin.setEstado(EstadoMonopatin.DISPONIBLE);
        Monopatin actualizado = repository.save(monopatin);
        return mapper.toDTO(actualizado);
    }

    @Override
    public MonopatinDTO actualizarUbicacion(Long id, Double latitud, Double longitud) {
        Monopatin monopatin = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));

        monopatin.setLatitud(latitud);
        monopatin.setLongitud(longitud);
        Monopatin actualizado = repository.save(monopatin);
        return mapper.toDTO(actualizado);
    }

    @Override
    public Map<String, Long> obtenerDisponibilidad() {
        List<Monopatin> todos = repository.findAll();

        long enMantenimiento = todos.stream()
                .filter(m -> m.getEstado() == EstadoMonopatin.EN_MANTENIMIENTO) // <- comparar enums
                .count();

        long dadosDeBaja = todos.stream()
                .filter(m -> m.getEstado() == EstadoMonopatin.DADO_DE_BAJA)
                .count();

        long enOperacion = todos.size() - enMantenimiento - dadosDeBaja;

        Map<String, Long> resultado = new HashMap<>();
        resultado.put("enOperacion", enOperacion);
        resultado.put("enMantenimiento", enMantenimiento);
        resultado.put("dadosDeBaja", dadosDeBaja);
        return resultado;
    }

    @Override
    public List<MonopatinDTO> listarCercanos(Double lat, Double lon, Double radio) {
        return repository.findAll()
                .stream()
                .filter(m -> geoService.withinRadius(lat, lon, m.getLatitud(), m.getLongitud(), radio))
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MonopatinReporteDTO> generarReporteKm(boolean incluirPausas) {
        List<Monopatin> monopatines = repository.findAll();

        return monopatines.stream().map(m -> {
            double km = m.getTotalKm() != null ? m.getTotalKm() : 0.0;
            double tiempo = incluirPausas && m.getTotalTiempoUso() != null
                    ? m.getTotalTiempoUso()
                    : 0.0;

            boolean requiereMantenimiento = km >= 1000 || tiempo >= 500;
            // 🔧 criterio de mantenimiento de ejemplo

            return new MonopatinReporteDTO(
                    m.getId(),
                    km,
                    tiempo,
                    requiereMantenimiento);
        }).collect(Collectors.toList());
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
