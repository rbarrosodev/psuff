package com.rodrigobarroso.em.impl;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

import java.util.List;
import java.util.Map;

public class ProxyEntityManagerImpl implements EntityManager {
    @com.rodrigobarroso.anotacao.PersistenceContext
    protected EntityManager em;


    @Override
    public void persist(Object o) {
        em.persist(o);
    }

    @Override
    public <T> T merge(T t) {
        return em.merge(t);
    }

    @Override
    public void remove(Object o) {
        em.remove(o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o) {
        return em.find(aClass, o);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
        return em.find(aClass, o, map);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
        return em.find(aClass, o, lockModeType);
    }

    @Override
    public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
        return em.find(aClass, o, lockModeType, map);
    }

    @Override
    public <T> T getReference(Class<T> aClass, Object o) {
        return em.getReference(aClass, o);
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void setFlushMode(FlushModeType flushModeType) {
        em.setFlushMode(flushModeType);
    }

    @Override
    public FlushModeType getFlushMode() {
        return em.getFlushMode();
    }

    @Override
    public void lock(Object o, LockModeType lockModeType) {
        em.lock(o, lockModeType);
    }

    @Override
    public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {
        em.lock(o, lockModeType, map);
    }

    @Override
    public void refresh(Object o) {
        em.refresh(o);
    }

    @Override
    public void refresh(Object o, Map<String, Object> map) {
        em.refresh(o, map);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType) {
        em.refresh(o, lockModeType);
    }

    @Override
    public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {
        em.refresh(o, lockModeType, map);
    }

    @Override
    public void clear() {
        em.clear();
    }

    @Override
    public void detach(Object o) {
        em.detach(o);
    }

    @Override
    public boolean contains(Object o) {
        return em.contains(o);
    }

    @Override
    public LockModeType getLockMode(Object o) {
        return em.getLockMode(o);
    }

    @Override
    public void setProperty(String s, Object o) {
        em.setProperty(s, o);
    }

    @Override
    public Map<String, Object> getProperties() {
        return em.getProperties();
    }

    @Override
    public Query createQuery(String s) {
        return em.createQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        return em.createQuery(criteriaQuery);
    }

    @Override
    public Query createQuery(CriteriaUpdate criteriaUpdate) {
        return em.createQuery(criteriaUpdate);
    }

    @Override
    public Query createQuery(CriteriaDelete criteriaDelete) {
        return em.createQuery(criteriaDelete);
    }

    @Override
    public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
        return em.createQuery(s, aClass);
    }

    @Override
    public Query createNamedQuery(String s) {
        return em.createNamedQuery(s);
    }

    @Override
    public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
        return em.createNamedQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s) {
        return em.createNativeQuery(s);
    }

    @Override
    public Query createNativeQuery(String s, Class aClass) {
        return em.createNativeQuery(s, aClass);
    }

    @Override
    public Query createNativeQuery(String s, String s1) {
        return em.createNativeQuery(s, s1);
    }

    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
        return em.createNamedStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s) {
        return em.createStoredProcedureQuery(s);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
        return em.createStoredProcedureQuery(s, classes);
    }

    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
        return em.createStoredProcedureQuery(s, strings);
    }

    @Override
    public void joinTransaction() {
        em.joinTransaction();
    }

    @Override
    public boolean isJoinedToTransaction() {
        return em.isJoinedToTransaction();
    }

    @Override
    public <T> T unwrap(Class<T> aClass) {
        return em.unwrap(aClass);
    }

    @Override
    public Object getDelegate() {
        return em.getDelegate();
    }

    @Override
    public void close() {
        em.close();
    }

    @Override
    public boolean isOpen() {
        return em.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return em.getTransaction();
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return em.getEntityManagerFactory();
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return em.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return em.getMetamodel();
    }

    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
        return em.createEntityGraph(aClass);
    }

    @Override
    public EntityGraph<?> createEntityGraph(String s) {
        return em.createEntityGraph(s);
    }

    @Override
    public EntityGraph<?> getEntityGraph(String s) {
        return em.getEntityGraph(s);
    }

    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
        return em.getEntityGraphs(aClass);
    }
}