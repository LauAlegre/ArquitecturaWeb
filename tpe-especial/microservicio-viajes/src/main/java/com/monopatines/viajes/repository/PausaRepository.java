package com.monopatines.viajes.repository;

import com.monopatines.viajes.model.PausaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PausaRepository extends JpaRepository<PausaModel, Long> {

    boolean existsByViajeIdAndFechaFinIsNull(Long viajeId);

    List<PausaModel> findByViajeId(Long viajeId);

    Optional<PausaModel> findFirstByViajeIdAndFechaFinIsNullOrderByFechaInicioDesc(Long viajeId);
}