package com.rodrigobarroso.service;

import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.exception.EntidadeNaoEncontradaException;
import com.rodrigobarroso.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalService {

    private final TerminalRepository terminalRepository;

    @Autowired
    public TerminalService(TerminalRepository terminalRepository) {
        this.terminalRepository = terminalRepository;
    }

    public Terminal atualizarTerminal(Terminal terminal) {
        return terminalRepository.save(terminal);
    }

    public Terminal cadastrarTerminal(Terminal terminal) {
        return terminalRepository.save(terminal);
    }

    public void removerTerminal(Long id) {
        recuperarTerminaisPorId(id);
        terminalRepository.deleteById(id);
    }

    public List<Terminal> recuperarTerminais() {
        return terminalRepository.findAll();
    }

    public Terminal recuperarTerminaisPorId(Long id) {
        return terminalRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Terminal de ID " + id + " não encontrada.")
        );
    }
}