package com.dao;

import com.core.GenericDAO;
import com.dto.FabricanteDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class FabricanteDAO extends GenericDAO<FabricanteDTO> {

    private static final long serialVersionUID = 1007L;    
    private volatile static FabricanteDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(FabricanteDAO.class.getName());
    
    public FabricanteDAO() {
        super(FabricanteDTO.class);
    }    
    
    public static FabricanteDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (FabricanteDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new FabricanteDAO();
                }
            }
        }
        return uniqueInstance;
    }  

    @Override
    public boolean unique(FabricanteDTO fabricanteDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM FabricanteDTO fabricante WHERE fabricante.descricao = '");
            sql.append(fabricanteDTO.getDescricao());
            sql.append("'");

            return !em.createQuery(sql.toString()).getResultList().isEmpty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<FabricanteDTO> pesquisarNome(String fabricanteDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM FabricanteDTO fabricante WHERE LOWER (fabricante.descricao) LIKE '%");
            sql.append(fabricanteDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY fabricante.descricao");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<FabricanteDTO> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM FabricanteDTO fabricante ORDER BY fabricante.descricao");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}