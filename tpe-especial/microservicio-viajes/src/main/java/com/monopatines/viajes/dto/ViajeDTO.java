package com.monopatines.viajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViajeDTO {
    private Long idViaje;
    private LocalTime fecha_inicio;
    private LocalTime fecha_fin;
    private double km_recorridos;
    private CuentaDTO id_cuenta;
    private MonopatinDTO id_monopatin;
}
