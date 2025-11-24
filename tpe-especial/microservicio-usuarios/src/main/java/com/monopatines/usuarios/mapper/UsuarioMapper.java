package com.monopatines.usuarios.mapper;

import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // 🔹 Convierte de entidad → DTO
    public UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;



        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getCelular()
        );
    }

    // 🔹 Convierte de DTO → entidad
    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;



        return new Usuario(
                dto.getId(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getCelular(),

                null   // lista de cuentas ignorada (evita recursión infinita)
        );
    }
}
