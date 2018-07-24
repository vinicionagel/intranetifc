package com.dao;

import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.LocalizacaoDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class LocalizacaoDAO extends GenericDAO<LocalizacaoDTO> {

    private static final long serialVersionUID = 1012L;
    static final Logger logger = Logger.getLogger(LocalizacaoDAO.class.getName());    
    private volatile static LocalizacaoDAO uniqueInstance;
    
    public LocalizacaoDAO() {
        super(LocalizacaoDTO.class);
    }
            
    public static LocalizacaoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (LocalizacaoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new LocalizacaoDAO();
                }
            }
        }
        return uniqueInstance;
    }  

    @Override
    public boolean unique(LocalizacaoDTO localizacaoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM LocalizacaoDTO local ");
            sql.append("WHERE LOWER (local.descricao) = '");
            sql.append(localizacaoDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("' AND local.campusDTO.codigo = ");
            sql.append(localizacaoDTO.getCampusDTO().getCodigo());

            return !em.createQuery(sql.toString()).getResultList().isEmpty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<LocalizacaoDTO> pesquisarPorCampus(CampusDTO campusDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM LocalizacaoDTO local WHERE local.campusDTO.codigo = :campus ORDER BY local.descricao")
                    .setParameter("campus", campusDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<LocalizacaoDTO> pesquisarNome(LocalizacaoDTO localizacaoDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM LocalizacaoDTO local WHERE LOWER (local.descricao) LIKE  '%");
            sql.append(localizacaoDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY local.descricao");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<LocalizacaoDTO> pesquisarNomeECampus(String localizacaoDTO, CampusDTO campusDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM LocalizacaoDTO local WHERE LOWER (local.descricao) LIKE '%");
            sql.append(localizacaoDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' AND local.campusDTO.codigo = :campus ");
            sql.append("ORDER BY local.descricao");

            return em.createQuery(sql.toString())
                    .setParameter("campus", campusDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}