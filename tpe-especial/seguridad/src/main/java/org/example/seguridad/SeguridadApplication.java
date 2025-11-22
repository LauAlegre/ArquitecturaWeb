package org.example.seguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.example.seguridad.repository.UserRepository;
import org.example.seguridad.repository.AuthorityRepository;
import org.example.seguridad.model.User;
import org.example.seguridad.model.Authority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@SpringBootApplication
public class SeguridadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeguridadApplication.class, args);
    }

    // Inicializador de datos para crear usuario admin (solo entorno de desarrollo con H2 en memoria)
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepo,
                                AuthorityRepository authRepo,
                                PasswordEncoder encoder) {
        return args -> {
            String adminUsername = "admin";
            userRepo.findOneWithAuthoritiesByUsernameIgnoreCase(adminUsername)
                    .ifPresentOrElse(
                            u -> System.out.println("✔ Admin ya existe"),
                            () -> {
                                // Crear autoridad ADMIN si no existe
                                Authority adminAuth = authRepo.findByName("ADMIN")
                                        .orElseGet(() -> authRepo.save(new Authority("ADMIN")));

                                User admin = new User();
                                admin.setUsername(adminUsername);
                                admin.setPassword(encoder.encode("admin123"));
                                admin.setAuthorities(new HashSet<>());
                                admin.getAuthorities().add(adminAuth);
                                userRepo.save(admin);
                                System.out.println("🔥 Usuario admin creado (admin/admin123)");
                            }
                    );
        };
    }
}
