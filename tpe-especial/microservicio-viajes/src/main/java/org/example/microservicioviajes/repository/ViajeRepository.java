package org.example.microservicioviajes.repository;

import org.example.microservicioviajes.dto.UsoUsuarioDTO;
import org.example.microservicioviajes.model.ViajeModel;
import org.example.microservicioviajes.dto.MonopatinViajesCountDTO;
import org.springframework.data.mongodb.repository.Aggregation;
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

    @Aggregation(pipeline = {
            "{ $match: { $expr: { $eq: [ { $year: '$fechaInicio' }, ?0 ] } } }",
            "{ $group: { _id: '$monopatinId', cantidad: { $sum: 1 } } }",
            "{ $match: { cantidad: { $gt: ?1 } } }",
            "{ $project: { monopatinId: '$_id', cantidad: 1, _id: 0 } }",
            "{ $sort: { cantidad: -1 } }"
    })
    List<MonopatinViajesCountDTO> findMonopatinesConMasViajesMongo(int anio, long minViajes);

    @Aggregation(pipeline = {
            // Filtrar por período (fechaInicio entre inicio y fin) y por lista de usuarios
            "{ $match: { " +
                    "  fechaInicio: { $gte: ?0, $lt: ?1 }, " +
                    "  usuarioId: { $in: ?2 } " +
                    "} }",
            // Agrupamos por usuarioId
            "{ $group: { " +
                    "  _id: '$usuarioId', " +
                    "  cantidadViajes: { $sum: 1 }, " +
                    "  kmTotales: { $sum: { $ifNull: ['$kmRecorridos', 0] } }" +
                    // Si tenés un campo duracionMinutos, podés sumar así:
                    // "  minutosTotales: { $sum: { $ifNull: ['$duracionMinutos', 0] } }" +
                    "} }",
            // Ordenamos por cantidad de viajes desc
            "{ $sort: { cantidadViajes: -1 } }",
            // Proyectamos al DTO
            "{ $project: { " +
                    "  _id: 0, " +
                    "  usuarioId: '$_id', " +
                    "  cantidadViajes: 1, " +
                    "  kmTotales: 1" +
                    // Acordate de agregar minutosTotales si lo calculaste en el $group
                    "} }"
    })
    List<UsoUsuarioDTO> findUsoUsuariosPeriodoPorIds(LocalDateTime inicio,
                                                     LocalDateTime fin,
                                                     List<Long> usuarioIds);
}