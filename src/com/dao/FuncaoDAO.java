package com.dao;

import com.core.GenericDAO;
import com.dto.FuncaoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class FuncaoDAO extends GenericDAO<FuncaoDTO> {

    private static final long serialVersionUID = 1009L;
    static final Logger logger = Logger.getLogger(FuncaoDAO.class.getName());
    private volatile static FuncaoDAO uniqueInstance;
    
    public FuncaoDAO() {
        super(FuncaoDTO.class);
    }
        
    public static FuncaoDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (FuncaoDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new FuncaoDAO();
                }
            }
        }
        return uniqueInstance;
    }

    @Override
    public boolean unique(FuncaoDTO funcaoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM FuncaoDTO funcao WHERE LOWER (funcao.descricao) = '");
            sql.append(funcaoDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("'");

            return !em.createQuery(sql.toString()).getResultList().isEmpty();
        } catch (Throwable e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<FuncaoDTO> pesquisarNome(String funcaoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM FuncaoDTO funcao WHERE LOWER (funcao.descricao) LIKE '%");
            sql.append(funcaoDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY funcao.descricao");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public FuncaoDTO pesquisarFuncaoUsuarioSetor(UsuarioDTO usuario, SetorDTO setor) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT us.funcaoDTO ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.setorDTO.codigo = :setor ");
            sql.append("AND us.usuarioDTO.codigo = :usuario");

            return (FuncaoDTO) em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("setor", setor.getCodigo())
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<FuncaoDTO> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM FuncaoDTO funcao ORDER BY funcao.descricao");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}