package com.rodrigobarroso.servico.impl;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.dao.FabricaDeDAO;
import com.rodrigobarroso.excecao.OutdatedEntityException;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.AeroportoAppService;
import com.rodrigobarroso.servico.TerminalAppService;
import com.rodrigobarroso.excecao.AirportNotFoundException;
import com.rodrigobarroso.util.JPAUtil;

import java.util.List;

public class AeroportoAppServiceImpl implements AeroportoAppService {

    private final TerminalAppService terminalAppService;

    private static AeroportoAppService aeroportoAppService;
    private final AeroportoDAO aeroportoDAO;

    private AeroportoAppServiceImpl() {
        this.aeroportoDAO = FabricaDeDAO.getDao(AeroportoDAO.class);
        this.terminalAppService = TerminalAppServiceImpl.getInstance();
    }

    public static AeroportoAppService getInstance() {
        if (aeroportoAppService == null) {
            aeroportoAppService = new AeroportoAppServiceImpl();
        }

        return aeroportoAppService;
    }

    public void adiciona(Aeroporto aeroporto) {
        JPAUtil.beginTransaction();
        aeroportoDAO.adiciona(aeroporto);
        JPAUtil.commitTransaction();
        JPAUtil.closeEntityManager();
    }

    public void altera(Aeroporto aeroporto) throws AirportNotFoundException {
        JPAUtil.beginTransaction();
        try {
            aeroportoDAO.altera(aeroporto);
        } catch(AirportNotFoundException | OutdatedEntityException e){
            JPAUtil.rollbackTransaction();
            throw new AirportNotFoundException(e.getMessage());
        }

        JPAUtil.commitTransaction();
        JPAUtil.closeEntityManager();
    }


    public void deleta(Aeroporto aeroporto) throws AirportNotFoundException {
        JPAUtil.beginTransaction();
        aeroportoDAO.deleta(aeroporto);
        JPAUtil.commitTransaction();
        JPAUtil.closeEntityManager();
    }

    public Aeroporto recuperaAeroporto(String codigo) throws AirportNotFoundException {
        return aeroportoDAO.recuperaAeroporto(codigo);
    }


    public void adicionaTerminal(Terminal terminal) {
        terminalAppService.adiciona(terminal);
    }

    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        return terminalAppService.recuperaTerminaisPorAeroporto(aeroporto);
    }

    public List<Aeroporto> recuperaAeroportos() {
        return aeroportoDAO.recuperaAeroportos();
    }
}
