package org.example.microserviciomonopatines.dto;

import org.example.microserviciomonopatines.model.EstadoMonopatin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinDTO {

    private Long id;
    private EstadoMonopatin estado; // Ej: "DISPONIBLE", "EN_USO", "PAUSADO", "EN_MANTENIMIENTO"
    private Double latitud;
    private Double longitud;
    private Double totalKm;
    private Long totalTiempoUso; // En segundos o minutos, según tu modelo
    private Long paradaId; // Id de la parada actual (puede ser null)
}
