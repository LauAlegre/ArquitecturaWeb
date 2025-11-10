package com.monopatines.usuarios.service;


import client.ViajesClient;
import com.monopatines.usuarios.dto.UsoCuentaDTO;
import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.mapper.UsuarioMapper;
import com.monopatines.usuarios.model.Cuenta;
import com.monopatines.usuarios.model.Usuario;
import com.monopatines.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final UsuarioMapper mapper;
    private final ViajesClient viajesClient;

    public UsuarioService(UsuarioRepository repo, UsuarioMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
        this.viajesClient = new ViajesClient();
    }


    // ---------------------- CRUD ----------------------

    public List<UsuarioDTO> listar() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return mapper.toDTO(usuario);
    }

    public UsuarioDTO crear(UsuarioDTO dto) {
        Usuario nuevo = mapper.toEntity(dto);
        Usuario guardado = repo.save(nuevo);
        return mapper.toDTO(guardado);
    }

    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setCelular(dto.getCelular());
        return mapper.toDTO(repo.save(existente));
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // ---------------------- NUEVO MÉTODO ----------------------

    /**
     * Obtiene el uso de monopatines por usuario.
     * Si incluirRelacionados = true, suma los viajes de todas las cuentas del usuario.
     * Si incluirRelacionados = false, solo los viajes del usuario específico.
     */
    public UsoCuentaDTO obtenerUso(Long idUsuario, LocalDate desde, LocalDate hasta, boolean incluirRelacionados) {
        Usuario usuario = repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        double totalKm = 0;
        double totalMin = 0;
        int totalViajes = 0;

        if (incluirRelacionados) {
            // 🔹 Buscar todas las cuentas del usuario y traer los viajes por cuenta
            List<Cuenta> cuentas = usuario.getCuentas();
            if (cuentas.isEmpty()) {
                throw new RuntimeException("El usuario no tiene cuentas asociadas");
            }

            for (Cuenta cuenta : cuentas) {
                UsoCuentaDTO usoCuenta = viajesClient.obtenerUsoPorCuenta(cuenta.getId(), desde, hasta);
                if (usoCuenta != null) {
                    totalKm += usoCuenta.getKmTotales();
                    totalMin += usoCuenta.getTiempoTotal();
                    totalViajes += usoCuenta.getCantidadViajes();
                }
            }
        } else {
            // 🔹 Solo el uso personal (por idUsuario)
            UsoCuentaDTO usoPersonal = viajesClient.obtenerUsoPorUsuario(usuario.getId(), desde, hasta);
            if (usoPersonal != null) {
                totalKm += usoPersonal.getKmTotales();
                totalMin += usoPersonal.getTiempoTotal();
                totalViajes += usoPersonal.getCantidadViajes();
            }
        }

        return new UsoCuentaDTO(null, totalKm, totalMin, totalViajes);
    }
}
