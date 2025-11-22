package example.org.microserviciofacturacion.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "facturas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cuenta", nullable = false)
    private Long idCuenta;

    // 🔥 AHORA STRING PARA ACEPTAR ObjectId DE MONGO
    @Column(name = "id_viaje", nullable = false, length = 40)
    private String idViaje;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "monto_total", nullable = false)
    private Double montoTotal;
}
