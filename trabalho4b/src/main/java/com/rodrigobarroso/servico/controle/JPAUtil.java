package com.rodrigobarroso.servico.controle;

import com.rodrigobarroso.util.InfraestruturaException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JPAUtil {
    private static EntityManagerFactory emf;

    private static JPAUtil jpaUtil;
    private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();
    private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<EntityTransaction>();

    private JPAUtil() {
        try {
            emf = Persistence.createEntityManagerFactory("trabalho4");
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println(">>>>>>>>>> Mensagem de erro: " + e.getMessage());
            throw e;
        }
    }

    public static void beginTransaction() {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> Vai criar transação");

        EntityTransaction tx = threadTransaction.get();
        try {
            if (tx == null) {
                tx = getEntityManager().getTransaction();
                tx.begin();
                threadTransaction.set(tx);
            }
        }
        catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }

    public static EntityManager getEntityManager() {
        EntityManager entManager;
        // Abre uma nova Sessão, se a thread ainda não possui uma.
        try {
            if (jpaUtil == null) {
                jpaUtil = new JPAUtil();
            }
            entManager = threadEntityManager.get();
            if (entManager == null) {
                entManager = emf.createEntityManager();
                threadEntityManager.set(entManager);
            }
        }
        catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
        return entManager;
    }

    public static void commitTransaction() {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> Vai comitar transação");

        EntityTransaction tx = threadTransaction.get();
        try {
            if (tx != null && tx.isActive()) {
                tx.commit();
            }
            threadTransaction.set(null);
        } catch (RuntimeException ex) {
            try {
                rollbackTransaction();
            }
            catch (RuntimeException ignored) {
            }

            throw new InfraestruturaException(ex);
        }
    }

    public static void rollbackTransaction() {

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> Vai efetuar rollback da transação");

        System.out.println("Vai efetuar rollback de transacao");

        EntityTransaction tx = threadTransaction.get();
        try {
            threadTransaction.set(null);
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        } finally {
            closeEntityManager();
        }
    }

    public static void closeEntityManager() { // System.out.println("Vai fechar sessão");

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> Vai fechar o entity manager");

        try {
            EntityManager entManager = threadEntityManager.get();
            threadEntityManager.set(null);
            if (entManager != null && entManager.isOpen()) {
                entManager.close();
                // System.out.println("Fechou a sessão");
            }

            EntityTransaction tx = threadTransaction.get();
            if (tx != null && tx.isActive()) {
                rollbackTransaction();
                throw new RuntimeException("EntityManager sendo fechado " + "com transação ativa.");
            }
        } catch (RuntimeException ex) {
            throw new InfraestruturaException(ex);
        }
    }
}
