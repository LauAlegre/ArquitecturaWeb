package com.Mantenimiento.controller;

import com.Mantenimiento.dto.MantenimientoDTO;
import com.Mantenimiento.service.MantenimientoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/mantenimientos")
public class MantenimientoController {
    private final MantenimientoService service;
    public MantenimientoController(MantenimientoService s){ this.service=s; }

    @PostMapping("/iniciar")
    public MantenimientoDTO iniciar(@RequestParam Long monopatinId,
                                    @RequestParam(required=false) String descripcion,
                                    @RequestParam(required=false) String responsable,
                                    @RequestParam(required=false) Double costo){
        return service.iniciar(monopatinId, descripcion, responsable, costo);
    }

    @PutMapping("/{id}/finalizar")
    public MantenimientoDTO finalizar(@PathVariable Long id,
                                      @RequestParam(required=false) String observaciones,
                                      @RequestParam(required=false) Long paradaId){
        return service.finalizar(id, observaciones, paradaId);
    }

    @GetMapping("/activos")
    public List<MantenimientoDTO> activos(){ return service.activos(); }
}
