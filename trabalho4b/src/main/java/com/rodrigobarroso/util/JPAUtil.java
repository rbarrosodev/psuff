package com.rodrigobarroso.util;

import com.rodrigobarroso.excecao.InfrastructureException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JPAUtil {
    private static final Logger logger = LoggerFactory.getLogger(JPAUtil.class);
    private static JPAUtil jpaUtil = null;
    private final EntityManagerFactory entityManagerFactory;
    private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<>();
    private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<>();
    private static final ThreadLocal<Integer> threadTransactionCount = new ThreadLocal<>();

    private JPAUtil() {
        entityManagerFactory = Persistence.createEntityManagerFactory("trabalho4b");
    }

    public static void beginTransaction() {
        // Tenta pegar a transação atual do thread corrente
        EntityTransaction transaction = threadTransaction.get();

        // Tenta pegar a quantidade de "transações" abertas que existem no thread corrente
        // Só vai existir no máximo uma transação em aberto no thread atual, porem utilizamos essa variavel para saber quantas vezes foram pedidos para abrir uma transação.
        // Pois usaremos ela para saber qual é o momento certo de fechar a transação,
        // já que no momento em que formos fechar a transação só podemos fechar ela se essa for a transação mais externa,
        // ou seja, a primeira transação, a que realmente foi aberta.
        Integer transactionCount = threadTransactionCount.get();
        // System.out.println("Valor da thread TransactionCount no início de beginTransaction: " + transactionCount);

        // Se não existir transações, ou seja, se for a primeira transação
        // então deve ser aberto uma transação e o valor da transaçãoCount deve ser setada como 1
        if (transaction == null) {
            try {
                transaction = getEntityManager().getTransaction(); // Pega a transação do entity manager
                transaction.begin(); // Inicia a transação
                transactionCount = 1; // Seta a transaçãoCount como 1
                threadTransactionCount.set(transactionCount); // Setando a transaçãoCount no thread atual
                threadTransaction.set(transaction); // Setando a transação no thread atual
                logger.info(">>>> Criou a transação");
            }
            catch (RuntimeException ex) {
                throw new InfrastructureException(ex);
            }
        }
        // Se já existir uma transação, então deve ser incrementado o valor da transaçãoCount
        else {
            transactionCount++;
            threadTransactionCount.set(transactionCount); // Setando a transaçãoCount no thread atual
        }

        // System.out.println("Valor da thread TransactionCount no final de beginTransaction: " + transactionCount);
    }

    public static EntityManager getEntityManager() {
        EntityManager entityManager;

        try {
            // Verifica se o JPAUtil já foi instanciado
            if (jpaUtil == null) {
                jpaUtil = new JPAUtil();
            }
            entityManager = threadEntityManager.get(); // Pega o EntityManager do thread atual

            // Se não existir um EntityManager no thread atual, então deve ser criado um novo e setado no thread atual
            if (entityManager == null) {
                entityManager = jpaUtil.entityManagerFactory.createEntityManager();
                threadEntityManager.set(entityManager);
                logger.info(">>>> Criou o entity manager");
            }
        }
        catch (RuntimeException ex) {
            throw new InfrastructureException(ex);
        }

        return entityManager;
    }

    public static void commitTransaction() {
        EntityTransaction transaction = threadTransaction.get();
        Integer transactionCount = threadTransactionCount.get();

        // System.out.println("Valor da thread TransactionCount no início de commitTransaction: " + transactionCount);

        try {
            // Verifica se existe uma transação e se ela está ativa (commit ou fechada...)
            if (transaction != null && transaction.isActive()) {
                // Como foi chamado o método commitTransaction, então a quantidade de transações foi decrementada
                transactionCount--;

                // Se a quantidade for igual a 0, então esse é o último commitTransaction que será chamado
                // logo deve realmente commitar, por não ter nenhum outro commit que será chamado
                if (transactionCount == 0) {
                    transaction.commit(); // Commita a transação
                    threadTransaction.set(null); // Remove a transação da thread
                    threadTransactionCount.set(null); // Remove a transaçãoCount da thread
                    logger.info(">>>> Comitou a transação");
                }
                // Se a quantidade for diferente de 0, então esse não é o último commitTransaction que será chamado
                // Logo, não deve commitar, pois ainda existem outros commits que serão chamados, somente salvar a quantidade decrementada
                else {
                    threadTransactionCount.set(transactionCount);
                }
            }
        }
        catch (RuntimeException exception) {
            try {
                rollbackTransaction(); // Caso de um erro, tem o rollback da transação
            } catch (RuntimeException innerException) {
                throw new InfrastructureException(innerException);
            }
            throw new InfrastructureException(exception);
        }

        // System.out.println("Valor da thread TransactionCount no final de commitTransaction: " + transactionCount);
    }

    public static void rollbackTransaction() {
        EntityTransaction transaction = threadTransaction.get();

        // System.out.println("Valor da thread TransactionCount no início de rollbackTransaction: " + threadTransactionCount);

        try {
            // Pega a transação da thread corrente
            // se ela existir e tiver ativa, ele realiza o rollback
            threadTransaction.set(null);
            threadTransactionCount.set(null);
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
                logger.info(">>>> Deu rollback na transação");
            }
        }
        catch (RuntimeException ex) {
            throw new InfrastructureException(ex);
        }
        finally {
            closeEntityManager();
        }

        // System.out.println("Valor da thread TransactionCount no final de rollbackTransaction: " + threadTransactionCount);
    }

    public static void closeEntityManager() {
        EntityTransaction transaction = threadTransaction.get();
        Integer transactionCount = threadTransactionCount.get();

        // System.out.println("Valor da thread TransactionCount no inicio do close EM: " + transactionCount);

        // O entityManager só pode ser fechado caso não tenha nenhuma transação em aberto no thread corrente
        if (transactionCount == null || transactionCount == 0) {

            // Caso o transactionCount seja diferente de 0, então deve ser feito o rollback da transação
            if (transaction != null && transaction.isActive()) {
                rollbackTransaction();
            }

            EntityManager entityManager = threadEntityManager.get();

            threadEntityManager.set(null);
            threadTransactionCount.set(null);

            try {
                if (entityManager != null && entityManager.isOpen()) {
                    entityManager.close();
                    logger.info(">>>> Fechou o entity manager");
                }
            }
            catch (RuntimeException ex) {
                throw new InfrastructureException(ex);
            }
        }
        // System.out.println("Valor da thread TransactionCount no final do close EM: " + transactionCount);
    }
}