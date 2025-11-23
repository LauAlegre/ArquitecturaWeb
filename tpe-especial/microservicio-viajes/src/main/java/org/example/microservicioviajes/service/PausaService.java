package org.example.microservicioviajes.service;

import org.example.microservicioviajes.dto.MinutosPausaMonopatinDTO;
import org.example.microservicioviajes.dto.PausaDTO;
import org.example.microservicioviajes.mapper.PausaMapper;
import org.example.microservicioviajes.model.PausaModel;
import org.example.microservicioviajes.model.ViajeModel;
import org.example.microservicioviajes.repository.ViajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PausaService {

    private final ViajeRepository viajeRepository;
    private final PausaMapper pausaMapper;

    // ------------------------------------
    // 🔹 Crear pausa (embebida en viaje)
    // ------------------------------------
    public PausaDTO crear(String viajeId) {

        ViajeModel viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + viajeId));

        // verificar pausa abierta
        boolean existeAbierta = viaje.getPausas().stream()
                .anyMatch(p -> p.getFechaFin() == null);

        if (existeAbierta) {
            throw new IllegalStateException("Ya existe una pausa abierta para el viaje " + viajeId);
        }

        PausaModel pausa = new PausaModel();
        pausa.setFechaInicio(LocalDateTime.now());

        viaje.getPausas().add(pausa);
        viajeRepository.save(viaje);

        return pausaMapper.toDTO(pausa);
    }

    public MinutosPausaMonopatinDTO obtenerMinutosPausaPorMonopatin(Long monopatinId) {
        System.out.println("inicio");
        // Traigo todos los viajes de ese monopatín
        List<ViajeModel> viajes = viajeRepository.findByMonopatinId(monopatinId);

        long totalMinutos = 0L;
        int cantidadPausas = 0;

        for (ViajeModel viaje : viajes) {
            if (viaje.getPausas() == null) {
                continue;
            }

            for (PausaModel pausa : viaje.getPausas()) {
                if (pausa != null && pausa.getDuracionMinutos() != null) {
                    totalMinutos += pausa.getDuracionMinutos();
                    cantidadPausas++;
                }
            }
        }
        System.out.println("fin");
        return new MinutosPausaMonopatinDTO(
                monopatinId,
                totalMinutos

        );
    }



    // ------------------------------------
    // 🔹 Obtener pausa abierta
    // ------------------------------------
    public PausaDTO pausaAbierta(String viajeId) {
        ViajeModel viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + viajeId));

        return viaje.getPausas().stream()
                .filter(p -> p.getFechaFin() == null)
                .findFirst()
                .map(pausaMapper::toDTO)
                .orElse(null);
    }

    // ------------------------------------
    // 🔹 Cerrar pausa
    // ------------------------------------
    public PausaDTO cerrar(String viajeId) {

        ViajeModel viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + viajeId));

        PausaModel pausa = viaje.getPausas().stream()
                .filter(p -> p.getFechaFin() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No hay pausa abierta para este viaje"));

        pausa.setFechaFin(LocalDateTime.now());

        long min = Duration.between(pausa.getFechaInicio(), pausa.getFechaFin()).toMinutes();
        pausa.setDuracionMinutos((int) Math.max(0, min));

        viajeRepository.save(viaje);

        return pausaMapper.toDTO(pausa);
    }

    // ------------------------------------
    // 🔹 Listar pausas
    // ------------------------------------
    public java.util.List<PausaDTO> listarPorViaje(String viajeId) {
        ViajeModel viaje = viajeRepository.findById(viajeId)
                .orElseThrow(() -> new NoSuchElementException("Viaje no encontrado id=" + viajeId));

        return viaje.getPausas().stream()
                .map(pausaMapper::toDTO)
                .toList();
    }
}
