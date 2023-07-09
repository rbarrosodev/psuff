package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.models.Aeroporto;
import org.springframework.stereotype.Repository;

@Repository
public abstract class AeroportoDaoImpl extends JPADaoGenerico<Aeroporto, Long> implements AeroportoDAO {
    public AeroportoDaoImpl() {
        super(Aeroporto.class);
    }
}
