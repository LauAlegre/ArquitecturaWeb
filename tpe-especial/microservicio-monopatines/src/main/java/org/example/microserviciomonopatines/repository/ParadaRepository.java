package org.example.microserviciomonopatines.repository;

import org.example.microserviciomonopatines.model.Parada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParadaRepository extends JpaRepository<Parada, Long> {

}
