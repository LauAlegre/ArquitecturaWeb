package example.org.microserviciofacturacion.service;

import example.org.microserviciofacturacion.dto.TarifaDto;
import example.org.microserviciofacturacion.model.Tarifa;

import java.time.LocalDate;
import java.util.List;

public  interface TarifaService {


    /** Devuelve la tarifa aplicable a la fecha dada (hoy si pasás LocalDate.now()). */
    Tarifa vigente(LocalDate fecha);


    /** Crea un ajuste a partir de la fecha indicada en el DTO.
     *  Si había una tarifa vigente, la "cierra" el día anterior para evitar solapes.
     */
    Tarifa crearAjuste(TarifaDto dto);


    /** Historial completo ordenado por inicio de vigencia descendente. */
    List<Tarifa> historial();

}
