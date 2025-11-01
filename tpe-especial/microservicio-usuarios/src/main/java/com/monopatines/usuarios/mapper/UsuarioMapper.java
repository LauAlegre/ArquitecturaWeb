package com.monopatines.usuarios.mapper;

import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

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

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) return null;
        return new Usuario(
                dto.getId(),
                dto.getNombre(),
                dto.getApellido(),
                dto.getEmail(),
                dto.getCelular(),
                null // lista de cuentas se ignora para evitar recursión
        );
    }
}
