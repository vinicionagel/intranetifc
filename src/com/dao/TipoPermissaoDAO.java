package com.dao;

import com.core.GenericDAO;
import com.dto.TipoPermissaoDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class TipoPermissaoDAO extends GenericDAO<TipoPermissaoDTO> {

    private static final long serialVersionUID = 1023L;
    static final Logger logger = Logger.getLogger(TipoPermissaoDAO.class.getName());
    private volatile static TipoPermissaoDAO uniqueInstance;
    
    public static TipoPermissaoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TipoPermissaoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TipoPermissaoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    
    public TipoPermissaoDAO() {
        super(TipoPermissaoDTO.class);
    }

    @Override
    public boolean unique(TipoPermissaoDTO tipoPermissaoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM TipoPermissaoDTO WHERE descricao = '");
            sql.append(tipoPermissaoDTO.getDescricao());
            sql.append("'");

            return !em.createQuery(sql.toString()).getResultList().isEmpty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<TipoPermissaoDTO> pesquisarNome(TipoPermissaoDTO tipoPermissaoDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM TipoPermissaoDTO WHERE LOWER (descricao) LIKE '%");
            sql.append(tipoPermissaoDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
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