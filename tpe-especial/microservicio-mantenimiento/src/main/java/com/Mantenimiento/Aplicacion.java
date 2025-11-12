package com.Mantenimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.Mantenimiento")
public class Aplicacion {
    public static void main(String[] args) { SpringApplication.run(Aplicacion.class, args); }
}