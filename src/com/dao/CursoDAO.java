package com.dao;

import com.core.GenericDAO;
import com.dto.CursoDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class CursoDAO extends GenericDAO<CursoDTO>{
    
    private static final long serialVersionUID = 20014L;   
    private volatile static CursoDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(CursoDAO.class.getName());
    
    public static CursoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (CursoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new CursoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public CursoDTO pesquisarPorDescricao(String cursoDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM CursoDTO curso WHERE curso.descricao = '");
            sql.append(cursoDTO);
            sql.append("'");

            return (CursoDTO) em.createQuery(sql.toString()).getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public CursoDAO() {
        super(CursoDTO.class);
    }      
}
