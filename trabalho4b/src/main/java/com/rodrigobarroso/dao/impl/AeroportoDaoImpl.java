package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.anotacao.Autowired;
import com.rodrigobarroso.anotacao.PersistenceContext;
import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.excecao.AirportNotFoundException;
import com.rodrigobarroso.excecao.InfrastructureException;


import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class AeroportoDaoImpl implements AeroportoDAO {
    @Autowired
    protected EntityManager em;
    // A variável de instância 'em' de tipo EntityManager está anotada com a anotação @PersistenceContext
    // No Trabalho 4A, será criado um objeto dessa classe de serviço (exemplo AeroportoAppServiceImpl)
    // para cada sessão de usuário.
    // No Trabalho 4B, será criado para cada classe de serviço, um ÚNICO objeto dessa classe de serviço
    // (exemplo AeroportoAppServiceImpl) para todos os usuários que estiverem interagindo com o projeto.
    // No 4B, como esse único objeto de serviço vai referenciar o DAO abaixo, onde está definido a variável
    // de instância 'em' (EntityManager), se utilizarmos a mesma implementação do 4A, quando o InterceptadorDeDAO
    // para um determinado usuário atualizar o EntityManager para o EntityManager da thread corrente, todos
    // os outros usuários utilizaram esse mesmo EntityManager, uma vez que na memória só existirá uma única
    // instância do DAO onde está definido o EntityManager como dito acima.

    public AeroportoDaoImpl() {
    }

    public void adiciona(Aeroporto aeroporto) {
        try {
            em.persist(aeroporto);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    @Override
    public void altera(Aeroporto aeroporto) {
        try {
            Aeroporto aero = em.find(Aeroporto.class, aeroporto.getId(), LockModeType.PESSIMISTIC_WRITE);

            if (aero == null) {
                throw new AirportNotFoundException();
            }

            em.merge(aeroporto);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        } catch (AirportNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleta(Aeroporto aeroporto) throws AirportNotFoundException {
        try {
            Aeroporto aero = em.find(Aeroporto.class, aeroporto.getId(), LockModeType.PESSIMISTIC_WRITE);

            if (aero == null) {
                throw new AirportNotFoundException();
            }

            em.remove(aeroporto);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }


    public Aeroporto recuperaAeroporto(String codigo) {
        try {
            String jpql = "SELECT a FROM Aeroporto a WHERE a.codigo = :codigo";
            TypedQuery<Aeroporto> query = em.createQuery(jpql, Aeroporto.class);
            query.setParameter("codigo", codigo);
            List<Aeroporto> resultList = query.getResultList();

            Aeroporto aero = null;
            if (resultList != null) {
                aero = em.find(Aeroporto.class, resultList.get(0).getId());
            }

            if (aero == null) {
                throw new AirportNotFoundException();
            }

            return aero;
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        } catch (AirportNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Aeroporto> recuperaAeroportos() {
        try {
            return em.createQuery("SELECT a from Aeroporto a order by a.id asc", Aeroporto.class).getResultList();
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    @Override
    public void adicionaTerminal(Terminal terminal) {

    }

    @Override
    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        return null;
    }
}
