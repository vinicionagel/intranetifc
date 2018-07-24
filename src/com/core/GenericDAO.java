package com.core;

import com.util.PersistenceManager;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public class GenericDAO<T> implements IGenericDAO<T>, Serializable {

    private static final long serialVersionUID = 424242L;
    
    @SuppressWarnings("rawtypes")
    private final Class typeClass;
    //Transaction tx = null;
    protected EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
    public GenericDAO() {
        typeClass = null;
    }
    
    /**
     * @param argClass
     */
    public GenericDAO(@SuppressWarnings("rawtypes") Class argClass) {
        typeClass = argClass;
    }

    /* (non-Javadoc) 
     * @see com.navita.model.repository.GenericRepository#save(null) 
     */
    @Override
    public boolean save(T instance) {
        EntityManager em = emf.createEntityManager();
        try{
            em.getTransaction().begin();
            em.persist(instance);
            em.getTransaction().commit();
        return true;
        } catch (Exception e) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean remove(T instance) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            instance = em.merge(instance);  
            em.remove(instance);
            em.getTransaction().commit();
            return true;
        } catch (PersistenceException e) {
            return false;
        }finally{
            em.close();
        }
    }

    public boolean update(T instance) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(instance);
        em.getTransaction().commit();
        em.close();
        return true;
    }

    @Override
    public List<T> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT e FROM ");
            builder.append(typeClass.getSimpleName());
            builder.append(" e");
            return em.createQuery(builder.toString()).getResultList();
        } catch (Exception e) {
            Logger.getLogger(GenericDAO.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(long id) {
        EntityManager em = emf.createEntityManager();
        T obj = (T) em.find(typeClass, id);
        em.close();
        return obj;
    }

    @Override
    public boolean unique(T obj) {
        return false;
    }

    @Override
    public List<T> findAll(Integer begin, Integer end) {
        return null;
    }
}