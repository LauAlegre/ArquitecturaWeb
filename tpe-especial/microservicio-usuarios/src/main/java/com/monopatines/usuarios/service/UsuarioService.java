package com.monopatines.usuarios.service;

import com.monopatines.usuarios.dto.UsuarioDTO;
import com.monopatines.usuarios.mapper.UsuarioMapper;
import com.monopatines.usuarios.model.Usuario;
import com.monopatines.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final UsuarioMapper mapper;

    public UsuarioService(UsuarioRepository repo, UsuarioMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

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
}
