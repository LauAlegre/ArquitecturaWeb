// mapper/MantenimientoMapper.java
package com.Mantenimiento.mapper;

import com.Mantenimiento.dto.MantenimientoDTO;
import com.Mantenimiento.model.Mantenimiento;
import org.springframework.stereotype.Component;

@Component
public class MantenimientoMapper {
    public MantenimientoDTO toDTO(Mantenimiento m){
        if (m == null) return null;
        return new MantenimientoDTO(
                m.getId(), m.getMonopatinId(), m.getInicio(), m.getFin(),
                m.getDescripcion(), m.getResponsable(), m.getObservaciones(), m.getCosto()
        );
    }
    public Mantenimiento toEntity(MantenimientoDTO dto){
        if (dto == null) return null;
        Mantenimiento m = new Mantenimiento();
        m.setId(dto.getId());
        m.setMonopatinId(dto.getMonopatinId());
        m.setInicio(dto.getInicio());
        m.setFin(dto.getFin());
        m.setDescripcion(dto.getDescripcion());
        m.setResponsable(dto.getResponsable());
        m.setObservaciones(dto.getObservaciones());
        m.setCosto(dto.getCosto());
        return m;
    }
}
