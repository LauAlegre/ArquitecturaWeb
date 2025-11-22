package org.example.microservicioviajes.repository;

import org.example.microservicioviajes.model.ViajeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViajeRepository extends MongoRepository<ViajeModel, String> {

    // Buscar viajes por usuario
    List<ViajeModel> findByUsuarioId(Long usuarioId);

    // Buscar viajes por cuenta
    List<ViajeModel> findByCuentaId(Long cuentaId);

    // Buscar por monopatín
    List<ViajeModel> findByMonopatinId(Long monopatinId);

    // Entre fechas (para reportes)
    List<ViajeModel> findByFechaInicioBetween(LocalDateTime desde, LocalDateTime hasta);

    // Para saber si un viaje sigue abierto (fechaFin null)
    List<ViajeModel> findByUsuarioIdAndFechaFinIsNull(Long usuarioId);
}
