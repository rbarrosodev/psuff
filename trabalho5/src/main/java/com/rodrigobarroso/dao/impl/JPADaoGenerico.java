package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.anotacao.Executar;
import com.rodrigobarroso.dao.DAOGenerico;
import com.rodrigobarroso.excecao.InfrastructureException;
import com.rodrigobarroso.excecao.ObjectNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import java.lang.reflect.Method;

public class JPADaoGenerico<T, PK> implements DAOGenerico<T, PK> {
    private Class<T> tipo;

    public EntityManager em; // Para as subclasses poderem enxergar

    public JPADaoGenerico(Class<T> tipo) {
        this.tipo = tipo;
    }

    @Executar
    public T inclui(T obj) {
        try {
            em.persist(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }

        return obj;
    }


    @Executar
    public void altera(T obj) {
        try {
            em.merge(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    @Executar
    public void exclui(T obj) {
        try {
            em.remove(obj);
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
    }

    @Executar
    public T getPorId(PK id) throws ObjectNotFoundException {
        T tempObj = null;
        try {
            tempObj = em.find(tipo, id);
            
            if(tempObj == null) {
                throw new ObjectNotFoundException();
            }
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
        return tempObj;
    }

    @Override
    public T getPorIdComLock(PK id) throws ObjectNotFoundException {
        T tempObj = null;
        try {
            tempObj = em.find(tipo, id, LockModeType.PESSIMISTIC_WRITE);

            if(tempObj == null) {
                throw new ObjectNotFoundException();
            }
        }
        catch (RuntimeException e) {
            throw new InfrastructureException(e);
        }
        return tempObj;
    }

    public final T busca(Method metodo, Object[] args) throws ObjectNotFoundException {
        T obj = null;
        try {
            String nomeDaBusca = "a"; // getNomeDaBuscaPeloMetodo(metodo);

            // SELECT a from Aeroporto a
            // etc etc etc
            // bla bla bla sl sl oq

            // voltar na p4 8 min
        }
    }
}
