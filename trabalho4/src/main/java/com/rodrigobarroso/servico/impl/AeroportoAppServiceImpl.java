package com.rodrigobarroso.servico.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.util.AeroportoNotFoundException;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.servico.AeroportoAppService;

import com.rodrigobarroso.dao.JPATerminalDAO;
import com.rodrigobarroso.models.Terminal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;

public class AeroportoAppServiceImpl implements AeroportoAppService {

    @Autowired
    private AeroportoDAO aeroportoDAO;

    @Transactional
    public void adiciona(Aeroporto aeroporto) {
        aeroportoDAO.adiciona(aeroporto);
    }

    @Transactional
    public void altera(Aeroporto aeroporto) throws AeroportoNotFoundException {
        try {

            aeroportoDAO.altera(aeroporto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Transactional
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
        TerminalDAO terminalDAO = new JPATerminalDAO();
        terminalDAO.adiciona(terminal);
    }

    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        TerminalDAO terminalDAO = new JPATerminalDAO();
        return terminalDAO.recuperaTerminaisPorAeroporto(aeroporto);
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
