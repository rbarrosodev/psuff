package com.rodrigobarroso.dao.impl;

import com.rodrigobarroso.dao.AeroportoDAO;
import com.rodrigobarroso.dao.JPATerminalDAO;
import com.rodrigobarroso.dao.TerminalDAO;
import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.util.List;

public class AeroportoDaoImpl implements AeroportoDAO {

    public void adiciona(Aeroporto aeroporto) {
//        EntityManager entmanager = null;
//        EntityTransaction transaction = null;
//
//        try {
//            entmanager = EntityManagerGenerator.generate();
//            transaction = entmanager.getTransaction();
//
//            transaction.begin();
//            entmanager.persist(aeroporto);
//            transaction.commit();
//        }
//        catch(RuntimeException e) {
//            if (transaction != null) {
//                try {
//                    transaction.rollback();
//                }
//                catch(RuntimeException ex) { }
//            }
//            throw e;
//        } finally {
//            entmanager.close();
//        }

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try (sessionFactory; session) {
            session.save(aeroporto);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public void altera(Aeroporto aeroporto) {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try (sessionFactory; session) {
            Aeroporto aero = session.get(Aeroporto.class, aeroporto.getId());
            if (aero != null) {
                session.merge(aeroporto);
                transaction.commit();
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }


    public void deleta(Aeroporto aeroporto) {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try (sessionFactory; session) {
            Aeroporto aero = session.get(Aeroporto.class, aeroporto.getId());
            if (aero != null) {
                session.delete(aero);
                transaction.commit();
            }
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

//        EntityManager entmanager = null;
//        EntityTransaction transaction = null;
//        String query = "SELECT aero from Aeroporto aero WHERE aero.codigo = '%s'".formatted(codigo);
//
//        try {
//            entmanager = EntityManagerGenerator.generate();
//            transaction = entmanager.getTransaction();
//            transaction.begin();
//
//            @SuppressWarnings("unchecked")
//            List<Aeroporto> aeroportos = entmanager
//                    .createQuery(query)
//                    .getResultList();
//
//            if(aeroportos.isEmpty()) {
//                throw new AeroportoNotFoundException("Aeroporto não encontrado!");
//            }
//
//            entmanager.remove(aeroportos.get(0));
//            transaction.commit();
//        }
//        catch(RuntimeException e) {
//            if (transaction != null) {
//                transaction.rollback();
//            }
//            throw e;
//        }
//        finally {
//            entmanager.close();
//        }
    }


    @Override
    public void adicionaTerminal(Terminal terminal) {
        TerminalDAO terminalDAO = new JPATerminalDAO();
        terminalDAO.adiciona(terminal);
    }

    @Override
    public List<Terminal> recuperaTerminais(Aeroporto aeroporto) {
        TerminalDAO terminalDAO = new JPATerminalDAO();
        return terminalDAO.recuperaTerminaisPorAeroporto(aeroporto);
    }

    public Aeroporto recuperaAeroporto(String codigo) {
        String hql = "SELECT a from Aeroporto a where a.codigo = '%s'".formatted(codigo);

        System.out.println(hql);

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql, Aeroporto.class);

        return (Aeroporto) query.getSingleResult();


//        EntityManager entmanager = null;
//        String query = "SELECT aero from Aeroporto aero WHERE aero.codigo = '%s'".formatted(codigo);
//
//        try
//        {
//            entmanager = EntityManagerGenerator.generate();
//            @SuppressWarnings("unchecked")
//            List<Aeroporto> aeroportos = entmanager
//                    .createQuery(query)
//                    .getResultList();
//
//            if(aeroportos.isEmpty()) {
//                throw new AeroportoNotFoundException("Aeroporto não encontrado!");
//            }
//
//            return aeroportos.get(0);
//        }
//        finally {
//            entmanager.close();
//        }
    }

    public List<Aeroporto> recuperaAeroportos() {
        String hql = "SELECT a from Aeroporto a order by a.id";

        System.out.println(hql);

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql, Aeroporto.class);

        return query.getResultList();
    }




//        EntityManager entmanager = null;
//
//        try {
//            entmanager = EntityManagerGenerator.generate();
//
//            @SuppressWarnings("unchecked")
//            List<Aeroporto> aeroportos = entmanager
//                                            .createQuery("SELECT aero from Aeroporto aero order by aero.id")
//                                            .getResultList();
//
//            return aeroportos;
//        }
//        finally {
//            entmanager.close();
//        }
    }
