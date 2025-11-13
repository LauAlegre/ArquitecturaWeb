package org.example.microservicioviajes.service;

import org.example.microservicioviajes.dto.PausaDTO;
import org.example.microservicioviajes.mapper.PausaMapper;
import org.example.microservicioviajes.model.PausaModel;
import org.example.microservicioviajes.model.ViajeModel;
import org.example.microservicioviajes.repository.PausaRepository;
import org.example.microservicioviajes.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PausaService {

    private final PausaRepository pausaRepository;
    private final ViajeRepository viajeRepository;
    private final PausaMapper pausaMapper;

    // -------------------------------
    // 🔹 Crear pausa
    // -------------------------------
    public PausaDTO crear(Long viajeId) {
        ViajeModel viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + viajeId));

        if (pausaRepository.existsByViajeIdAndFechaFinIsNull(viajeId)) {
            throw new IllegalStateException("Ya existe una pausa abierta para el viaje " + viajeId);
        }

        PausaModel p = new PausaModel();
        p.setFechaInicio(LocalDateTime.now());
        p.setViaje(viaje);

        PausaModel saved = pausaRepository.save(p);
        return pausaMapper.toDTO(saved);
    }

    // -------------------------------
    // 🔹 Obtener pausa por ID
    // -------------------------------
    @Transactional(readOnly = true)
    public PausaDTO obtenerPorId(Long pausaId) {
        return pausaRepository.findById(pausaId)
                .map(pausaMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + pausaId));
    }

    // -------------------------------
    // 🔹 Listar pausas de un viaje
    // -------------------------------
    @Transactional(readOnly = true)
    public List<PausaDTO> listarPorViaje(Long viajeId) {
        return pausaRepository.findByViajeId(viajeId)
                .stream()
                .map(pausaMapper::toDTO)
                .collect(Collectors.toList());
    }

    // -------------------------------
    // 🔹 Obtener la pausa abierta
    // -------------------------------
    @Transactional(readOnly = true)
    public PausaDTO pausaAbierta(Long viajeId) {
        return pausaRepository
                .findFirstByViajeIdAndFechaFinIsNullOrderByFechaInicioDesc(viajeId)
                .map(pausaMapper::toDTO)
                .orElse(null);
    }

    // -------------------------------
    // 🔹 Cerrar pausa
    // -------------------------------
    public PausaDTO cerrar(Long pausaId) {
        PausaModel pausa = pausaRepository.findById(pausaId)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + pausaId));

        if (pausa.getFechaFin() != null)
            throw new IllegalStateException("La pausa ya está cerrada");

        pausa.setFechaFin(LocalDateTime.now());

        long minutos = Duration.between(pausa.getFechaInicio(), pausa.getFechaFin()).toMinutes();
        pausa.setDuracionMinutos((int) Math.max(0, minutos));

        PausaModel saved = pausaRepository.save(pausa);
        return pausaMapper.toDTO(saved);
    }

    // -------------------------------
    // 🔹 Actualizar pausa
    // -------------------------------
    public PausaDTO actualizar(Long pausaId, PausaDTO dto) {
        PausaModel pausa = pausaRepository.findById(pausaId)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + pausaId));

        // Reasociar viaje
        if (dto.getViajeId() != null &&
                (pausa.getViaje() == null ||
                        !dto.getViajeId().equals(pausa.getViaje().getId()))) {

            ViajeModel viaje = viajeRepository.findById(dto.getViajeId())
                    .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + dto.getViajeId()));
            pausa.setViaje(viaje);
        }

        // Actualizar fechas
        if (dto.getFechaInicio() != null) pausa.setFechaInicio(dto.getFechaInicio());
        if (dto.getFechaFin() != null) pausa.setFechaFin(dto.getFechaFin());

        // Actualizar duración
        if (pausa.getFechaInicio() != null && pausa.getFechaFin() != null) {
            long mins = Duration.between(pausa.getFechaInicio(), pausa.getFechaFin()).toMinutes();
            pausa.setDuracionMinutos((int) Math.max(0, mins));
        } else if (dto.getDuracionMinutos() != null) {
            pausa.setDuracionMinutos(dto.getDuracionMinutos());
        }

        PausaModel saved = pausaRepository.save(pausa);
        return pausaMapper.toDTO(saved);
    }

    // -------------------------------
    // 🔹 Eliminar pausa  (FALTABA)
    // -------------------------------
    public void eliminar(Long pausaId) {
        if (!pausaRepository.existsById(pausaId)) {
            throw new NoSuchElementException("Pausa no encontrada id=" + pausaId);
        }
        pausaRepository.deleteById(pausaId);
    }
}
