package org.example.microserviciomonopatines.client;



import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CuentasClient {

    private final RestTemplate rest = new RestTemplate();

    public boolean esPremium(Long idCuenta) {
        String url = "http://localhost:8080/cuentas/" + idCuenta;

        try {
            Map<String, Object> response = rest.getForObject(url, Map.class);

            String tipo = (String) response.get("tipoCuenta");

            return "PREMIUM".equalsIgnoreCase(tipo);

        } catch (Exception e) {
            throw new RuntimeException("No se pudo verificar la cuenta " + idCuenta);
        }
    }

}
