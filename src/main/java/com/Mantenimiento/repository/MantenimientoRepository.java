package com.Mantenimiento.repository;

import com.Mantenimiento.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
    boolean existsByMonopatinIdAndFinIsNull(Long monopatinId);
    List<Mantenimiento> findByFinIsNull();
}