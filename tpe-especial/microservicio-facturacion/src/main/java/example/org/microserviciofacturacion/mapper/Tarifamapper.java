package example.org.microserviciofacturacion.mapper;

import example.org.microserviciofacturacion.dto.TarifaDto;
import example.org.microserviciofacturacion.model.Tarifa;
import org.springframework.stereotype.Component;

@Component
public class Tarifamapper {

    // Convierte un DTO a una entidad Tarifa
    public Tarifa toEntity(TarifaDto dto) {
        Tarifa tarifa = new Tarifa();
        tarifa.setPrecioMinuto(dto.getPrecioMinuto());
        tarifa.setPrecioExtraPausa(dto.getPrecioExtraPausa());
        tarifa.setFechaInicioVigencia(dto.getFechaInicioVigencia());
        tarifa.setFechaFinVigencia(dto.getFechaFinVigencia());
        return tarifa;
    }

    // Convierte una entidad Tarifa a un DTO
    public TarifaDto toDto(Tarifa tarifa) {
        TarifaDto dto = new TarifaDto();
        dto.setPrecioMinuto(tarifa.getPrecioMinuto());
        dto.setPrecioExtraPausa(tarifa.getPrecioExtraPausa());
        dto.setFechaInicioVigencia(tarifa.getFechaInicioVigencia());
        dto.setFechaFinVigencia(tarifa.getFechaFinVigencia());
        return dto;
    }
}
