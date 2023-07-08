package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.excecao.OutdatedEntityException;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.excecao.AirportNotFoundException;
import com.rodrigobarroso.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import java.util.List;

public class JPAAeroportoDAO implements AeroportoDAO {

    public void adiciona(Aeroporto aeroporto) {
        EntityManager em = JPAUtil.getEntityManager();
        em.persist(aeroporto); // Salva o aeroporto
    }

    @Override
    public void altera(Aeroporto aeroporto) throws AirportNotFoundException, OutdatedEntityException {
        Aeroporto aero = null;

        try {
            EntityManager em = JPAUtil.getEntityManager();
            aero = em.find(Aeroporto.class, aeroporto.getId(), LockModeType.PESSIMISTIC_WRITE);

            if (aero == null) {
                throw new AirportNotFoundException("Aeroporto não encontrado.");
            }
            // O merge entre nada e tudo é tudo. Ao tentar alterar um produto deletado ele será re-inserido
            // no banco de dados.
            em.merge(aeroporto);

        } catch (OptimisticLockException e) {
            throw new OutdatedEntityException("Esse aeroporto já foi atualizado por outra pessoa");
        }
        catch (RuntimeException e) {
            throw new AirportNotFoundException(e.getMessage());
        }
    }


    public void deleta(Aeroporto aeroporto) throws AirportNotFoundException {
        try {
            EntityManager em = JPAUtil.getEntityManager();

            Aeroporto aero = em.find(Aeroporto.class, aeroporto.getId());

            if (aero == null) {
                throw new AirportNotFoundException("Aeroporto não encontrado");
            }

            em.remove(aero);
        }
        catch (RuntimeException e) {
            throw new AirportNotFoundException(e.getMessage());
        }
    }


    @Override
    public void adicionaTerminal(Terminal terminal) {
        TerminalDAO terminalDAO = new JPATerminalDAO();
        terminalDAO.adiciona(terminal);
    }

    @Override
    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        TerminalDAO terminalDAO = new JPATerminalDAO();
        return terminalDAO.recuperaTerminaisPorAeroporto(aeroporto);
    }

    public Aeroporto recuperaAeroporto(String codigo) throws AirportNotFoundException {
        EntityManager em = JPAUtil.getEntityManager();
        String queryText = "SELECT a from Aeroporto a where a.codigo = '%s'".formatted(codigo);

        Aeroporto aero = em.createQuery(queryText, Aeroporto.class).getSingleResult();

        // createQuery(): Utilizado para passar uma query da informação pedida
        // Nesse caso, foi passado uma query SELECT da table Aeroporto onde o código
        // do aeroporto tem que ser o pedido anteriormente. Além disso, é necessário
        // também passar o .class da classe referenciada nessa query, nesse caso Aeroporto.
        // Após isso, é pedido o único resultado de todos os registros com esse código, e
        // como o campo código é UNIQUE, temos a garantia que só será retornado apenas um
        // objeto mesmo, que será o objeto Aeroporto que tem o código requisitado.

        // Características no método find():
        // 1. É genérico: não requer um cast.
        // 2. Retorna null caso a linha não seja encontrada no banco.

        if (aero == null) {
            throw new AirportNotFoundException("Aeroporto não encontrado");
        }

        return aero;
    }

    public List<Aeroporto> recuperaAeroportos() {
        EntityManager em = JPAUtil.getEntityManager();
        // Retorna um List vazio caso a tabela correspondente esteja vazia.

        return em
                .createQuery("select a from Aeroporto a order by a.id", Aeroporto.class)
                .getResultList();
    }
}
