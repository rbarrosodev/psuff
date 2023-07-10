package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.models.Terminal;
import org.springframework.stereotype.Repository;

@Repository
public abstract class TerminalDaoImpl extends JPADaoGenerico<Terminal, Long> implements TerminalDAO {
    public TerminalDaoImpl() {
        super(Terminal.class);
    }

    // TerminalDaoImpl extende os métodos de JPADaoGenerico<Terminal,Long> trazendo seus métodos
    // para si, e também implementa os métodos não genéricos de TerminalDAO.
}