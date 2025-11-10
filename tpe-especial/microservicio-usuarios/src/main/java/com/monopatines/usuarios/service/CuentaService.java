package com.monopatines.usuarios.service;

import com.monopatines.usuarios.dto.CuentaDTO;
import com.monopatines.usuarios.mapper.CuentaMapper;
import com.monopatines.usuarios.model.Cuenta;
import com.monopatines.usuarios.model.Usuario;
import com.monopatines.usuarios.repository.CuentaRepository;
import com.monopatines.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    private final CuentaRepository repo;
    private final UsuarioRepository usuarioRepo; // ✅ agregado
    private final CuentaMapper mapper;

    public CuentaService(CuentaRepository repo, UsuarioRepository usuarioRepo, CuentaMapper mapper) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo; // ✅ inicializado
        this.mapper = mapper;
    }

    // 📋 Listar todas las cuentas
    public List<CuentaDTO> listar() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Buscar una cuenta por ID
    public CuentaDTO buscarPorId(Long id) {
        Cuenta cuenta = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return mapper.toDTO(cuenta);
    }

    // ➕ Crear una cuenta nueva
    public CuentaDTO crear(CuentaDTO dto) {
        Cuenta nueva = mapper.toEntity(dto);
        Cuenta guardada = repo.save(nueva);
        return mapper.toDTO(guardada);
    }

    // ✏️ Actualizar cuenta existente
    public CuentaDTO actualizar(Long id, CuentaDTO dto) {
        Cuenta existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        existente.setSaldo(dto.getSaldo());
        existente.setActiva(dto.getActiva());
        existente.setMercadoPagoId(dto.getMercadoPagoId());
        return mapper.toDTO(repo.save(existente));
    }

    // 🗑️ Eliminar cuenta
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // 🚫 Anular cuenta
    public void anularCuenta(Long id) {
        repo.anularCuenta(id);
    }

    // ✅ Activar cuenta
    public void activarCuenta(Long id) {
        repo.activarCuenta(id);
    }

    // 🤝 Asociar usuario a cuenta
    public CuentaDTO asociarUsuario(Long idCuenta, Long idUsuario) {
        Cuenta cuenta = repo.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        cuenta.getUsuarios().add(usuario);
        repo.save(cuenta);

        return mapper.toDTO(cuenta);
    }
}

