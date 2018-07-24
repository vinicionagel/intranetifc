

package com.dao;

import com.core.GenericDAO;
import static com.dao.AndamentoDAO.logger;
import com.dto.AgendamentoDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class AgendamentoDAO extends GenericDAO<AgendamentoDTO>{
    
    private static final long serialVersionUID = 20018L;   
    private volatile static AgendamentoDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(AgendamentoDAO.class.getName());
    
    public static AgendamentoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (AgendamentoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new AgendamentoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public AgendamentoDAO() {
        super(AgendamentoDTO.class);
    }  
    
    public List<AgendamentoDTO> findAllOrdenadoPorDia(){
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM AgendamentoDTO a ORDER BY a.diaSemanaDTO.codigo, a.horario").getResultList();
        } catch (Exception e) {  
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}