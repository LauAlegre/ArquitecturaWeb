package example.org.microserviciofacturacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "example.org.microserviciofacturacion.controller",
                "example.org.microserviciofacturacion.service",
                "example.org.microserviciofacturacion.repository",
                "example.org.microserviciofacturacion.model",
                "example.org.microserviciofacturacion.dto",
                "example.org.microserviciofacturacion.mapper"
        }
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
