package com.monopatines.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // 👈 Escanea automáticamente subpaquetes (controller, service, client, etc.)
public class MicroservicioUsuariosApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroservicioUsuariosApplication.class, args);
    }
}
