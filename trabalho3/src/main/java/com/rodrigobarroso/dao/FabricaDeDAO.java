package com.rodrigobarroso.dao;

import com.rodrigobarroso.dao.impl.JPAAeroportoDAO;
import com.rodrigobarroso.dao.impl.JPATerminalDAO;

public class FabricaDeDAO {
    private static final AeroportoDAO AIRPORT_DAO = new JPAAeroportoDAO();
    private static final TerminalDAO TERMINAL_DAO = new JPATerminalDAO();

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
