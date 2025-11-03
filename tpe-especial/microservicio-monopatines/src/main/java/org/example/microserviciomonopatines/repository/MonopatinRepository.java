package org.example.microserviciomonopatines.repository;

import org.example.microserviciomonopatines.model.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonopatinRepository extends JpaRepository<Monopatin, Long> {

    // Opcionales útiles:
    List<Monopatin> findByEstado(String estado);

    long countByEstado(String estado);

    List<Monopatin> findByParadaId(Long paradaId);
}
