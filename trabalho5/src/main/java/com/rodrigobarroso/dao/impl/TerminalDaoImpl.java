package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.models.Terminal;


public abstract class TerminalDaoImpl extends JPADaoGenerico<Terminal, Long> implements TerminalDAO {
    public TerminalDaoImpl() {
        super(Terminal.class);
    }
}