package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.models.Aeroporto;
import org.springframework.stereotype.Repository;

@Repository
public abstract class AeroportoDaoImpl extends JPADaoGenerico<Aeroporto, Long> implements AeroportoDAO {
    public AeroportoDaoImpl() {
        super(Aeroporto.class);
    }

    // AeroportoDaoImpl extende os métodos de JPADaoGenerico<Aeroporto,Long> trazendo seus métodos
    // para si, e também implementa os métodos não genéricos de AeroportoDAO, como 'recuperaUmAeroporto'.
}