package org.example.microservicioviajes.service;

import org.example.microservicioviajes.client.UsuarioClientViajes;
import org.example.microservicioviajes.dto.ViajeDTO;
import org.example.microservicioviajes.dto.UsoDTO;
import org.example.microservicioviajes.mapper.ViajeMapper;
import org.example.microservicioviajes.model.ViajeModel;
import org.example.microservicioviajes.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ViajeService {
    private final ViajeRepository viajeRepository;
    private final ViajeMapper viajeMapper;
    private final UsuarioClientViajes usuarioClient;

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
        existente.setFechaInicio(dto.getFechaInicio());
        existente.setFechaFin(dto.getFechaFin());
        existente.setKmRecorridos(dto.getKmRecorridos());
        existente.setCuentaId(dto.getCuentaId());
        existente.setMonopatinId(dto.getMonopatinId());
        existente.setUsuarioId(dto.getUsuarioId());

        return viajeMapper.toDTO(viajeRepository.save(existente));
    }

    public void eliminar(Long id) {
        if (!viajeRepository.existsById(id)) {
            throw new NoSuchElementException("Viaje no encontrado id=" + id);
        }
        viajeRepository.deleteById(id);
    }

    /*
     * ======================
     * Reglas/operaciones de negocio
     * ======================
     */

    /** Cierra un viaje (setea fecha_fin y km_recorridos). */
    public ViajeDTO cerrarViaje(Long id, LocalDateTime fechaFin, BigDecimal kmRecorridos) {
        ViajeModel v = viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + id));
        if (v.getFechaFin() != null) {
            throw new IllegalStateException("El viaje ya está cerrado");
        }
        v.setFechaFin(fechaFin);
        v.setKmRecorridos(kmRecorridos);
        return viajeMapper.toDTO(viajeRepository.save(v));
    }

    /**
     * Reporte: monopatines con más de X viajes en un año (requiere usuario admin).
     */
    @Transactional(readOnly = true)
    public List<ViajeRepository.MonopatinViajesCount> monopatinesConMasDeXViajes(int anio, long minViajes,
            Long usuarioAdminId) {
        // validar admin
        if (usuarioAdminId == null || !usuarioClient.esAdmin(usuarioAdminId)) {
            throw new SecurityException("Acceso denegado: se requiere usuario admin.");
        }
        return viajeRepository.findMonopatinesConMasDeXViajesEnAnio(anio, minViajes);
    }

    // Nuevo: uso por cuenta con período
    @Transactional(readOnly = true)
    public UsoDTO usoPorCuenta(Long cuentaId, LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("El parámetro 'desde' no puede ser posterior a 'hasta'.");
        }
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime finExclusivo = hasta.plusDays(1).atStartOfDay();

        double kmTotales = 0d;
        double minutosTotales = 0d;
        int cantidad = 0;

        for (ViajeModel v : viajeRepository.findAll()) {
            if (!Objects.equals(v.getCuentaId(), cuentaId))
                continue;
            if (v.getFechaInicio() == null || v.getFechaFin() == null)
                continue; // solo cerrados
            if (v.getFechaInicio().isBefore(inicio) || !v.getFechaInicio().isBefore(finExclusivo))
                continue;

            cantidad++;
            if (v.getKmRecorridos() != null)
                kmTotales += v.getKmRecorridos().doubleValue();
            minutosTotales += Duration.between(v.getFechaInicio(), v.getFechaFin()).toMinutes();
        }
        return new UsoDTO(cuentaId, kmTotales, minutosTotales, cantidad);
    }

    @Transactional(readOnly = true)
    public UsoDTO usoPorUsuario(Long usuarioId, LocalDate desde, LocalDate hasta) {
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("El parámetro 'desde' no puede ser posterior a 'hasta'.");
        }
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime finExclusivo = hasta.plusDays(1).atStartOfDay();

        double kmTotales = 0d;
        double minutosTotales = 0d;
        int cantidad = 0;

        for (ViajeModel v : viajeRepository.findAll()) {
            if (!Objects.equals(v.getUsuarioId(), usuarioId))
                continue;
            if (v.getFechaInicio() == null || v.getFechaFin() == null)
                continue; // solo cerrados
            if (v.getFechaInicio().isBefore(inicio) || !v.getFechaInicio().isBefore(finExclusivo))
                continue;

            cantidad++;
            if (v.getKmRecorridos() != null)
                kmTotales += v.getKmRecorridos().doubleValue();
            minutosTotales += Duration.between(v.getFechaInicio(), v.getFechaFin()).toMinutes();
        }
        return new UsoDTO(usuarioId, kmTotales, minutosTotales, cantidad);
    }

    /*
     * Ranking de usuarios por uso filtrado por período y tipo de usuario.
     * Ahora: obtiene los usuarioIds del microservicio de usuarios y valida que
     * quien solicita es admin.
     */
    @Transactional(readOnly = true)
    public List<ViajeRepository.UsoUsuario> usuariosMasActivosPorTipo(LocalDate desde,
            LocalDate hasta,
            String tipoUsuario,
            Long usuarioAdminId,
            int limite) {
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("El parámetro 'desde' no puede ser posterior a 'hasta'.");
        }

        // Validar admin
        if (usuarioAdminId == null || !usuarioClient.esAdmin(usuarioAdminId)) {
            throw new SecurityException("Acceso denegado: se requiere usuario admin.");
        }

        // Obtener ids de usuarios del tipo desde el microservicio de usuarios
        List<Long> usuarioIdsDelTipo = usuarioClient.obtenerIdsUsuariosPorTipo(tipoUsuario);
        if (usuarioIdsDelTipo == null || usuarioIdsDelTipo.isEmpty()) {
            return List.of(); // No hay usuarios del tipo => no hay ranking
        }

        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.plusDays(1).atStartOfDay();

        List<ViajeRepository.UsoUsuario> lista = viajeRepository.findUsoUsuariosPeriodoPorIds(inicio, fin,
                usuarioIdsDelTipo);

        if (limite > 0 && lista.size() > limite) {
            return lista.subList(0, limite);
        }
        return lista;
    }
}