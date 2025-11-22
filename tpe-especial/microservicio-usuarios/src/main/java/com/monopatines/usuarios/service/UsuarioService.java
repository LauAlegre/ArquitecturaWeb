package com.monopatines.usuarios.service;

import com.monopatines.usuarios.client.ViajesClient;
import com.monopatines.usuarios.dto.UsoCuentaDTO;
import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.mapper.UsuarioMapper;
import com.monopatines.usuarios.model.Cuenta;
import com.monopatines.usuarios.model.Rol;
import com.monopatines.usuarios.model.TipoCuenta;
import com.monopatines.usuarios.model.Usuario;
import com.monopatines.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // 🔹 Por defecto, todos los métodos son de solo lectura
public class UsuarioService {

    private final UsuarioRepository repo;
    private final UsuarioMapper mapper;
    private final ViajesClient viajesClient;

    public UsuarioService(UsuarioRepository repo, UsuarioMapper mapper, ViajesClient viajesClient) {
        this.repo = repo;
        this.mapper = mapper;
        this.viajesClient =viajesClient;
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

    @Transactional(readOnly = false) // 🔹 Escritura → inicia transacción real
    public UsuarioDTO crear(UsuarioDTO dto, Long idAdmin) {
        verificarPermisosAdmin(idAdmin);
        Usuario nuevo = mapper.toEntity(dto);
        Usuario guardado = repo.save(nuevo);
        return mapper.toDTO(guardado);
    }

    @Transactional(readOnly = false)
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        existente.setNombre(dto.getNombre());
        existente.setApellido(dto.getApellido());
        existente.setEmail(dto.getEmail());
        existente.setCelular(dto.getCelular());
        return mapper.toDTO(repo.save(existente));
    }

    @Transactional(readOnly = false)
    public void eliminar(Long id, Long idAdmin) {
        verificarPermisosAdmin(idAdmin);

        repo.deleteById(id);
    }

    // ---------------------- CONSULTAS COMPLEJAS ----------------------

    /**
     * Obtiene el uso de monopatines por usuario.
     * Si incluirRelacionados = true, suma los viajes de todas las cuentas del usuario.
     * Si incluirRelacionados = false, solo los viajes del usuario específico.
     */
    public UsoCuentaDTO obtenerUso(Long idUsuario, LocalDate desde, LocalDate hasta, boolean incluirRelacionados) {



        // 2️⃣ Buscar usuario objetivo
        Usuario usuario = repo.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + idUsuario));

        double totalKm = 0;
        double totalMin = 0;
        int totalViajes = 0;

        if (incluirRelacionados) {
            List<Cuenta> cuentas = usuario.getCuentas();
            if (cuentas == null || cuentas.isEmpty()) {
                throw new RuntimeException("El usuario no tiene cuentas asociadas");
            }

            List<UsoCuentaDTO> usos = cuentas.stream()
                    .map(c -> viajesClient.obtenerUsoPorCuenta(c.getId(), desde, hasta))
                    .filter(Objects::nonNull)
                    .toList();

            totalKm = usos.stream().mapToDouble(UsoCuentaDTO::getKmTotales).sum();
            totalMin = usos.stream().mapToDouble(UsoCuentaDTO::getTiempoTotal).sum();
            totalViajes = usos.stream().mapToInt(UsoCuentaDTO::getCantidadViajes).sum();

            return new UsoCuentaDTO(null, "Agregado", totalKm, totalMin, totalViajes);

        } else {
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

    // ---------------------- PERMISOS Y UTILITARIOS ----------------------

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
        return usuario.getRol() == Rol.ADMIN;
    }

    public List<Long> obtenerUsuariosPorTipoCuenta(String tipoCuenta) {
        return repo.findIdsByTipoCuenta(TipoCuenta.valueOf(tipoCuenta.toUpperCase()));
    }

}
