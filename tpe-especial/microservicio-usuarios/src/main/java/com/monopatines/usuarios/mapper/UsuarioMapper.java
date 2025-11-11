package com.monopatines.usuarios.mapper;

import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.model.Rol;
import com.monopatines.usuarios.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // 🔹 Convierte de entidad → DTO
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        String rol = (usuario.getRol() != null)
                ? usuario.getRol().name() // Enum → String
                : null;

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getCelular(),
                rol
        );
    }

    // 🔹 Convierte de DTO → entidad
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;

        Rol rol = null;
        if (dto.getRol() != null) {
            try {
                rol = Rol.valueOf(dto.getRol().toUpperCase()); // String → Enum
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Rol inválido: " + dto.getRol());
            }
        }

        return new Usuario(
                dto.getId(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getCelular(),
                rol,   // ✅ conversión agregada
                null   // lista de cuentas ignorada (evita recursión infinita)
        );
    }
}
