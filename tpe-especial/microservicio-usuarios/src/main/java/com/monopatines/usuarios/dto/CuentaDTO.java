package com.monopatines.usuarios.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private Long id;
    private LocalDate fechaAlta;
    private Double saldo;
    private Boolean activa;
    private String mercadoPagoId;
}
