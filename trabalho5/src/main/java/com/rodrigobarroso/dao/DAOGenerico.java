package com.rodrigobarroso.dao;

import com.rodrigobarroso.excecao.ObjectNotFoundException;

public interface DAOGenerico<T, PK> {
    // O DAOGenerico é uma interface que é utilizada para declarar métodos genéricos, que serão implementadas
    // pela classe JPADaoGenerico. Como esse DAO é genérico, ele atende qualquer tipo de classe. 
    T inclui(T obj);

    void altera(T obj);

    void exclui(T obj);

    T getPorId(PK id) throws ObjectNotFoundException;

    T getPorIdComLock(PK id) throws ObjectNotFoundException;
}
