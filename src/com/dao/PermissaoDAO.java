package com.dao;

import com.core.GenericDAO;
import com.dto.InterfaceDTO;
import com.dto.PermissaoDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class PermissaoDAO extends GenericDAO<PermissaoDTO> {

    private static final long serialVersionUID = 1014L;
    static final Logger logger = Logger.getLogger(PermissaoDAO.class.getName());
    private volatile static PermissaoDAO uniqueInstance;
    
    public PermissaoDAO() {
        super(PermissaoDTO.class);
    }        

    public static PermissaoDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (PermissaoDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PermissaoDAO();
                }
            }
        }
        return uniqueInstance;
    }

    public List<PermissaoDTO> permissoesUsuario(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT u.permissoes ");
            sql.append("FROM UsuarioDTO as u ");
            sql.append("WHERE u.codigo = :usuario ");

            return em.createQuery(sql.toString()).
                    setParameter("usuario", usuarioDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<PermissaoDTO> pesquisarPermisoesPorInterface(InterfaceDTO interfaceDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM PermissaoDTO as pdto ");
            sql.append("WHERE pdto.interfaceDTO.codigo = :interface");
            return em.createQuery(sql.toString()).
                    setParameter("interface", interfaceDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public PermissaoDTO pesquisarPermissaoPorInterface(InterfaceDTO interfaceDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM PermissaoDTO as pdto ");
            sql.append("WHERE pdto.interfaceDTO.codigo = :interface");
            return (PermissaoDTO) em.createQuery(sql.toString()).
                    setParameter("interface", interfaceDTO.getCodigo()).getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}