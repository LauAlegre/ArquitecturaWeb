package com.Mantenimiento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MantenimientoDTO {
    public Long id;
    public Long monopatinId;
    public LocalDateTime inicio, fin;
    public String descripcion, responsable, observaciones;
    public Double costo;
}
