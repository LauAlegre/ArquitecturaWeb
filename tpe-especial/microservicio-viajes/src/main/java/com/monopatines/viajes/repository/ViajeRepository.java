package com.monopatines.viajes.repository;

import com.monopatines.viajes.model.ViajeModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<ViajeModel, Long> {

    /* ===== Proyección para el reporte existente ===== */
    interface MonopatinViajesCount {
        Long getMonopatinId();
        Long getCantidad();
    }

    /* ===== Proyecciones de uso ===== */
    interface UsoUsuario {
        Long getUsuarioId();
        Long getCantidadViajes();
        java.math.BigDecimal getTotalKm();
        Long getTotalMinutos();
    }

    interface UsoCuenta {
        Long getCuentaId();
        Long getCantidadViajes();
        java.math.BigDecimal getTotalKm();
        Long getTotalMinutos();
    }

    @Query("""
        select v.monopatinId as monopatinId, count(v) as cantidad
        from ViajeModel v
        where function('year', v.fechaInicio) = :anio
        group by v.monopatinId
        having count(v) > :minViajes
        order by cantidad desc
        """)
    List<MonopatinViajesCount> findMonopatinesConMasDeXViajesEnAnio(
            @Param("anio") int anio,
            @Param("minViajes") long minViajes
    );

    @Query("""
        select v.monopatinId as monopatinId, count(v.id) as cantidad
        from ViajeModel v
        where v.fechaInicio >= :inicio and v.fechaInicio < :fin
        group by v.monopatinId
        having count(v.id) > :minViajes
        order by cantidad desc
        """)
    List<MonopatinViajesCount> findMonopatinesConMasDeXViajesEntre(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("minViajes") long minViajes
    );

    /* ===== Uso por usuario (ranking) - nativa ===== */
    @Query(value = """
        select 
          v.id_usuario as usuarioId,
          count(v.id_viaje) as cantidadViajes,
          coalesce(sum(v.km_recorridos),0) as totalKm,
          coalesce(sum(TIMESTAMPDIFF(MINUTE, v.fecha_inicio, v.fecha_fin)),0) as totalMinutos
        from viaje v
        where v.fecha_inicio >= :desde and v.fecha_inicio < :hasta
          and v.fecha_fin is not null
        group by v.id_usuario
        order by totalMinutos desc
        """, nativeQuery = true)
    List<UsoUsuario> findUsoUsuariosPeriodo(@Param("desde") LocalDateTime desde,
                                            @Param("hasta") LocalDateTime hasta);

    /* ===== Uso agregado de un usuario específico - nativa ===== */
    @Query(value = """
        select 
          v.id_usuario as usuarioId,
          count(v.id_viaje) as cantidadViajes,
          coalesce(sum(v.km_recorridos),0) as totalKm,
          coalesce(sum(TIMESTAMPDIFF(MINUTE, v.fecha_inicio, v.fecha_fin)),0) as totalMinutos
        from viaje v
        where v.id_usuario = :idUsuario
          and v.fecha_inicio >= :desde and v.fecha_inicio < :hasta
          and v.fecha_fin is not null
        group by v.id_usuario
        """, nativeQuery = true)
    List<UsoUsuario> findUsoUsuario(@Param("idUsuario") Long idUsuario,
                                    @Param("desde") LocalDateTime desde,
                                    @Param("hasta") LocalDateTime hasta);

    /* ===== Uso agregado de una cuenta específica - nativa ===== */
    @Query(value = """
        select 
          v.id_cuenta as cuentaId,
          count(v.id_viaje) as cantidadViajes,
          coalesce(sum(v.km_recorridos),0) as totalKm,
          coalesce(sum(TIMESTAMPDIFF(MINUTE, v.fecha_inicio, v.fecha_fin)),0) as totalMinutos
        from viaje v
        where v.id_cuenta = :idCuenta
          and v.fecha_inicio >= :desde and v.fecha_inicio < :hasta
          and v.fecha_fin is not null
        group by v.id_cuenta
        """, nativeQuery = true)
    List<UsoCuenta> findUsoCuenta(@Param("idCuenta") Long idCuenta,
                                  @Param("desde") LocalDateTime desde,
                                  @Param("hasta") LocalDateTime hasta);

}