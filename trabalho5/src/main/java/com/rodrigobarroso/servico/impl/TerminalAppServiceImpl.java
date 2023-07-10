package com.rodrigobarroso.servico.impl;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.TerminalAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class TerminalAppServiceImpl implements TerminalAppService {
    @Autowired
    private TerminalDAO terminalDAO;

    @Transactional
    public void adiciona(Terminal terminal) {
        terminalDAO.inclui(terminal);
    }

    @Override
    public List<Terminal> recuperaTerminais() {
        return terminalDAO.recuperaTerminais();
    }
}