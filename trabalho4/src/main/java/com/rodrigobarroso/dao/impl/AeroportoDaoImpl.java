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
