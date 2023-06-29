package com.rodrigobarroso.servico.impl;

import com.rodrigobarroso.dao.FabricaDeDAO;
import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.TerminalAppService;
import com.rodrigobarroso.util.JPAUtil;

import java.util.List;

public class TerminalAppServiceImpl implements TerminalAppService {

    private final TerminalDAO terminalDAO;
    private static TerminalAppService terminalAppService;

    private TerminalAppServiceImpl() {
        this.terminalDAO = FabricaDeDAO.getDao(TerminalDAO.class);
    }

    public static TerminalAppService getInstance() {
        if (terminalAppService == null) {
            terminalAppService = new TerminalAppServiceImpl();
        }

        return terminalAppService;
    }

    @Override
    public void adiciona(Terminal terminal) {
        JPAUtil.beginTransaction();
        terminalDAO.adiciona(terminal);
        JPAUtil.commitTransaction();
        JPAUtil.closeEntityManager();
    }

    @Override
    public List<Terminal> recuperaTerminaisPorAeroporto(Aeroporto aeroporto) {
        return terminalDAO.recuperaTerminaisPorAeroporto(aeroporto);
    }
}
