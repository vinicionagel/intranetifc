package com.dao;

import com.core.GenericDAO;
import com.dto.LocalizacaoDTO;
import com.dto.ReservaLocalizacaoDTO;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ReservaLocalizacaoDAO extends GenericDAO<ReservaLocalizacaoDTO> {
    
    private static final long serialVersionUID = 1017L;
    static final Logger logger = Logger.getLogger(ReservaLocalizacaoDAO.class.getName());
    private volatile static ReservaLocalizacaoDAO uniqueInstance;    
    
    public static ReservaLocalizacaoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (ReservaLocalizacaoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new ReservaLocalizacaoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public ReservaLocalizacaoDAO() {
        super(ReservaLocalizacaoDTO.class);
    }
    
    public List<ReservaLocalizacaoDTO> uniqueReservaLocalizacao(LocalizacaoDTO localizacao, Date dataInicial, Date dataFinal) {
       EntityManager em = emf.createEntityManager();
       try {            
            return em.createQuery("FROM ReservaLocalizacaoDTO AS rl WHERE rl.localizacaoDTO.codigo = :localizacao AND (( :dataInicial > rl.dataInicial AND :dataInicial < rl.dataFinal) OR ( :dataFinal > rl.dataInicial AND :dataFinal < rl.dataFinal) OR (rl.dataInicial = :dataInicial) OR ( :dataInicial < rl.dataInicial AND :dataFinal >= rl.dataFinal ))")
                    .setParameter("localizacao", localizacao.getCodigo())
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();                                                    
        } catch (Exception e) {      
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
           em.close();
       }
    }
    
    public ReservaLocalizacaoDTO pesquisarReservaLocalizacaoDTO (LocalizacaoDTO localizacao, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            return (ReservaLocalizacaoDTO) em.createQuery("FROM ReservaLocalizacaoDTO AS rl WHERE rl.localizacaoDTO.codigo = :localizacao AND  :dataInicial = rl.dataInicial AND :dataFinal = rl.dataFinal")
                    .setParameter("localizacao", localizacao.getCodigo())
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}