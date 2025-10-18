package org.service;

import org.dto.CarreraDTO;
import org.mapper.CarreraMapper;
import org.model.Carrera;
import org.repository.CarreraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CarreraService {
    private final CarreraRepository repo;

    public CarreraService(CarreraRepository carreraRepository) {
        this.repo = carreraRepository;
    }

    public CarreraDTO crearCarrera(CarreraDTO carreraDTO) {
        Carrera carrera = CarreraMapper.toEntity(carreraDTO);
        Carrera guardado = repo.save(carrera);
        return CarreraMapper.toDto(guardado);

    }
    // f) recuperar las carreras con estudiantes inscriptos, ordenadas por cantidad de inscriptos (desc)
    public List<CarreraDTO> obtenerCarrerasConInscriptosOrdenadasPorCantidadDesc() {
        return repo.findCarrerasConInscriptosOrderByCantidadDesc();
    }


    public List<CarreraDTO> obtenerTodasLasCarreras() {
        return repo.findAll()
                .stream()
                .map(CarreraMapper::toDto)
                .toList();
    }





}
