package example.org.microserviciofacturacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosDeFacturacionDto {
    private Long idCuenta;
    private String idViaje;
    private int minutosViaje;
    private int minutosPausa;
    private boolean pausaExtensa;  // opcional, según tu lógica
    private LocalDate fechaEmision;
}
