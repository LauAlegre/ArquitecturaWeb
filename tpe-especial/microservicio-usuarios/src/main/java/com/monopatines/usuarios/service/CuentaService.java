package com.monopatines.usuarios.service;

import com.monopatines.usuarios.dto.CuentaDTO;
import com.monopatines.usuarios.mapper.CuentaMapper;
import com.monopatines.usuarios.model.Cuenta;
import com.monopatines.usuarios.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    private final CuentaRepository repo;
    private final CuentaMapper mapper;

    public CuentaService(CuentaRepository repo, CuentaMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public List<CuentaDTO> listar() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public CuentaDTO buscarPorId(Long id) {
        Cuenta cuenta = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return mapper.toDTO(cuenta);
    }

    public CuentaDTO crear(CuentaDTO dto) {
        Cuenta nueva = mapper.toEntity(dto);
        Cuenta guardada = repo.save(nueva);
        return mapper.toDTO(guardada);
    }

    public CuentaDTO actualizar(Long id, CuentaDTO dto) {
        Cuenta existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        existente.setSaldo(dto.getSaldo());
        existente.setActiva(dto.getActiva());
        existente.setMercadoPagoId(dto.getMercadoPagoId());
        return mapper.toDTO(repo.save(existente));
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public void anularCuenta(Long id) {
        repo.anularCuenta(id);
    }

    public void activarCuenta(Long id) {
        repo.activarCuenta(id);
    }
}
