package com.dao;

import com.core.GenericDAO;
import com.dto.PatrimonioDTO;
import com.dto.ReservaPatrimonioDTO;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ReservaPatrimonioDAO extends GenericDAO<ReservaPatrimonioDTO> {
    
    private static final long serialVersionUID = 1018L;
    static final Logger logger = Logger.getLogger(ReservaPatrimonioDAO.class.getName());
    private volatile static ReservaPatrimonioDAO uniqueInstance;
    
    public static ReservaPatrimonioDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (ReservaPatrimonioDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new ReservaPatrimonioDAO();
                }
            }
        }
        return uniqueInstance;
    }     
    
    public ReservaPatrimonioDAO() {
        super(ReservaPatrimonioDTO.class);
    }
    
    /**
     * Método usando em unique e para carregamento por semana (lazy loading)
     * @param localizacao
     * @param dataInicial
     * @param dataFinal
     * @return 
     */
    public List<ReservaPatrimonioDTO> uniqueReservaPatrimonio(PatrimonioDTO patrimonio, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM ReservaPatrimonioDTO AS rl WHERE rl.patrimonioDTO.codigo = :patrimonio AND (( :dataInicial > rl.dataInicial AND :dataInicial < rl.dataFinal) OR ( :dataFinal > rl.dataInicial AND :dataFinal < rl.dataFinal) OR (rl.dataInicial = :dataInicial) OR ( :dataInicial < rl.dataInicial AND :dataFinal >= rl.dataFinal ))")
                    .setParameter("patrimonio", patrimonio.getCodigo())
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
    
    public ReservaPatrimonioDTO pesquisarReservaPatrimonioDTO (PatrimonioDTO patrimonio, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            return (ReservaPatrimonioDTO) em.createQuery("FROM ReservaPatrimonioDTO AS rp WHERE rp.patrimonioDTO.codigo = :patrimonio AND :dataInicial = rp.dataInicial AND :dataFinal = rp.dataFinal")
                    .setParameter("patrimonio", patrimonio.getCodigo())
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