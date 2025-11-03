package com.monopatines.viajes.service;
import com.monopatines.viajes.dto.ViajeDTO;
import com.monopatines.viajes.mapper.ViajeMapper;
import com.monopatines.viajes.model.ViajeModel;
import com.monopatines.viajes.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ViajeService {
    private final ViajeRepository viajeRepository;
    private final ViajeMapper viajeMapper; // suponiendo @Component en el mapper

    /* ======================
       ABM (CRUD)
       ====================== */

    public ViajeDTO crear(ViajeDTO dto) {
        ViajeModel entity = viajeMapper.toEntity(dto);
        ViajeModel saved = viajeRepository.save(entity);
        return viajeMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public ViajeDTO obtenerPorId(Long id) {
        ViajeModel v = viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + id));
        return viajeMapper.toDTO(v);
    }

    @Transactional(readOnly = true)
    public Page<ViajeDTO> listar(Pageable pageable) {
        return viajeRepository.findAll(pageable).map(viajeMapper::toDTO);
    }

    public ViajeDTO actualizar(Long id, ViajeDTO dto) {
        ViajeModel existente = viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + id));

        // campos editables
        existente.setFechaInicio(dto.getFecha_inicio());
        existente.setFechaFin(dto.getFecha_fin());
        existente.setKmRecorridos(dto.getKm_recorridos());
        if (dto.getId_cuenta() != null) {
            existente.setCuentaId(dto.getId_cuenta().getIdCuenta());
        }
        if (dto.getId_monopatin() != null) {
            existente.setMonopatinId(dto.getId_monopatin().getIdMonopatin());
        }

        return viajeMapper.toDTO(viajeRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!viajeRepository.existsById(id)) {
            throw new NoSuchElementException("Viaje no encontrado id=" + id);
        }
        viajeRepository.deleteById(id);
    }

    /* ======================
       Reglas/operaciones de negocio
       ====================== */

    /** Cierra un viaje (setea fecha_fin y km_recorridos). */
    public ViajeDTO cerrarViaje(Long id, LocalTime fechaFin, double kmRecorridos) {
        ViajeModel v = viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + id));
        if (v.getFechaFin() != null) {
            throw new IllegalStateException("El viaje ya está cerrado");
        }
        v.setFechaFin(fechaFin);
        v.setKmRecorridos(kmRecorridos);
        return viajeMapper.toDTO(viajeRepository.save(v));
    }

    /** Reporte: monopatines con más de X viajes en un año. */
    @Transactional(readOnly = true)
    public List<ViajeRepository.MonopatinViajesCount> monopatinesConMasDeXViajes(int anio, long minViajes) {
        return viajeRepository.findMonopatinesConMasDeXViajesEnAnio(anio, minViajes);
    }
}
