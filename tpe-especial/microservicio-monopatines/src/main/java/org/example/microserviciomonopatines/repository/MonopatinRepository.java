package org.example.microserviciomonopatines.repository;

import org.example.microserviciomonopatines.dto.MonopatinReporteDTO;
import org.example.microserviciomonopatines.model.EstadoMonopatin;
import org.example.microserviciomonopatines.model.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MonopatinRepository extends JpaRepository<Monopatin, Long> {
    long countByEstado(EstadoMonopatin estado);

    // búsqueda por radio (native Haversine — ajustar nombres de columnas/tabla)
    @Query(value = "SELECT * " +
            "FROM monopatin m " +
            "WHERE (6371 * acos( " +
            "    cos(radians(:lat)) * cos(radians(m.latitud)) * cos(radians(m.longitud) - radians(:lon)) + " +
            "    sin(radians(:lat)) * sin(radians(m.latitud)) " +
            ")) <= :radio", nativeQuery = true)
    List<Monopatin> findWithinRadius(@Param("lat") double lat, @Param("lon") double lon, @Param("radio") double radio);

    // Proyección/DTO para reporte (sin COALESCE ni CASE WHEN)
    @Query("SELECT new org.example.microserviciomonopatines.dto.MonopatinReporteDTO( " +
            "  m.id, " +
            "  (CASE WHEN m.totalKm IS NULL THEN 0.0 ELSE m.totalKm END), " +
            "  (CASE WHEN m.totalTiempoUso IS NULL THEN 0L ELSE m.totalTiempoUso END), " +
            "  (CASE WHEN ( (m.totalKm IS NOT NULL AND m.totalKm >= 1000.0) OR " +
            "               (m.totalTiempoUso IS NOT NULL AND m.totalTiempoUso >= 500L) ) " +
            "        THEN true ELSE false END) " +
            ") FROM Monopatin m")
    List<MonopatinReporteDTO> generarReporteKm();

    @Query("SELECT new org.example.microserviciomonopatines.dto.MonopatinReporteDTO( " +
            "  m.id, " +
            "  (CASE WHEN m.totalKm IS NULL THEN 0.0 ELSE m.totalKm END), " +
            "  (CASE WHEN :incluirPausas = true THEN (CASE WHEN m.totalTiempoUso IS NULL THEN 0L ELSE m.totalTiempoUso END) ELSE 0L END), "
            +
            "  (CASE WHEN ( (m.totalKm IS NOT NULL AND m.totalKm >= 1000.0) OR " +
            "               ( (CASE WHEN :incluirPausas = true THEN (CASE WHEN m.totalTiempoUso IS NULL THEN 0L ELSE m.totalTiempoUso END) ELSE 0L END) >= 500L ) ) "
            +
            "        THEN true ELSE false END) " +
            ") FROM Monopatin m")
    List<MonopatinReporteDTO> generarReporteKm(@Param("incluirPausas") boolean incluirPausas);



    @Modifying
    @Transactional
    @Query("UPDATE Monopatin m SET m.latitud = :lat, m.longitud = :lon WHERE m.id = :id")
    int updateUbicacionById(@Param("id") Long id, @Param("lat") Double lat, @Param("lon") Double lon);

    @Modifying
    @Transactional
    @Query("UPDATE Monopatin m SET m.estado = :estado WHERE m.id = :id")
    int updateEstadoById(@Param("id") Long id, @Param("estado") EstadoMonopatin estado);
}
