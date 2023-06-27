package com.rodrigobarroso.dao;

import com.rodrigobarroso.dao.impl.AeroportoDaoImpl;
import com.rodrigobarroso.dao.impl.TerminalDaoImpl;

public class FabricaDeDAO {
    private static final AeroportoDAO AIRPORT_DAO = new AeroportoDaoImpl();
    private static final TerminalDAO TERMINAL_DAO = new TerminalDaoImpl();

    public static <T> T getDao(Class<T> classDao) {
        if(classDao.equals(AeroportoDAO.class)) {
            return classDao.cast(AIRPORT_DAO);
        }

        if(classDao.equals(TerminalDAO.class)) {
            return classDao.cast(TERMINAL_DAO);
        }

        return null;
    }

}
