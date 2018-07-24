package com.dao;

import com.core.GenericDAO;
import com.dto.AndamentoDTO;
import com.dto.ChamadoDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class AndamentoDAO extends GenericDAO<AndamentoDTO> {

    private static final long serialVersionUID = 1001L;
    static final Logger logger = Logger.getLogger(AndamentoDAO.class.getName());
    private volatile static AndamentoDAO uniqueInstance;
    
    public static AndamentoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (AndamentoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new AndamentoDAO();
                }
            }
        }
        return uniqueInstance;
    }    
    
    public AndamentoDAO() {
        super(AndamentoDTO.class);
    }
    
    public List<AndamentoDTO> pesquisarNome(AndamentoDTO andamentoDTO) {
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();
            sql.append("FROM AndamentoDTO as st where st.descricao LIKE  '");
            sql.append(andamentoDTO.getDescricao());
            sql.append("%' ORDER BY DESC st.descricao");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {  
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<AndamentoDTO> pesquisarAndamentosDaChamada(ChamadoDTO chamadoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM AndamentoDTO as anda WHERE anda.chamadoDTO.codigo = ");
            sql.append(chamadoDTO.getCodigo());
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