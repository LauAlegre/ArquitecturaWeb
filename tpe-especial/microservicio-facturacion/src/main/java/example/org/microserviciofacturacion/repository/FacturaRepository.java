package example.org.microserviciofacturacion.repository;

import example.org.microserviciofacturacion.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // 🔹 Consulta personalizada para sumar el total facturado en un rango de fechas
    @Query("SELECT SUM(f.montoTotal) FROM Factura f " +
            "WHERE f.fechaEmision BETWEEN :inicio AND :fin")
    Double calcularTotalFacturado(LocalDate inicio, LocalDate fin);
}
