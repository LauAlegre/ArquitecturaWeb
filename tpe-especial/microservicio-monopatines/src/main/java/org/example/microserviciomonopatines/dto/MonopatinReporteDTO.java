package org.example.microserviciomonopatines.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinReporteDTO {
    private Long id;
    private Double totalKm;
    private Long totalTiempoUso;
    private Boolean requiereMantenimiento;
}