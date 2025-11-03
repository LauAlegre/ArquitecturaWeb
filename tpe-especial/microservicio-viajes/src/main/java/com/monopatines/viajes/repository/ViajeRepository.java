package com.monopatines.viajes.repository;

import com.monopatines.viajes.model.ViajeModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ViajeRepository extends JpaRepository<ViajeModel, Long> {

    /* ===== Proyección para el reporte ===== */
    interface MonopatinViajesCount {
        Long getMonopatinId();
        Long getCantidad();
    }

    @Query("""
        select v.monopatinId as monopatinId, count(v) as cantidad
        from ViajeModel v
        where function('year', v.fechaInicio) = :anio
        group by v.monopatinId
        having count(v) > :minViajes
        order by cantidad desc
        """)
    List<MonopatinViajesCount> findMonopatinesConMasDeXViajesEnAnio(
            @Param("anio") int anio,
            @Param("minViajes") long minViajes
    );
}