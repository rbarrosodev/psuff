package com.rodrigobarroso.servico.impl;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.dao.FabricaDeDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.servico.AeroportoAppService;
import com.rodrigobarroso.servico.TerminalAppService;
import com.rodrigobarroso.util.AeroportoNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
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
        aeroportoDAO.adiciona(aeroporto);
    }

    public void altera(Aeroporto aeroporto) throws AeroportoNotFoundException {
        try {
            aeroportoDAO.altera(aeroporto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void deleta(Aeroporto aeroporto) throws AeroportoNotFoundException {
        try {
            Aeroporto aero = aeroportoDAO.recuperaAeroporto(aeroporto.getCodigo());

            if (aero != null) {
                aeroportoDAO.deleta(aeroporto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Aeroporto recuperaAeroporto(String codigo) {
        String hql = "SELECT a from Aeroporto a where a.codigo = '%s'".formatted(codigo);

        System.out.println(hql);

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql, Aeroporto.class);

        return (Aeroporto) query.getSingleResult();
    }


    public void adicionaTerminal(Terminal terminal) {
        terminalAppService.adiciona(terminal);
    }

    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        return terminalAppService.recuperaTerminaisPorAeroporto(aeroporto);
    }

    public List<Aeroporto> recuperaAeroportos() {
        String hql = "SELECT a from Aeroporto a order by a.id";

        System.out.println(hql);

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql, Aeroporto.class);

        return query.getResultList();
    }
}
