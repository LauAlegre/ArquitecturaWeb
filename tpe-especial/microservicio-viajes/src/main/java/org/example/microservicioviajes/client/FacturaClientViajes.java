package org.example.microservicioviajes.client;

import org.example.microservicioviajes.dto.DatosDeFacturacionDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class FacturaClientViajes {

    private final RestTemplate restTemplate = new RestTemplate();

    public void generarFactura(DatosDeFacturacionDTO dto) {
        try {
            String url = UriComponentsBuilder
                    .fromHttpUrl("http://localhost:8082/facturas/generar")
                    .toUriString();
            restTemplate.postForLocation(url, dto);
        } catch (Exception e) {
            System.err.println("Error enviando datos de facturación: " + e.getMessage());
        }
    }
}
