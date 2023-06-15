package com.rodrigobarroso.dao;

import com.rodrigobarroso.models.Aeroporto;
import com.rodrigobarroso.models.Terminal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;
import java.util.List;

public class JPATerminalDAO implements TerminalDAO {
    @Override
    public void adiciona(Terminal terminal) {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try (sessionFactory; session) {
            session.save(terminal);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    public List<Terminal> recuperaTerminaisPorAeroporto(Aeroporto aero) {
        String hql = String.format("SELECT t from Terminal t where t.aeroporto.id = %s", aero.getId());

        System.out.println(hql);

        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        Session session = sessionFactory.openSession();

        Query query = session.createQuery(hql, Terminal.class);

        return query.getResultList();
    }
}
