/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author vinicio
 */
public class PersistenceManager {

    public static final boolean DEBUG = true;
    private static final PersistenceManager singleton = new PersistenceManager();
    protected EntityManagerFactory emf;

    public static PersistenceManager getInstance() {
        return singleton;
    }

    private PersistenceManager() {
    }

    public EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            createEntityManagerFactory();
        }
        return emf;
    }

    public void closeEntityManagerFactory() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }

    protected void createEntityManagerFactory() {
        this.emf = Persistence.createEntityManagerFactory("IntranetIFC");
    }
}
