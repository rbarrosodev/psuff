package com.rodrigobarroso.dao;

import com.rodrigobarroso.excecao.ObjectNotFoundException;

public interface DAOGenerico<T, PK> {
    T inclui(T obj);

    void altera(T obj);

    void exclui(T obj);

    T getPorId(PK id) throws ObjectNotFoundException;

    T getPorIdComLock(PK id) throws ObjectNotFoundException;
}
