package com.dao;

import com.core.GenericDAO;
import com.dto.AndamentoOcorrenciaDTO;
import com.dto.OcorrenciaDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class AndamentoOcorrenciaDAO extends GenericDAO<AndamentoOcorrenciaDTO>{
    
    private static final long serialVersionUID = 20012L;   
    private volatile static AndamentoOcorrenciaDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(AndamentoOcorrenciaDAO.class.getName());
    
    public static AndamentoOcorrenciaDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (AndamentoOcorrenciaDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new AndamentoOcorrenciaDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public AndamentoOcorrenciaDAO() {
        super(AndamentoOcorrenciaDTO.class);
    }     
    
    public List<AndamentoOcorrenciaDTO> pesquisarAndamentosDaOcorrencia(OcorrenciaDTO ocorrenciaDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM AndamentoOcorrenciaDTO anda WHERE anda.ocorrenciaDTO.codigo = ");
            sql.append(ocorrenciaDTO.getCodigo());
            sql.append(" ORDER BY anda.codigo");
            
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {            
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}
