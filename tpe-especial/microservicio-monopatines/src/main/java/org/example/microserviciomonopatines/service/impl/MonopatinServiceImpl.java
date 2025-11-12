package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.client.UsuarioClientMonopatines;
import org.example.microserviciomonopatines.dto.MonopatinDTO;
import org.example.microserviciomonopatines.dto.MonopatinReporteDTO;
import org.example.microserviciomonopatines.model.EstadoMonopatin;
import org.example.microserviciomonopatines.repository.MonopatinRepository;
import org.example.microserviciomonopatines.service.GeoService;
import org.example.microserviciomonopatines.service.MonopatinService;
import org.springframework.stereotype.Service;
import org.example.microserviciomonopatines.mapper.MonopatinMapper;
import org.example.microserviciomonopatines.model.Monopatin;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonopatinServiceImpl implements MonopatinService {

    private final MonopatinRepository repository;
    private final GeoService geoService;
    private final MonopatinMapper mapper;
    private final UsuarioClientMonopatines usuarioClient;

    public MonopatinServiceImpl(MonopatinRepository repository, GeoService geoService, MonopatinMapper mapper,
            UsuarioClientMonopatines usuarioClient) {
        this.repository = repository;
        this.geoService = geoService;
        this.mapper = mapper;
        this.usuarioClient = usuarioClient;
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
        EstadoMonopatin e = parseEstado(estado);
        int rows = repository.updateEstadoById(id, e);
        if (rows == 0)
            throw new RuntimeException("Monopatín no encontrado con id " + id);
        return buscarPorId(id);
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
        int rows = repository.updateUbicacionById(id, latitud, longitud);
        if (rows == 0)
            throw new RuntimeException("Monopatín no encontrado con id " + id);
        return buscarPorId(id);
    }

    @Override
    public Map<String, Long> obtenerDisponibilidad() {
        long enMantenimiento = repository.countByEstado(EstadoMonopatin.EN_MANTENIMIENTO);
        long dadosDeBaja = repository.countByEstado(EstadoMonopatin.DADO_DE_BAJA);
        long total = repository.count();
        long enOperacion = total - enMantenimiento - dadosDeBaja;

        Map<String, Long> resultado = new HashMap<>();
        resultado.put("enOperacion", enOperacion);
        resultado.put("enMantenimiento", enMantenimiento);
        resultado.put("dadosDeBaja", dadosDeBaja);
        return resultado;
    }

    @Override
    public List<MonopatinDTO> listarCercanos(Double lat, Double lon, Double radio) {
        return repository.findWithinRadius(lat, lon, radio)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MonopatinReporteDTO> generarReporteKm(Boolean incluirPausas, Long usuarioId) {
        if (usuarioId == null) {
            throw new RuntimeException("usuarioId requerido");
        }

        Boolean esAdmin = usuarioClient.esAdmin(usuarioId);
        if (esAdmin == null || !esAdmin) {
            throw new RuntimeException("Usuario no autorizado");
        }

        // si el flag es null usamos la query sin parámetro (comportamiento por defecto
        // del repository)
        if (incluirPausas == null) {
            return repository.generarReporteKm();
        } else {
            return repository.generarReporteKm(incluirPausas);
        }
    }

    @Override
    public MonopatinDTO finalizarViaje(Long id, Double kmRecorridos) {
        var modelo = repository.findById(id)
                .orElseThrow(() -> new java.util.NoSuchElementException("Monopatín no encontrado id=" + id));

        // Sumar kilómetros
        if (kmRecorridos != null) {
            var total = java.util.Optional.ofNullable(modelo.getTotalKm())
                    .orElse(0.0);
            modelo.setTotalKm(total + kmRecorridos);
        }

        // Cambiar estado a DISPONIBLE usando el enum correcto
        modelo.setEstado(EstadoMonopatin.DISPONIBLE);

        var guardado = repository.save(modelo);
        return mapper.toDTO(guardado);
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
