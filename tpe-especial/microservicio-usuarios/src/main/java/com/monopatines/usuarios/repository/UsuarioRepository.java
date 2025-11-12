package com.monopatines.usuarios.repository;

import com.monopatines.usuarios.model.TipoCuenta;
import com.monopatines.usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("""
    SELECT DISTINCT u.id 
    FROM Usuario u 
    JOIN u.cuentas c 
    WHERE c.tipoCuenta = :tipoCuenta
""")
    List<Long> findIdsByTipoCuenta(@Param("tipoCuenta") TipoCuenta tipoCuenta);

}

