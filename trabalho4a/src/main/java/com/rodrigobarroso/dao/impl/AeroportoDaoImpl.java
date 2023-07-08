package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.anotacao.PersistenceContext;
import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import com.rodrigobarroso.util.AeroportoNotFoundException;
import com.rodrigobarroso.util.InfraestruturaException;


import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;
import java.util.List;

public class AeroportoDaoImpl implements AeroportoDAO {
    @PersistenceContext
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


    public void adiciona(Aeroporto aeroporto) {
        try {
            em.persist(aeroporto);
        } catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        }
    }

    @Override
    public void altera(Aeroporto aeroporto) {
        try {
            Aeroporto aero = em.find(Aeroporto.class, aeroporto.getId(), LockModeType.PESSIMISTIC_WRITE);

            if (aero == null) {
                throw new AeroportoNotFoundException();
            }

            em.merge(aeroporto);
        }
        catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        } catch (AeroportoNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleta(String codigoAero) {
        try {
            Aeroporto aeroporto = recuperaAeroporto(codigoAero);

            Aeroporto aero = em.find(Aeroporto.class, aeroporto.getId(), LockModeType.PESSIMISTIC_WRITE);

            if (aero == null) {
                throw new AeroportoNotFoundException();
            }

            em.remove(aeroporto);
        }
        catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        } catch (AeroportoNotFoundException e) {
            throw new RuntimeException(e);
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
                throw new AeroportoNotFoundException();
            }

            return aero;
        }
        catch (RuntimeException e) {
            throw new InfraestruturaException(e);
        } catch (AeroportoNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Aeroporto> recuperaAeroportos() {
        try {
            return em.createQuery("SELECT a from Aeroporto a order by a.id asc", Aeroporto.class).getResultList();
        }
        catch (RuntimeException e) {
            throw new InfraestruturaException(e);
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
