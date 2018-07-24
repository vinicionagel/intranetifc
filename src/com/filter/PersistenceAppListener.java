/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.filter;

import com.util.PersistenceManager;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 *
 * @author vinicio
 */
@WebFilter(urlPatterns={"/*"})
public class PersistenceAppListener implements Filter {

    private EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();

    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            // realiza as ações do sistema filterChain.doFilter(request, response);
            filterChain.doFilter(request, response);
            
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }


    @Override
    public void destroy() {
         PersistenceManager.getInstance().closeEntityManagerFactory();
    }
}
