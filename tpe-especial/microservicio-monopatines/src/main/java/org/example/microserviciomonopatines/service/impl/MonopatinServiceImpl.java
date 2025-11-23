package org.example.microserviciomonopatines.service.impl;

import org.example.microserviciomonopatines.client.PausaClientMonopatines;
import org.example.microserviciomonopatines.dto.MonopatinDTO;
import org.example.microserviciomonopatines.dto.MonopatinReporteDTO;
import org.example.microserviciomonopatines.dto.PausaMonopatinDTO;
import org.example.microserviciomonopatines.model.EstadoMonopatin;
import org.example.microserviciomonopatines.repository.MonopatinRepository;
import org.example.microserviciomonopatines.service.GeoService;
import org.example.microserviciomonopatines.service.MonopatinService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.microserviciomonopatines.mapper.MonopatinMapper;
import org.example.microserviciomonopatines.model.Monopatin;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)   // 🔹 Por defecto, todo es solo lectura
public class MonopatinServiceImpl implements MonopatinService {

    private final MonopatinRepository repository;
    private final GeoService geoService;
    private final MonopatinMapper mapper;
    private final PausaClientMonopatines pausaClient;

    public MonopatinServiceImpl(MonopatinRepository repository, GeoService geoService,
                                MonopatinMapper mapper, PausaClientMonopatines pausaClient) {
        this.pausaClient = pausaClient;
        this.repository = repository;
        this.geoService = geoService;
        this.mapper = mapper;
    }

    // ------------------------------ LECTURAS ---------------------------------

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
    public List<MonopatinReporteDTO> generarReporteKm(Boolean incluirPausas) {
        if(incluirPausas)
        {
            List<MonopatinReporteDTO> reporteKm = repository.generarReporteKm();
            for(MonopatinReporteDTO a : reporteKm )
            {
                PausaMonopatinDTO minutosPausa = pausaClient.obtenerPausasDeMonopatin(a.getId());
                a.setTotalTiempoUso(a.getTotalTiempoUso() + minutosPausa.getTotalMinutosPausa());

            }
            return  reporteKm;
        }
        return  repository.generarReporteKm();
    }

    // ------------------------------ ESCRITURAS ---------------------------------

    @Override
    @Transactional(readOnly = false)
    public MonopatinDTO crear(MonopatinDTO dto) {
        Monopatin monopatin = mapper.toEntity(dto);
        Monopatin guardado = repository.save(monopatin);
        return mapper.toDTO(guardado);
    }

    @Override
    @Transactional(readOnly = false)
    public MonopatinDTO actualizar(Long id, MonopatinDTO dto) {
        Monopatin existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));

        existente.setEstado(dto.getEstado());
        existente.setLatitud(dto.getLatitud());
        existente.setLongitud(dto.getLongitud());
        existente.setTotalKm(dto.getTotalKm());
        existente.setTotalTiempoUso(dto.getTotalTiempoUso());
        existente.setParadaId(dto.getParadaId());

        return mapper.toDTO(repository.save(existente));
    }

    @Override
    @Transactional(readOnly = false)
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public MonopatinDTO cambiarEstado(Long id, String estado) {
        EstadoMonopatin e = parseEstado(estado);
        int rows = repository.updateEstadoById(id, e);
        if (rows == 0)
            throw new RuntimeException("Monopatín no encontrado con id " + id);
        return buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = false)
    public MonopatinDTO darDeBaja(Long id) {
        Monopatin mono = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));
        mono.setEstado(EstadoMonopatin.DADO_DE_BAJA);
        return mapper.toDTO(repository.save(mono));
    }

    @Override
    @Transactional(readOnly = false)
    public MonopatinDTO reactivar(Long id) {
        Monopatin mono = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Monopatín no encontrado con id " + id));
        mono.setEstado(EstadoMonopatin.DISPONIBLE);
        return mapper.toDTO(repository.save(mono));
    }

    @Override
    @Transactional(readOnly = false)
    public MonopatinDTO actualizarUbicacion(Long id, Double latitud, Double longitud) {
        int rows = repository.updateUbicacionById(id, latitud, longitud);
        if (rows == 0)
            throw new RuntimeException("Monopatín no encontrado con id " + id);
        return buscarPorId(id);
    }

    @Override
    @Transactional(readOnly = false)
    public MonopatinDTO finalizarViaje(Long id, Double kmRecorridos, Long minutosTotales) {
        Monopatin mono = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Monopatín no encontrado id=" + id));

        // --- SUMAR KM ---
        if (kmRecorridos != null) {
            double totalKm = Optional.ofNullable(mono.getTotalKm()).orElse(0.0);
            mono.setTotalKm(totalKm + kmRecorridos);
        }

        // --- SUMAR MINUTOS ---
        if (minutosTotales != null) {
            long totalMin = Optional.ofNullable(mono.getTotalTiempoUso()).orElse(0L);
            mono.setTotalTiempoUso(totalMin + minutosTotales);
        }

        // Cambiar estado
        mono.setEstado(EstadoMonopatin.DISPONIBLE);

        return mapper.toDTO(repository.save(mono));
    }


    private EstadoMonopatin parseEstado(String raw) {
        try {
            return EstadoMonopatin.valueOf(raw.trim().toUpperCase());
        } catch (Exception ex) {
            throw new RuntimeException("Estado inválido: " + raw);
        }
    }
}
