package org.example.microservicioviajes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.example.microservicioviajes")
@EnableJpaRepositories(basePackages = "org.example.microservicioviajes.repository")
@EntityScan(basePackages = "org.example.microservicioviajes.model")
public class MicroservicioViajesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioViajesApplication.class, args);
    }

}
