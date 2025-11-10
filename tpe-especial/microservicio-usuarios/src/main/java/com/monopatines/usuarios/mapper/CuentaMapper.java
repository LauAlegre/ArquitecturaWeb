package com.monopatines.usuarios.mapper;

import com.monopatines.usuarios.dto.CuentaDTO;
import com.monopatines.usuarios.model.Cuenta;
import com.monopatines.usuarios.model.TipoCuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    // 🔹 Convierte de entidad → DTO
    public CuentaDTO toDTO(Cuenta cuenta) {
        if (cuenta == null) return null;

        String tipoCuenta = (cuenta.getTipoCuenta() != null)
                ? cuenta.getTipoCuenta().name()  // Enum → String
                : null;

        return new CuentaDTO(
                cuenta.getId(),
                cuenta.getFechaAlta(),
                cuenta.getSaldo(),
                cuenta.getActiva(),
                cuenta.getMercadoPagoId(),
                tipoCuenta
        );
    }

    // 🔹 Convierte de DTO → entidad
    public Cuenta toEntity(CuentaDTO dto) {
        if (dto == null) return null;

        TipoCuenta tipo = null;
        if (dto.getTipoCuenta() != null) {
            try {
                tipo = TipoCuenta.valueOf(dto.getTipoCuenta().toUpperCase()); // String → Enum
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Tipo de cuenta inválido: " + dto.getTipoCuenta());
            }
        }

        return new Cuenta(
                dto.getId(),
                dto.getFechaAlta(),
                dto.getSaldo(),
                dto.getActiva(),
                dto.getMercadoPagoId(),
                tipo,
                null // lista de usuarios ignorada en el mapper
        );
    }
}
