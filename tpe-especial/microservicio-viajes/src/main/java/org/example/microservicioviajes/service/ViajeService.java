package org.example.microservicioviajes.service;

import org.example.microservicioviajes.client.EstadoClientViajes;
import org.example.microservicioviajes.client.FacturaClientViajes;
import org.example.microservicioviajes.client.UsuarioClientViajes;
import org.example.microservicioviajes.dto.*;
import org.example.microservicioviajes.mapper.ViajeMapper;
import org.example.microservicioviajes.model.ViajeModel;
import org.example.microservicioviajes.repository.ViajeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ViajeService {

    private final ViajeRepository viajeRepository;
    private final ViajeMapper viajeMapper;
    private final UsuarioClientViajes usuarioClient;
    private final EstadoClientViajes estadoClient;
    private final FacturaClientViajes facturaClient;

    public ViajeService(
            ViajeRepository viajeRepository,
            ViajeMapper viajeMapper,
            UsuarioClientViajes usuarioClient,
            EstadoClientViajes estadoClient,
            FacturaClientViajes facturaClient) {

        this.viajeRepository = viajeRepository;
        this.viajeMapper = viajeMapper;
        this.usuarioClient = usuarioClient;
        this.estadoClient = estadoClient;
        this.facturaClient = facturaClient;
    }

    // ========================================================
    // 🔹 INICIAR VIAJE
    // ========================================================
    public ViajeDTO iniciarViaje(ViajeDTO dto) {

        if (dto.getMonopatinId() == null)
            throw new IllegalArgumentException("monopatinId requerido");

        estadoClient.cambiarEstado(dto.getMonopatinId(), "EN_USO");

        ViajeModel model = viajeMapper.toEntity(dto);
        model.setFechaInicio(LocalDateTime.now());
        model.setFechaFin(null);
        model.setPausas(List.of());

        ViajeModel guardado = viajeRepository.save(model);

        return viajeMapper.toDTO(guardado);
    }

    // ========================================================
    // 🔹 OBTENER POR ID
    // ========================================================
    public ViajeDTO obtenerPorId(String id) {
        ViajeModel v = viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + id));
        return viajeMapper.toDTO(v);
    }

    // ========================================================
    // 🔹 LISTAR
    // ========================================================
    public Page<ViajeDTO> listar(Pageable pageable) {
        return viajeRepository.findAll(pageable).map(viajeMapper::toDTO);
    }

    // ========================================================
    // 🔹 ACTUALIZAR
    // ========================================================
    public ViajeDTO actualizar(String id, ViajeDTO dto) {
        ViajeModel existente = viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + id));

        existente.setFechaInicio(dto.getFechaInicio());
        existente.setFechaFin(dto.getFechaFin());
        existente.setKmRecorridos(dto.getKmRecorridos());
        existente.setCuentaId(dto.getCuentaId());
        existente.setMonopatinId(dto.getMonopatinId());
        existente.setUsuarioId(dto.getUsuarioId());

        return viajeMapper.toDTO(viajeRepository.save(existente));
    }

    // ========================================================
    // 🔹 ELIMINAR
    // ========================================================
    public void eliminar(String id) {
        if (!viajeRepository.existsById(id)) {
            throw new NoSuchElementException("Viaje no encontrado id=" + id);
        }
        viajeRepository.deleteById(id);
    }

    // ========================================================
    // 🔹 CERRAR VIAJE
    // ========================================================
    public DatosDeFacturacionDTO cerrarViaje(String id, LocalDateTime fechaFin, Double kmRecorridos) {

        ViajeModel v = viajeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + id));

        if (v.getFechaFin() != null)
            throw new IllegalStateException("El viaje ya está cerrado");

        LocalDateTime finReal = (fechaFin != null) ? fechaFin : LocalDateTime.now();
        v.setFechaFin(finReal);
        v.setKmRecorridos(kmRecorridos);

        long minutosTotales = Duration.between(v.getFechaInicio(), finReal).toMinutes();

        long minutosPausa = v.getPausas().stream()
                .mapToLong(p -> {
                    if (p.getFechaInicio() != null && p.getFechaFin() != null)
                        return Duration.between(p.getFechaInicio(), p.getFechaFin()).toMinutes();
                    return 0L;
                })
                .sum();

        viajeRepository.save(v);

        if (v.getMonopatinId() != null) {
            estadoClient.finalizarMonopatin(
                    v.getMonopatinId(),
                    kmRecorridos != null ? kmRecorridos : 0.0,
                    minutosTotales
            );
        }

        DatosDeFacturacionDTO datos = new DatosDeFacturacionDTO(
                v.getCuentaId(),
                v.getId(),
                (int) minutosTotales,
                (int) minutosPausa,
                minutosPausa > 15,
                LocalDate.now()
        );

        facturaClient.generarFactura(datos);

        return datos;
    }

    // ========================================================
    // 🔹 USO POR CUENTA
    // ========================================================
    public UsoDTO usoPorCuenta(Long cuentaId, LocalDate desde, LocalDate hasta) {

        LocalDateTime start = desde.atStartOfDay();
        LocalDateTime end = hasta.plusDays(1).atStartOfDay();

        double totalKm = 0;
        double totalMin = 0;
        int cantidad = 0;

        for (ViajeModel v : viajeRepository.findAll()) {

            if (!cuentaId.equals(v.getCuentaId())) continue;
            if (v.getFechaInicio() == null || v.getFechaFin() == null) continue;

            if (v.getFechaInicio().isBefore(start) ||
                    !v.getFechaInicio().isBefore(end)) continue;

            cantidad++;
            if (v.getKmRecorridos() != null)
                totalKm += v.getKmRecorridos();

            totalMin += Duration.between(v.getFechaInicio(), v.getFechaFin()).toMinutes();
        }

        return new UsoDTO(cuentaId, totalKm, totalMin, cantidad);
    }

    // ========================================================
    // 🔹 USO POR USUARIO (Mongo)
    // ========================================================
    public UsoDTO usoPorUsuario(Long usuarioId, LocalDate desde, LocalDate hasta) {

        LocalDateTime start = desde.atStartOfDay();
        LocalDateTime end = hasta.plusDays(1).atStartOfDay();

        double totalKm = 0;
        double totalMin = 0;
        int cantidad = 0;

        for (ViajeModel v : viajeRepository.findAll()) {

            if (!usuarioId.equals(v.getUsuarioId())) continue;
            if (v.getFechaInicio() == null || v.getFechaFin() == null) continue;

            if (v.getFechaInicio().isBefore(start) ||
                    !v.getFechaInicio().isBefore(end)) continue;

            cantidad++;
            if (v.getKmRecorridos() != null)
                totalKm += v.getKmRecorridos();

            totalMin += Duration.between(v.getFechaInicio(), v.getFechaFin()).toMinutes();
        }

        return new UsoDTO(usuarioId, totalKm, totalMin, cantidad);
    }

    @Transactional(readOnly = true)
    public List<MonopatinViajesCountDTO> monopatinesConMasDeXViajes(int anio,
                                                                    long minViajes) {
        return viajeRepository.findMonopatinesConMasViajesMongo(anio, minViajes);
    }

    @Transactional(readOnly = true)
    public List<UsoUsuarioDTO> usuariosMasActivosPorTipo(LocalDate desde,
                                                         LocalDate hasta,
                                                         String tipoUsuario,
                                                         int limite) {
        if (desde.isAfter(hasta)) {
            throw new IllegalArgumentException("El parámetro 'desde' no puede ser posterior a 'hasta'.");
        }
        List<Long> usuarioIdsDelTipo = usuarioClient.obtenerIdsUsuariosPorTipo(tipoUsuario);
        if (usuarioIdsDelTipo == null || usuarioIdsDelTipo.isEmpty()) {
            return List.of();
        }
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = hasta.plusDays(1).atStartOfDay();
        List<UsoUsuarioDTO> lista =
                viajeRepository.findUsoUsuariosPeriodoPorIds(inicio, fin, usuarioIdsDelTipo);
        if (limite > 0 && lista.size() > limite) {
            return lista.subList(0, limite);
        }
        return lista;
    }
}
