package com.dao;

import com.core.GenericDAO;
import com.dto.PatrimonioDTO;
import com.dto.TipoPatrimonioDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class TipoPatrimonioDAO extends GenericDAO<TipoPatrimonioDTO> {

    private static final long serialVersionUID = 1022L;
    static final Logger logger = Logger.getLogger(TipoPatrimonioDAO.class.getName());
    private volatile static TipoPatrimonioDAO uniqueInstance;
    
    public static TipoPatrimonioDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TipoPatrimonioDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TipoPatrimonioDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    
    public TipoPatrimonioDAO() {
        super(TipoPatrimonioDTO.class);
    }

    @Override
    public boolean unique(TipoPatrimonioDTO tipoPatrimonioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoPatrimonioDTO tipoPatrimonio WHERE tipoPatrimonio.descricao = '");
            sql.append(tipoPatrimonioDTO.getDescricao());
            sql.append("'");

            return !em.createQuery(sql.toString()).getResultList().isEmpty();
        } catch (Throwable e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<TipoPatrimonioDTO> pesquisarNome(String tipoPatrimonioDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoPatrimonioDTO tipoPatrimonio WHERE LOWER (tipoPatrimonio.descricao) LIKE '%");
            sql.append(tipoPatrimonioDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY tipoPatrimonio.descricao");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<TipoPatrimonioDTO> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoPatrimonioDTO tipoPatrimonio ORDER BY tipoPatrimonio.descricao");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<TipoPatrimonioDTO> pesquisarTipoPatrimonioPorPatrimonios(List<PatrimonioDTO> patrimonios) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            if(!patrimonios.isEmpty()){
                sql.append("SELECT DISTINCT patri.tipoPatrimonioDTO ");
                sql.append("FROM PatrimonioDTO as patri ");
                sql.append("WHERE patri.codigo ");
                sql.append("IN (");  
                sql.append(gerarListaIN(patrimonios));
                sql.append(" ) ");
            }
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {            
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
        
    private String gerarListaIN(List<PatrimonioDTO> lista){        
        String result = "";        
        int contador = 0;        
        for (PatrimonioDTO c : lista){            
            result = result.concat(c.getCodigo().toString());
            if (lista.size() > (contador + 1)){
                result = result.concat(",");
            } 
           contador ++;
        }	
        return result;
    }
}