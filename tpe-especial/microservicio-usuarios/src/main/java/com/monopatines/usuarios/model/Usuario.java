package com.monopatines.usuarios.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;
    private String email;
    private String celular;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @ManyToMany(mappedBy = "usuarios")
    private List<Cuenta> cuentas;


}
