package com.Mantenimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com.Mantenimiento")
public class Mantenimiento {
    public static void main(String[] args) { SpringApplication.run(Mantenimiento.class, args); }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}