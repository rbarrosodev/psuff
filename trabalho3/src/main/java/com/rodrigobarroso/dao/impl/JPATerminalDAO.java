package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.List;

public class JPATerminalDAO implements TerminalDAO {
    @Override
    public void adiciona(Terminal terminal) {
        EntityManager em = JPAUtil.getEntityManager();
        em.persist(terminal); // Salva o terminal
    }

    public List<Terminal> recuperaTerminaisPorAeroporto(Aeroporto aero) {
        String queryText = String.format("SELECT t from Terminal t where t.aeroporto.id = %s", aero.getId());

        EntityManager em = JPAUtil.getEntityManager();
        // Retorna um List vazio caso a tabela correspondente esteja vazia.

        return em
                .createQuery(queryText, Terminal.class)
                .getResultList();
    }
}
