package org.example.microservicioviajes.repository;

import org.example.microservicioviajes.model.PausaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PausaRepository extends JpaRepository<PausaModel, Long> {

    boolean existsByViajeIdAndFechaFinIsNull(Long viajeId);

    List<PausaModel> findByViajeId(Long viajeId);

    Optional<PausaModel> findFirstByViajeIdAndFechaFinIsNullOrderByFechaInicioDesc(Long viajeId);

    @Query("select coalesce(sum(p.duracionMinutos), 0) from PausaModel p where p.viaje.id = :viajeId")
    Long sumDuracionMinutosByViajeId(@Param("viajeId") Long viajeId);
}