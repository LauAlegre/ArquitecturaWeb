package com.monopatines.usuarios.service;

import com.monopatines.usuarios.dto.CuentaDTO;
import com.monopatines.usuarios.mapper.CuentaMapper;
import com.monopatines.usuarios.model.Cuenta;
import com.monopatines.usuarios.model.Rol;
import com.monopatines.usuarios.model.Usuario;
import com.monopatines.usuarios.repository.CuentaRepository;
import com.monopatines.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 🔹 Por defecto todos los métodos solo leen
public class CuentaService {

    private final CuentaRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final CuentaMapper mapper;

    public CuentaService(CuentaRepository repo, UsuarioRepository usuarioRepo, CuentaMapper mapper) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
        this.mapper = mapper;
    }

    // 📋 Listar todas las cuentas → solo lectura, no hace falta cambiar nada
    public List<CuentaDTO> listar() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Buscar una cuenta por ID → solo lectura
    public CuentaDTO buscarPorId(Long id) {
        Cuenta cuenta = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return mapper.toDTO(cuenta);
    }

    // ➕ Crear una cuenta nueva → modifica, necesita transacción de escritura
    @Transactional(readOnly = false)
    public CuentaDTO crear(CuentaDTO dto, Long idAdmin) {
        Usuario admin = usuarioRepo.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario administrador no encontrado"));
        if (admin.getRol() != Rol.ADMIN) {
            throw new RuntimeException("El usuario no tiene permisos de administrador");
        }
        Cuenta nueva = mapper.toEntity(dto);
        Cuenta guardada = repo.save(nueva);
        return mapper.toDTO(guardada);
    }

    // ✏️ Actualizar cuenta existente → también modifica
    @Transactional(readOnly = false)
    public CuentaDTO actualizar(Long id, CuentaDTO dto) {
        Cuenta existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        existente.setSaldo(dto.getSaldo());
        existente.setActiva(dto.getActiva());
        existente.setMercadoPagoId(dto.getMercadoPagoId());
        return mapper.toDTO(repo.save(existente));
    }

    // 🗑️ Eliminar cuenta → escritura
    @Transactional(readOnly = false)
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // 🚫 Anular cuenta → escritura
    @Transactional(readOnly = false)
    public void anularCuenta(Long id) {
        repo.anularCuenta(id);
    }

    // ✅ Activar cuenta → escritura
    @Transactional(readOnly = false)
    public void activarCuenta(Long id) {

        repo.activarCuenta(id);
    }

    // 🤝 Asociar usuario a cuenta → escritura
    @Transactional(readOnly = false)
    public CuentaDTO asociarUsuario(Long idCuenta, Long idUsuario) {
        Cuenta cuenta = repo.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Usuario usuario = usuarioRepo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        cuenta.getUsuarios().add(usuario);
        repo.save(cuenta);

        return mapper.toDTO(cuenta);
    }

    // 💸 Debitar saldo → escritura
    @Transactional(readOnly = false)
    public void debitarSaldo(Long idCuenta, double monto) {
        Cuenta cuenta = repo.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (cuenta.getSaldo() == null) {
            throw new RuntimeException("La cuenta no tiene saldo definido");
        }

        if (cuenta.getSaldo() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        cuenta.setSaldo(cuenta.getSaldo() - monto);
        repo.save(cuenta);
    }
}
