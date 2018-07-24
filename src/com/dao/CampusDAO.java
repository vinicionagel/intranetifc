package com.dao;

import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class CampusDAO extends GenericDAO<CampusDTO> {
    
    private static final long serialVersionUID = 1004L;
    static final Logger logger = Logger.getLogger(CampusDAO.class.getName());
    private volatile static CampusDAO uniqueInstance;
    
    public static CampusDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (CampusDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new CampusDAO();
                }
            }
        }
        return uniqueInstance;
    }  

    public CampusDAO() {
        super(CampusDTO.class);
    }

    @Override
    public boolean unique(CampusDTO campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM CampusDTO as c WHERE LOWER (c.descricao) = '");
            sql.append(campusDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("'");

            return !em.createQuery(sql.toString()).getResultList().isEmpty();
        } catch (Throwable e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<CampusDTO> pesquisarNome(String campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM CampusDTO c WHERE LOWER (c.descricao) LIKE '%");
            sql.append(campusDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%'");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<CampusDTO> pesquisarCampusPorUsuario(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT us.setorDTO.campusDTO ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario");

            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .getResultList();

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<CampusDTO> pesquisarCampusPorUsuarioOrdenados(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT us.setorDTO.campusDTO ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append("ORDER BY us.setorDTO.campusDTO.codigo DESC");
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {      
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<CampusDTO> pesquisarCampusPorSetores(String setores) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT setor.campusDTO ");
            sql.append("FROM SetorDTO as setor ");
            if(!setores.isEmpty()){
              sql.append("WHERE setor.codigo ");
              sql.append("IN (");  
              sql.append(setores);
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
}