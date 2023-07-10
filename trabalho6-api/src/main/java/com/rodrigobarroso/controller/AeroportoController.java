package com.rodrigobarroso.controller;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.service.AeroportoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="aeroportos/")
public class AeroportoController {

    private final AeroportoService aeroportoService;

    @Autowired
    public AeroportoController(AeroportoService aeroportoService) {
        this.aeroportoService = aeroportoService;
    }

    @GetMapping
    public List<Aeroporto> recuperarAeroportos() {
        return aeroportoService.recuperarAeroportos();
    }

    @PostMapping
    public Aeroporto cadastrarAeroporto(@RequestBody Aeroporto aeroporto) {
        return aeroportoService.cadastrarAeroporto(aeroporto);
    }

    @GetMapping("{idAeroporto}")
    public Aeroporto recuperarAeroportoPorId(@PathVariable("idAeroporto") Long id) {
        return aeroportoService.recuperarAeroportoPorId(id);
    }

    @PutMapping
    public Aeroporto atualizarAeroporto(@RequestBody Aeroporto aeroporto) {
        return aeroportoService.atualizarAeroporto(aeroporto);
    }

    @DeleteMapping("{idAeroporto}")
    public void removerAeroporto(@PathVariable("idAeroporto") Long id) {
        aeroportoService.removerAeroporto(id);
    }

    @GetMapping("terminal/{idTerminal}")
    public List<Aeroporto> recuperarAeroportosPorTerminal(@PathVariable("idTerminal") Long id) {
        return aeroportoService.recuperarAeroportosPorTerminal(id);
    }
}
