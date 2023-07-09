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

public abstract class AeroportoDaoImpl extends JPADaoGenerico<Aeroporto, Long> implements AeroportoDAO {
    public AeroportoDaoImpl() {
        super(Aeroporto.class);
    }
}
