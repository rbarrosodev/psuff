package com.rodrigobarroso.dao.controle;

import com.rodrigobarroso.dao.impl.AeroportoDaoImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FabricaDeDAO {

    @Bean
    public static AeroportoDaoImpl getAeroportoDao() {
        return getDao(com.rodrigobarroso.dao.impl.AeroportoDaoImpl.class);
    }

    public static <T> T getDao(Class<T> classeDoDao) {
        return classeDoDao.cast(Enhancer.create(classeDoDao, new InterceptadorDeDAO()));
    }
}