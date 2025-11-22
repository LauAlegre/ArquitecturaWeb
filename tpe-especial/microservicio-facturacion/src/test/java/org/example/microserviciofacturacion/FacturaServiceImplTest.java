package org.example.microserviciofacturacion;

import example.org.microserviciofacturacion.client.CuentaClient;
import example.org.microserviciofacturacion.dto.DatosDeFacturacionDto;
import example.org.microserviciofacturacion.mapper.Facturamapper;

import example.org.microserviciofacturacion.model.Tarifa;
import example.org.microserviciofacturacion.repository.FacturaRepository;
import example.org.microserviciofacturacion.service.FacturaServiceImpl;
import example.org.microserviciofacturacion.service.TarifaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaServiceImplTest {

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private TarifaService tarifaService;


    @Mock
    private CuentaClient cuentaClient;   // 👈 ESTE ES EL MOCK IMPORTANTE

    @InjectMocks
    private FacturaServiceImpl facturaService;  // 👈 Mockito crea el service con los mocks

    @Test
    void generarFactura_debeDebitarSaldoEnElMicroservicioUsuarios() {

        // ---- Arrange ----
        DatosDeFacturacionDto dto = new DatosDeFacturacionDto(
                1L,       // idCuenta
                10L,      // idViaje
                30,       // minutosViaje
                0,        // minutosPausa
                false,
                LocalDate.now()
        );

        Tarifa tarifa = new Tarifa();
        tarifa.setPrecioMinuto(2.0);
        tarifa.setPrecioExtraPausa(10.0);

        when(tarifaService.vigente(any())).thenReturn(tarifa);
        when(facturaRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // ---- Act ----
        facturaService.generarFactura(dto);

        // ---- Assert ----
        verify(cuentaClient, times(1)).debitarSaldo(eq(1L), eq(60.0));  // 30 minutos * $2
    }
}
