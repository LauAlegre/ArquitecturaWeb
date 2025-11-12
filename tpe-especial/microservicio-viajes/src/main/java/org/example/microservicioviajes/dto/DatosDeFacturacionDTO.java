package org.example.microservicioviajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DatosDeFacturacionDTO {
    private Long idCuenta;
    private Long idViaje;
    private int minutosViaje;
    private int minutosPausa;
    private boolean pausaExtensa; // opcional, según tu lógica
    private LocalDate fechaEmision;
}
