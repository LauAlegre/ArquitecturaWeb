package com.monopatines.viajes.mapper;

import com.monopatines.viajes.dto.ViajeDTO;
import com.monopatines.viajes.model.ViajeModel;

public class ViajeMapper {
    private final PausaMapper pausaMapper = new PausaMapper();

    public ViajeDTO toDTO(ViajeModel viaje) {
        if (viaje == null) return null;

        ViajeDTO dto = new ViajeDTO();
        dto.setIdViaje(viaje.getId());
        dto.setFecha_inicio(viaje.getFechaInicio());
        dto.setFecha_fin(viaje.getFechaFin());
        dto.setKm_recorridos(viaje.getKmRecorridos());

        // mapeamos objetos anidados simples
        if (viaje.getCuentaId() != null) {
            CuentaDTO cuentaDTO = new CuentaDTO();
            cuentaDTO.setIdCuenta(viaje.getId_cuenta());
            dto.setId_cuenta(cuentaDTO);
        }

        if (viaje.getId_monopatin() != null) {
            MonopatinDTO monopatinDTO = new MonopatinDTO();
            monopatinDTO.setIdMonopatin(viaje.getId_monopatin());
            dto.setId_monopatin(monopatinDTO);
        }

        return dto;
    }

    public ViajeModel toEntity(ViajeDTO dto) {
        if (dto == null) return null;

        ViajeModel viaje = new ViajeModel();
        viaje.setId(dto.getIdViaje());
        viaje.setFechaInicio(dto.getFecha_inicio());
        viaje.setFechaFin(dto.getFecha_fin());
        viaje.setKmRecorridos(dto.getKm_recorridos());

        // si tu entidad guarda los IDs como números, asignalos así:
        if (dto.getId_cuenta() != null) {
            viaje.setId_cuenta(dto.getId_cuenta().getIdCuenta());
        }

        if (dto.getId_monopatin() != null) {
            viaje.setId_monopatin(dto.getId_monopatin().getIdMonopatin());
        }

        // la lista de pausas se ignora para evitar recursión (si existe)
        return viaje;
    }

}

