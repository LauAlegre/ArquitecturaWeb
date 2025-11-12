package example.org.microserviciofacturacion.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CuentaClient {

    private final RestTemplate restTemplate = new RestTemplate();

    // 👈 correcto, porque el microservicio de usuarios corre en el puerto 8080

    /**
     * 🔹 Realiza el débito de saldo en una cuenta.
     * Llama al endpoint PUT /cuentas/{id}/debitar/{monto}
     */
    public void debitarSaldo(Long idCuenta, double monto) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/cuentas/{id}/debitar/{monto}")
                .buildAndExpand(idCuenta, monto)
                .toUriString();

        restTemplate.put(url, null);
    }
}
