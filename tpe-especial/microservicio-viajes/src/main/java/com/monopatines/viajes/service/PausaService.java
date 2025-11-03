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
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PausaService {
    private final PausaRepository pausaRepository;
    private final ViajeRepository viajeRepository;
    private final PausaMapper pausaMapper; // suponiendo @Component

    /* ======================
       ABM (CRUD)
       ====================== */

    /** Crea una pausa para un viaje. */
    public PausaDTO crear(PausaDTO dto) {
        // Validar viaje
        Long viajeId = (long) dto.getId_viaje();
        ViajeModel viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + viajeId));

        // Regla: no permitir dos pausas abiertas a la vez
        if (existePausaAbierta(viajeId)) {
            throw new IllegalStateException("Ya existe una pausa abierta para el viaje " + viajeId);
        }

        PausaModel entity = pausaMapper.toEntity(dto);
        entity.setViaje(viaje);
        PausaModel saved = pausaRepository.save(entity);
        PausaDTO res = pausaMapper.toDTO(saved);
        res.setId_viaje(saved.getViaje().getIdViaje().intValue());
        return res;
    }

    @Transactional(readOnly = true)
    public PausaDTO obtenerPorId(Long id) {
        PausaModel p = pausaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + id));
        return pausaMapper.toDTO(p);
    }

    @Transactional(readOnly = true)
    public List<PausaDTO> listarPorViaje(Long viajeId) {
        // Requiere método en repo: findByViajeId(Long)
        List<PausaModel> pausas = pausaRepository.findByViajeId(viajeId);
        return pausas.stream().map(pausaMapper::toDTO).collect(Collectors.toList());
    }

    public PausaDTO actualizar(Long id, PausaDTO dto) {
        PausaModel existente = pausaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + id));

        existente.setFechaInicio(dto.getFecha_inicio());
        existente.setFechaFin(dto.getFecha_fin());
        existente.setDuracionMinutos(dto.getDuracion_minutos());

        return pausaMapper.toDTO(pausaRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!pausaRepository.existsById(id)) {
            throw new NoSuchElementException("Pausa no encontrada id=" + id);
        }
        pausaRepository.deleteById(id);
    }

    /* ======================
       Reglas/operaciones de negocio
       ====================== */

    /** Cierra una pausa, calcula duración (minutos) y guarda. */
    public PausaDTO cerrarPausa(Long pausaId, LocalTime fechaFin) {
        PausaModel pausa = pausaRepository.findById(pausaId)
                .orElseThrow(() -> new NoSuchElementException("Pausa no encontrada id=" + pausaId));

        if (pausa.getFechaFin() != null) {
            throw new IllegalStateException("La pausa ya está cerrada");
        }
        pausa.setFechaFin(fechaFin);

        // cálculo simple con LocalTime del mismo día; si manejás días distintos, adaptá a LocalDateTime
        long mins = Duration.between(pausa.getFechaInicio(), pausa.getFechaFin()).toMinutes();
        pausa.setDuracionMinutos((int) Math.max(mins, 0));

        return pausaMapper.toDTO(pausaRepository.save(pausa));
    }

    /** ¿Existe alguna pausa abierta (sin fecha_fin) para el viaje? */
    @Transactional(readOnly = true)
    public boolean existePausaAbierta(Long viajeId) {
        return pausaRepository.existsByViajeIdAndFechaFinIsNull(viajeId);
    }

    /** Devuelve la última pausa abierta del viaje (si hay). */
    @Transactional(readOnly = true)
    public PausaDTO ultimaPausaAbierta(Long viajeId) {
        return pausaRepository
                .findFirstByViajeIdAndFechaFinIsNullOrderByFechaInicioDesc(viajeId)
                .map(pausaMapper::toDTO)
                .orElse(null);
    }
}
