package com.dao;

import com.core.GenericDAO;
import com.dto.ConfiguracaoDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ConfiguracaoDAO extends GenericDAO<ConfiguracaoDTO> {

    private static final long serialVersionUID = 1009L;
    static final Logger logger = Logger.getLogger(ConfiguracaoDAO.class.getName());
    private volatile static ConfiguracaoDAO uniqueInstance;

    public ConfiguracaoDAO() {
        super(ConfiguracaoDAO.class);
    }

    public static ConfiguracaoDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ConfiguracaoDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ConfiguracaoDAO();
                }
            }
        }
        return uniqueInstance;
    }

    public List<ConfiguracaoDTO> consultarConfiguracoesPorTipo(String tipo) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM ConfiguracaoDTO c ");
            sql.append(" WHERE c.tipoConfiguracaoDTO.descricao = '");
            sql.append(tipo);
            sql.append("'");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<ConfiguracaoDTO> consultarConfiguracoesUsuario(UsuarioDTO usu){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT u.configuracoes ");
            sql.append(" FROM UsuarioDTO u ");
            sql.append(" WHERE u.codigo = :usuario ");
            return em.createQuery(sql.toString()).setParameter("usuario", usu.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public boolean verificarUsuarioObservadorRecebeEmail(UsuarioDTO usu){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();            
            sql.append(" SELECT u ");
            sql.append(" FROM UsuarioDTO u ");
            sql.append(" WHERE u.codigo = :usuario ");
            sql.append(" AND 1 IN (SELECT c.codigo FROM u.configuracoes as c)");
            return em.createQuery(sql.toString()).setParameter("usuario", usu.getCodigo()).getSingleResult() != null;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean verificarUsuarioAtribuidoRecebeEmail(UsuarioDTO usu){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();            
            sql.append(" SELECT u ");
            sql.append(" FROM UsuarioDTO u ");
            sql.append(" WHERE u.codigo = :usuario ");
            sql.append(" AND 2 IN (SELECT c.codigo FROM u.configuracoes as c)");
            return em.createQuery(sql.toString()).setParameter("usuario", usu.getCodigo()).getSingleResult() != null;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean verificarUsuarioAutorRecebeEmail(UsuarioDTO usu){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();   
            sql.append(" SELECT u ");
            sql.append(" FROM UsuarioDTO u ");
            sql.append(" WHERE u.codigo = :usuario ");
            sql.append(" AND 3 IN (SELECT c.codigo FROM u.configuracoes as c)");
            return em.createQuery(sql.toString()).setParameter("usuario", usu.getCodigo()).getSingleResult() != null;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }
}