package com.rodrigobarroso.servico.impl;

import com.rodrigobarroso.dao.controle.FabricaDeDAO;
import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.TerminalAppService;

import java.util.List;

public class TerminalAppServiceImpl implements TerminalAppService {

    private final TerminalDAO terminalDAO;
    private static TerminalAppService terminalAppService;

    private TerminalAppServiceImpl() {
        this.terminalDAO = FabricaDeDAO.getDAO(TerminalDAO.class);
    }

    public static TerminalAppService getInstance() {
        if (terminalAppService == null) {
            terminalAppService = new TerminalAppServiceImpl();
        }

        return terminalAppService;
    }

    @Override
    public void adiciona(Terminal terminal) {
        terminalDAO.adiciona(terminal);
    }

    @Override
    public List<Terminal> recuperaTerminaisPorAeroporto(Aeroporto aeroporto) {
        return terminalDAO.recuperaTerminaisPorAeroporto(aeroporto);
    }
}
