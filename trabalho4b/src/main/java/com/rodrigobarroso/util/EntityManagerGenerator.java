package com.rodrigobarroso.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerGenerator {
    private static EntityManagerGenerator factory = null;

    private EntityManagerFactory emf = null;

    private EntityManagerGenerator() {
        try {
            emf = Persistence.createEntityManagerFactory("trabalho_1");
        }
        catch(Throwable e) {
            e.printStackTrace();
            System.out.println("Mensagem de erro: " + e.getMessage());
        }
    }

    public static EntityManager generate() {
        if (factory == null) {
            factory = new EntityManagerGenerator();
        }

        return factory.emf.createEntityManager();
    }
}
