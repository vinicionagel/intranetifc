package com.dao;

import com.auxiliar.Contexto;
import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class SetorDAO extends GenericDAO<SetorDTO> {

    private static final long serialVersionUID = 1020L;
    static final Logger logger = Logger.getLogger(SetorDAO.class.getName());
    private volatile static SetorDAO uniqueInstance;
    
    public static SetorDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (SetorDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new SetorDAO();
                }
            }
        }
        return uniqueInstance;
    }     
    
    public SetorDAO() {
        super(SetorDTO.class);
    }

    @Override
    public boolean unique(SetorDTO setorDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM SetorDTO setor ");
            sql.append(" WHERE LOWER (setor.descricao) = '");
            sql.append(setorDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("'");
            sql.append(" AND setor.campusDTO.codigo = :campus");
            return !em.createQuery(sql.toString())
                    .setParameter("campus", setorDTO.getCampusDTO().getCodigo())
                    .getResultList()
                    .isEmpty();
        } catch (Throwable e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<SetorDTO> pesquisarNome(SetorDTO setorDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE LOWER (setor.descricao) LIKE '%");
            sql.append(setorDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY  setor.campusDTO, setor.descricao");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> pesquisarNomeECampus(String setorDTO, CampusDTO campusDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE LOWER (setor.descricao) LIKE '%");
            sql.append(setorDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' AND setor.campusDTO.codigo = :campus ");
            sql.append("ORDER BY setor.campusDTO, setor.descricao");

            return em.createQuery(sql.toString()).setParameter("campus", campusDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> pesquisarSetorPorCampus(String setorDTO, CampusDTO campusDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE LOWER (setor.descricao) LIKE '%");
            sql.append(setorDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' AND setor.campusDTO.codigo = :campus ");
            sql.append("AND (LOWER (setor.descricao) != 'usuário sem setor' ");
            sql.append("AND LOWER (setor.descricao) != 'administrador')");
            sql.append("ORDER BY setor.campusDTO, setor.descricao");
            return em.createQuery(sql.toString()).setParameter("campus", campusDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<SetorDTO> pesquisarPorCampus(CampusDTO campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM SetorDTO setor WHERE setor.campusDTO.codigo = :campus AND LOWER(setor.descricao) NOT IN ('administrador', 'usuário sem setor') AND setor.interfaceAtivo LIKE '%''2:1''%' ORDER BY setor.descricao")
                    .setParameter("campus", campusDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> pesquisarPorCampusOcorrencia(CampusDTO campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM SetorDTO setor WHERE setor.campusDTO.codigo = :campus AND LOWER(setor.descricao) NOT IN ('administrador', 'usuário sem setor') AND setor.interfaceAtivo LIKE '%''22:1''%' ORDER BY setor.descricao")
                    .setParameter("campus", campusDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<SetorDTO> pesquisarSetoresPorCampus(List<CampusDTO> campusBusca){
        EntityManager em = emf.createEntityManager();
        try {
            List<SetorDTO> setores = new ArrayList<>();
            campusBusca.forEach((campusDTO) -> {
                setores.addAll(em.createQuery("FROM SetorDTO setor WHERE setor.campusDTO.codigo = :campus AND LOWER(setor.descricao) NOT IN ('administrador', 'usuário sem setor') ORDER BY setor.descricao")
                        .setParameter("campus", campusDTO.getCodigo())
                        .getResultList());
            });
            return setores;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    
    public List<SetorDTO> consultaTodosSetoresMenosUsuarioSemSetorDoUsuario(UsuarioDTO usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT s ");
            sql.append("FROM UsuarioSetorDTO us, SetorDTO s ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append("AND us.setorDTO.codigo = s.codigo ");
            sql.append("AND LOWER (us.setorDTO.descricao) != 'usuário sem setor' ");
            sql.append("ORDER BY s.campusDTO.descricao, s.descricao");
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> consultarSetoresDoUsuario(UsuarioDTO usuario, CampusDTO campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT s ");
            sql.append("FROM UsuarioSetorDTO us, SetorDTO s ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append("AND us.setorDTO.codigo = s.codigo ");
            sql.append("AND LOWER (us.setorDTO.descricao) != 'usuário sem setor' ");
            sql.append("AND us.setorDTO.campusDTO.codigo = :codigoCampus ");
            sql.append("ORDER BY s.campusDTO.descricao, s.descricao");

            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("codigoCampus", campusDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
     public List<SetorDTO> consultarSetoresDoUsuarioPorCampusUsuarioSemSetor(UsuarioDTO usuario, CampusDTO campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT s ");
            sql.append("FROM UsuarioSetorDTO us, SetorDTO s ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append("AND us.setorDTO.codigo = s.codigo ");
            sql.append("AND LOWER (us.setorDTO.descricao) != 'usuário sem setor' ");
            sql.append("AND us.setorDTO.campusDTO.codigo = :campusCodigo ");
            sql.append("ORDER BY s.campusDTO.descricao, s.descricao");

            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("campusCodigo", campusDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> consultarSetoresDoUsuarioPorCampus(Contexto contexto) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT us.setorDTO ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append("AND us.setorDTO.campusDTO.codigo = :campus ");
            sql.append("ORDER BY us.setorDTO.descricao");
            
            return em.createQuery(sql.toString())
                    .setParameter("usuario", contexto.getUsuarioLogado().getCodigo())
                    .setParameter("campus", contexto.getCampusAtual().getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> consultarSetoresDoUsuarioOcorrenciaPorCampus(UsuarioDTO usuario, CampusDTO campus) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT us.setorDTO ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append("AND us.setorDTO.campusDTO.codigo = :campus ");
            sql.append("AND us.setorDTO.interfaceAtivo LIKE '%''22:1''%' ");
            sql.append("ORDER BY us.setorDTO.descricao");

            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("campus", campus.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> consultarSetoresDoUsuarioPorCampus(UsuarioDTO usuario, CampusDTO campus) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT us.setorDTO ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append("AND us.setorDTO.campusDTO.codigo = :campus ");
            sql.append("ORDER BY us.setorDTO.descricao");
            
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("campus", campus.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<SetorDTO> consultarSetoresOrdenadosPorCampus(Contexto contexto) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT us.setorDTO ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario ");            
            sql.append("ORDER BY us.setorDTO.campusDTO.descricao");
            return em.createQuery(sql.toString())
                    .setParameter("usuario", contexto.getUsuarioLogado().getCodigo())                    
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public SetorDTO pesquisarDescricao(String setorDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE setor.descricao = '");
            sql.append(setorDTO);
            sql.append("'");

            return (SetorDTO) em.createQuery(sql.toString()).getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<SetorDTO> findAll(){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE LOWER(setor.descricao) NOT IN ('administrador', 'usuário sem setor') ORDER BY setor.campusDTO.descricao, setor.descricao");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> pesquisarTodosMenosSelecionado(SetorDTO setor){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE LOWER(setor.descricao) NOT IN ('administrador', 'usuário sem setor') AND setor.codigo != :id ORDER BY setor.campusDTO.descricao, setor.descricao");
            return em.createQuery(sql.toString()).setParameter("id", setor.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
        
    public List<SetorDTO> pesquisarTodosMenosSetorSemSetor(){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE LOWER (setor.descricao) != 'usuário sem setor' ORDER BY setor.campusDTO.descricao, setor.descricao");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> pesquisarTodosMenosSetorSemSetorPorCampus(CampusDTO campusDTO){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM SetorDTO setor WHERE LOWER (setor.descricao) != 'usuário sem setor' AND setor.campusDTO.codigo = :codigoCampus ORDER BY setor.campusDTO.descricao, setor.descricao");
            return em.createQuery(sql.toString()).setParameter("codigoCampus", campusDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public SetorDTO pesquisarSetorUsuariosSemSetor(CampusDTO campus) {	
        EntityManager em = emf.createEntityManager();
	try{
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT setor ");
            sql.append(" FROM SetorDTO setor ");
            sql.append(" WHERE LOWER (setor.descricao) = 'usuário sem setor' ");
            sql.append(" AND setor.campusDTO.codigo = :campus ");

            return (SetorDTO) em.createQuery(sql.toString())
                    .setParameter("campus", campus.getCodigo())
                    .getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}