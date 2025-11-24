package org.example.microserviciomonopatines.controller;


import org.example.microserviciomonopatines.service.impl.IaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ia")
public class IaController {

    @Autowired
    private IaService iaService;

    @PostMapping(value = "/prompt", produces = "application/json")
    public ResponseEntity<?> procesarPrompt(
            @RequestHeader("X-Cuenta-Id") Long idCuenta,   // ✅ VIENE EN HEADERS
            @RequestBody String prompt                    // ✅ BODY sigue como texto
    ) {
        try {
            return iaService.procesarPrompt(prompt, idCuenta);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al procesar el prompt: " + e.getMessage());
        }
    }
}

