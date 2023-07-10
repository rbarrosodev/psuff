package com.rodrigobarroso.service;

import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.exception.EntidadeNaoEncontradaException;
import com.rodrigobarroso.repository.AeroportoRepository;
import com.rodrigobarroso.repository.TerminalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AeroportoService {

    private final AeroportoRepository aeroportoRepository;
    private final TerminalRepository terminalRepository;

    @Autowired
    public AeroportoService(AeroportoRepository aeroportoRepository, TerminalRepository terminalRepository) {
        this.aeroportoRepository = aeroportoRepository;
        this.terminalRepository = terminalRepository;
    }

    public List<Aeroporto> recuperarAeroportos() {
        return aeroportoRepository.findAll();
    }

    public Aeroporto cadastrarAeroporto(Aeroporto aeroporto) {
        return aeroportoRepository.save(aeroporto);
    }

    public Aeroporto recuperarAeroportoPorId(Long id) {
        return aeroportoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Aeroporto de ID " + id + " não encontrado."));
    }

    public Aeroporto atualizarAeroporto(Aeroporto aeroporto) {
        Aeroporto umAeroporto = recuperarAeroportoPorId(aeroporto.getId());
        List<Terminal> newTerminais = aeroporto.getTerminais();
        List<Terminal> oldTerminais = umAeroporto.getTerminais();

        for (Terminal terminal : newTerminais) {
            if (!oldTerminais.contains(terminal)) {
                terminalRepository.findById(terminal.getId()).orElseThrow(()-> new EntidadeNaoEncontradaException(
                        "Terminal de ID " + terminal.getId() + " não encontrada."
                ));
            }
        }

        return aeroportoRepository.save(aeroporto);
    }

    public void removerAeroporto(Long id) {
        recuperarAeroportoPorId(id);
        aeroportoRepository.deleteById(id);
    }

    public List<Aeroporto> recuperarAeroportosPorTerminal(Long id) {
        return aeroportoRepository.findByTerminalId(id);
    }
}