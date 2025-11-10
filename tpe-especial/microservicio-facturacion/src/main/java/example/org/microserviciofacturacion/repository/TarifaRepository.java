package example.org.microserviciofacturacion.repository;

import example.org.microserviciofacturacion.model.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TarifaRepository extends JpaRepository<Tarifa, Long> {

    // Busca la tarifa vigente a una fecha determinada
    @Query("""
           SELECT t FROM Tarifa t
           WHERE t.fechaInicioVigencia <= :fecha
             AND (t.fechaFinVigencia IS NULL OR t.fechaFinVigencia >= :fecha)
           ORDER BY t.fechaInicioVigencia DESC
           """)
    Optional<Tarifa> findVigente(@Param("fecha") LocalDate fecha);
}
