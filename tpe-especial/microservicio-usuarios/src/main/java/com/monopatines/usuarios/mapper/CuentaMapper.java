package com.monopatines.usuarios.mapper;

import com.monopatines.usuarios.dto.CuentaDTO;
import com.monopatines.usuarios.model.Cuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public CuentaDTO toDTO(Cuenta cuenta) {
        if (cuenta == null) return null;
        return new CuentaDTO(
                cuenta.getId(),
                cuenta.getFechaAlta(),
                cuenta.getSaldo(),
                cuenta.getActiva(),
                cuenta.getMercadoPagoId()
        );
    }

    public Cuenta toEntity(CuentaDTO dto) {
        if (dto == null) return null;
        return new Cuenta(
                dto.getId(),
                dto.getFechaAlta(),
                dto.getSaldo(),
                dto.getActiva(),
                dto.getMercadoPagoId(),
                null // lista de usuarios ignorada
        );
    }
}
