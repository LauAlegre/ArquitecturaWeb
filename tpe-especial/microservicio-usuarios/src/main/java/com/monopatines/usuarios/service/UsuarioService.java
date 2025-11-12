package com.monopatines.usuarios.service;


import client.ViajesClient;
import com.monopatines.usuarios.dto.UsoCuentaDTO;
import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.mapper.UsuarioMapper;
import com.monopatines.usuarios.model.Cuenta;
import com.monopatines.usuarios.model.Rol;
import com.monopatines.usuarios.model.TipoCuenta;
import com.monopatines.usuarios.model.Usuario;
import com.monopatines.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    public UsoCuentaDTO obtenerUso(Long idUsuario, LocalDate desde, LocalDate hasta, boolean incluirRelacionados, Long idSolicitante) {

        // 1️⃣ Verificar permisos
        verificarPermisosAdmin(idSolicitante);

        // 2️⃣ Buscar usuario objetivo
        Usuario usuario = repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));

        double totalKm = 0;
        double totalMin = 0;
        int totalViajes = 0;

        if (incluirRelacionados) {
            // 🔹 Buscar todas las cuentas del usuario
            List<Cuenta> cuentas = usuario.getCuentas();
            if (cuentas == null || cuentas.isEmpty()) {
                throw new RuntimeException("El usuario no tiene cuentas asociadas");
            }

            // 🔹 Sumar los usos de todas las cuentas
            List<UsoCuentaDTO> usos = cuentas.stream()
                    .map(c -> viajesClient.obtenerUsoPorCuenta(c.getId(), desde, hasta))
                    .filter(Objects::nonNull)
                    .toList();

            totalKm = usos.stream().mapToDouble(UsoCuentaDTO::getKmTotales).sum();
            totalMin = usos.stream().mapToDouble(UsoCuentaDTO::getTiempoTotal).sum();
            totalViajes = usos.stream().mapToInt(UsoCuentaDTO::getCantidadViajes).sum();

            // Devuelve como agregado total
            return new UsoCuentaDTO(null,"Agregado" ,totalKm, totalMin, totalViajes);

        } else {
            // 🔹 Solo uso personal (por usuario)
            UsoCuentaDTO usoPersonal = viajesClient.obtenerUsoPorUsuario(usuario.getId(), desde, hasta);

            if (usoPersonal == null) {
                throw new RuntimeException("No se encontró información de uso para el usuario con id: " + idUsuario);
            }

            return new UsoCuentaDTO(
                    usuario.getId(),
                    "Usuario",
                    usoPersonal.getKmTotales(),
                    usoPersonal.getTiempoTotal(),
                    usoPersonal.getCantidadViajes()
            );
        }
    }

    /**
     * Verifica que el solicitante tenga rol ADMIN.
     */
    private void verificarPermisosAdmin(Long idSolicitante) {
        Usuario solicitante = repo.findById(idSolicitante)
                .orElseThrow(() -> new RuntimeException("Usuario solicitante no encontrado con id: " + idSolicitante));

        if (solicitante.getRol() != Rol.ADMIN) {
            throw new RuntimeException("No tiene permisos para ver el uso de otros usuarios");
        }
    }

    public boolean esAdmin(Long idUsuario) {
        Usuario usuario = repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));

        // Supone que Usuario tiene un atributo 'rol' o 'tipoUsuario'
        // Por ejemplo: "ADMIN" o "USER"
        return usuario.getRol() == Rol.ADMIN;
    }

    public List<Long> obtenerUsuariosPorTipoCuenta(String tipoCuenta) {
        return repo.findIdsByTipoCuenta(TipoCuenta.valueOf(tipoCuenta.toUpperCase()));
    }

}
