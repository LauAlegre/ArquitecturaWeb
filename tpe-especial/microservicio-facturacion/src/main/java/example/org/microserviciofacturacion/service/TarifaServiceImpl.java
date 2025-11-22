package example.org.microserviciofacturacion.service;

import example.org.microserviciofacturacion.dto.TarifaDto;
import example.org.microserviciofacturacion.mapper.Tarifamapper;    // 👈
import example.org.microserviciofacturacion.model.Tarifa;
import example.org.microserviciofacturacion.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class TarifaServiceImpl implements TarifaService {

    private final TarifaRepository tarifaRepository;
    private final Tarifamapper tarifaMapper;

    public TarifaServiceImpl(TarifaRepository tarifaRepository,
                              Tarifamapper tarifamapper) {            // 👈
        this.tarifaRepository = tarifaRepository;
        this.tarifaMapper = tarifamapper;
    }

    @Override
    public Tarifa vigente(LocalDate fecha) {
        LocalDate f = (fecha == null) ? LocalDate.now() : fecha;
        return tarifaRepository.findVigente(f)
                .orElseThrow(() -> new IllegalStateException("No hay tarifa vigente para la fecha: " + f));
    }

    @Transactional
    @Override
    public Tarifa crearAjuste(TarifaDto dto) {

        if (dto.getFechaInicioVigencia() == null)
            throw new IllegalArgumentException("fechaInicioVigencia es obligatoria");

        LocalDate inicioNueva = dto.getFechaInicioVigencia();
        if (dto.getFechaFinVigencia() != null && dto.getFechaFinVigencia().isBefore(inicioNueva))
            throw new IllegalArgumentException("fechaFinVigencia no puede ser anterior al inicio");

        tarifaRepository.findVigente(inicioNueva).ifPresent(vigente -> {
            LocalDate nuevoFin = inicioNueva.minusDays(1);
            if (vigente.getFechaFinVigencia() == null || !vigente.getFechaFinVigencia().isBefore(nuevoFin)) {
                vigente.setFechaFinVigencia(nuevoFin);
                tarifaRepository.save(vigente);
            }
        });

        // 👇 en vez de construirla “a mano”
        Tarifa nueva = tarifaMapper.toEntity(dto);
        return tarifaRepository.save(nueva);
    }

    @Override
    public List<Tarifa> historial() {
        return tarifaRepository.findAll().stream()
                .sorted(Comparator.comparing(Tarifa::getFechaInicioVigencia).reversed())
                .toList();
    }
    @Override
    @Transactional(readOnly = false)
    public void eliminarTarifa(Long id) {


        Tarifa tarifa = tarifaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe la tarifa con id " + id));

        tarifaRepository.delete(tarifa);
    }

}
