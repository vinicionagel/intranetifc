package com.dao;

import com.core.GenericDAO;
import com.dto.FuncaoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioSetorDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class UsuarioSetorDAO extends GenericDAO<UsuarioSetorDTO> {

    private static final long serialVersionUID = 1027L;
    static final Logger logger = Logger.getLogger(UsuarioSetorDAO.class.getName());
    private volatile static UsuarioSetorDAO uniqueInstance;

    public static UsuarioSetorDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (UsuarioSetorDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new UsuarioSetorDAO();
                }
            }
        }
        return uniqueInstance;
    }

    public UsuarioSetorDAO() {
        super(UsuarioSetorDTO.class);
    }

    public boolean unique(UsuarioDTO usuarioDTO, SetorDTO setorDTO, FuncaoDTO funcaoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM UsuarioSetorDTO usuarioSetor");
            sql.append(" WHERE usuarioSetor.usuarioDTO.codigo = ");
            sql.append(usuarioDTO.getCodigo());
            sql.append(" AND usuarioSetor.setorDTO.codigo = ");
            sql.append(setorDTO.getCodigo());

            return !em.createQuery(sql.toString()).getResultList().isEmpty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<UsuarioSetorDTO> pesquisarUsuariosSetor(SetorDTO setorDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM UsuarioSetorDTO usuarioSetor WHERE usuarioSetor.setorDTO.codigo = :setor").setParameter("setor", setorDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<UsuarioSetorDTO> pesquisarSetoresUsuario(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM UsuarioSetorDTO usuarioSetor WHERE usuarioSetor.usuarioDTO.codigo = :usuario").setParameter("usuario", usuarioDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public int pesquisarUsuarioSetorPorSetorEUsuario(UsuarioDTO usuarioDTO,SetorDTO setorDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            return (int) em.createQuery("SELECT usuarioSetor.responsavel FROM UsuarioSetorDTO usuarioSetor WHERE usuarioSetor.setorDTO.codigo = :setor AND usuarioSetor.usuarioDTO.codigo = :usuario ").
                    setParameter("setor", setorDTO.getCodigo()).setParameter("usuario", usuarioDTO.getCodigo()).getSingleResult();
        } catch (Exception e) {
            return 0;
        } finally {
            em.close();
        }
    }
    
    public UsuarioSetorDTO pesquisarUsuarioSetorUsuarioSemSetor(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM UsuarioSetorDTO usuarioSetor");
            sql.append(" WHERE usuarioSetor.usuarioDTO.codigo = :usuario");
            sql.append(" AND LOWER (usuarioSetor.setorDTO.descricao) = 'usuário sem setor'");
            return (UsuarioSetorDTO) em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}