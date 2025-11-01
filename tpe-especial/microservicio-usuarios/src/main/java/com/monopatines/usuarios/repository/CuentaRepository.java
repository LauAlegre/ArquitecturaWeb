package com.monopatines.usuarios.repository;

import com.monopatines.usuarios.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Cuenta c SET c.activa = false WHERE c.id = :id")
    void anularCuenta(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Cuenta c SET c.activa = true WHERE c.id = :id")
    void activarCuenta(Long id);
}
