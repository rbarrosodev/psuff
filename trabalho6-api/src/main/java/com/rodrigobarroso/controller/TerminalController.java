package com.rodrigobarroso.controller;

import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.service.TerminalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("terminais/")
public class TerminalController {

    private final TerminalService terminalService;

    public TerminalController(TerminalService terminalService) {
        this.terminalService = terminalService;
    }

    @PutMapping
    public Terminal atualizarTerminal(@RequestBody Terminal terminal) {
        return terminalService.atualizarTerminal(terminal);
    }

    @PostMapping
    public Terminal cadastrarTerminal(@RequestBody Terminal terminal) {
        return terminalService.cadastrarTerminal(terminal);
    }

    @DeleteMapping("{idTerminal}")
    public void removerTerminal(@PathVariable("idTerminal") Long id) {
        terminalService.removerTerminal(id);
    }

    @GetMapping
    public List<Terminal> recuperarTerminais() {
        return terminalService.recuperarTerminais();
    }

    @GetMapping("{idTerminal}")
    private Terminal recuperarTerminalPorId(@PathVariable("idTerminal") Long id) {
        return terminalService.recuperarTerminaisPorId(id);
    }
}