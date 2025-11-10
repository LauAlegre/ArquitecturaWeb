package com.monopatines.viajes.service;

import com.monopatines.viajes.dto.PausaDTO;
import com.monopatines.viajes.mapper.PausaMapper;
import com.monopatines.viajes.model.PausaModel;
import com.monopatines.viajes.model.ViajeModel;
import com.monopatines.viajes.repository.PausaRepository;
import com.monopatines.viajes.repository.ViajeRepository;
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

    @Transactional(readOnly = true)
    public PausaDTO obtenerPorId(Long id) {
        return pausaRepository.findById(id).map(pausaMapper::toDTO)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + id));
    }

    @Transactional(readOnly = true)
    public List<PausaDTO> listarPorViaje(Long viajeId) {
        return pausaRepository.findByViajeId(viajeId).stream().map(pausaMapper::toDTO).collect(Collectors.toList());
    }

    public PausaDTO cerrar(Long pausaId) {
        PausaModel pausa = pausaRepository.findById(pausaId)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + pausaId));
        if (pausa.getFechaFin() != null) throw new IllegalStateException("La pausa ya está cerrada");
        pausa.setFechaFin(LocalDateTime.now());
        long mins = Duration.between(pausa.getFechaInicio(), pausa.getFechaFin()).toMinutes();
        pausa.setDuracionMinutos((int) Math.max(mins, 0));
        return pausaMapper.toDTO(pausaRepository.save(pausa));
    }

    @Transactional(readOnly = true)
    public PausaDTO pausaAbierta(Long viajeId) {
        return pausaRepository.findFirstByViajeIdAndFechaFinIsNullOrderByFechaInicioDesc(viajeId)
                .map(pausaMapper::toDTO)
                .orElse(null);
    }

    public PausaDTO actualizar(Long pausaId, PausaDTO dto) {
        PausaModel pausa = pausaRepository.findById(pausaId)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + pausaId));

        // Opcional: re-asociar a otro viaje si se envía un viajeId diferente
        if (dto.getViajeId() != null && (pausa.getViaje() == null || !dto.getViajeId().equals(pausa.getViaje().getId()))) {
            ViajeModel viaje = viajeRepository.findById(dto.getViajeId())
                    .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + dto.getViajeId()));
            pausa.setViaje(viaje);
        }

        if (dto.getFechaInicio() != null) {
            pausa.setFechaInicio(dto.getFechaInicio());
        }
        if (dto.getFechaFin() != null) {
            pausa.setFechaFin(dto.getFechaFin());
        }

        // Si hay ambas fechas, recalcular duración; si no, permitir setear manualmente
        if (pausa.getFechaInicio() != null && pausa.getFechaFin() != null) {
            long mins = Duration.between(pausa.getFechaInicio(), pausa.getFechaFin()).toMinutes();
            pausa.setDuracionMinutos((int) Math.max(mins, 0));
        } else if (dto.getDuracionMinutos() != null) {
            pausa.setDuracionMinutos(dto.getDuracionMinutos());
        }

        PausaModel saved = pausaRepository.save(pausa);
        return pausaMapper.toDTO(saved);
    }
}