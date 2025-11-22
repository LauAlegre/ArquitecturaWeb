package org.example.microserviciomonopatines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonopatinReporteDTO {
    private Long id;
    private double totalKm;
    private long totalTiempoUso;          // tiempo total (con pausas)
    private long totalTiempoUsoSinPausas; // tiempo solo de viajes
    private boolean requiereMantenimiento;
}
