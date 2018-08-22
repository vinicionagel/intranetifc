package com.dao;

import com.core.GenericDAO;
import com.dto.StatusDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class StatusDAO extends GenericDAO<StatusDTO> {

    private static final long serialVersionUID = 1021L;
    static final Logger logger = Logger.getLogger(StatusDAO.class.getName());
    private volatile static StatusDAO uniqueInstance;
    
    public static StatusDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (StatusDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new StatusDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    

    public StatusDAO() {
        super(StatusDTO.class);
    }
    
    public StatusDTO pesquisarDescricao(String statusDTO)  {
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT status FROM StatusDTO status WHERE status.descricao = '");
            sql.append(statusDTO);
            sql.append("'");
            
            return (StatusDTO) em.createQuery(sql.toString()).getSingleResult();            
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;            
        } finally {
            em.close();
        }
    }
    
    public List<StatusDTO> pesquisarNome(StatusDTO statusDTO)  {
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();
            sql.append("FROM StatusDTO as st WHERE LOWER (st.descricao) LIKE '%");
            sql.append(statusDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("%'");
            
            return em.createQuery(sql.toString()).getResultList();            
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;            
        } finally {
            em.close();
        }
    }
}