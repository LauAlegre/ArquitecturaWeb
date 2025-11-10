package org.example.microservicioviajes;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.monopatines.viajes")
@EnableJpaRepositories(basePackages = "com.monopatines.viajes.repository")
@EntityScan(basePackages = "com.monopatines.viajes.model")
public class MicroservicioViajesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioViajesApplication.class, args);
    }

}
