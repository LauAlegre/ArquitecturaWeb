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


    @Query("""
       SELECT new org.example.microserviciomonopatines.dto.MonopatinReporteDTO(
           m.id,
           m.totalKm,
           m.totalTiempoUso,
           CASE 
               WHEN m.totalKm < 1500 THEN false
               ELSE true
           END
       )
       FROM Monopatin m
       ORDER BY m.totalKm DESC
       """)
    List<MonopatinReporteDTO> generarReporteKm();



    @Modifying
    @Transactional
    @Query("UPDATE Monopatin m SET m.latitud = :lat, m.longitud = :lon WHERE m.id = :id")
    int updateUbicacionById(@Param("id") Long id, @Param("lat") Double lat, @Param("lon") Double lon);

    @Modifying
    @Transactional
    @Query("UPDATE Monopatin m SET m.estado = :estado WHERE m.id = :id")
    int updateEstadoById(@Param("id") Long id, @Param("estado") EstadoMonopatin estado);
}
