package org.example.microservicioviajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsoUsuarioDTO {
    private Long usuarioId;
    private double totalKm;
    private double totalMinutos;
    private int cantidadViajes;
}

