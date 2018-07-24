package com.dao;

import com.auxiliar.Contexto;
import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class PatrimonioDAO extends GenericDAO<PatrimonioDTO> {

    private static final long serialVersionUID = 1013L;
    static final Logger logger = Logger.getLogger(PatrimonioDAO.class.getName());
    private volatile static PatrimonioDAO uniqueInstance;
    
    public PatrimonioDAO() {
        super(PatrimonioDTO.class);
    }
            
    public static PatrimonioDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (PatrimonioDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new PatrimonioDAO();
                }
            }
        }
        return uniqueInstance;
    }  
    
    @Override
    public boolean unique(PatrimonioDTO patrimonioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM PatrimonioDTO patrimonio WHERE patrimonio.descricao = '");
            sql.append(patrimonioDTO.getDescricao());
            sql.append("'");
            sql.append("AND patrimonio.localizacaoDTO.codigo = :localizacao");

            return !em.createQuery(sql.toString())
                    .setParameter("localizacao", patrimonioDTO.getLocalizacaoDTO().getCodigo())
                    .getResultList()
                    .isEmpty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<PatrimonioDTO> pesquisarNome(PatrimonioDTO patrimonioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM PatrimonioDTO patrimonio WHERE LOWER (patrimonio.descricao) LIKE  '%");
            sql.append(patrimonioDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY patrimonio.setorDTO.campusDTO.descricao, patrimonio.localizacaoDTO.descricao, patrimonio.descricao");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<PatrimonioDTO> pesquisarNomeECampus(PatrimonioDTO patrimonioDTO, CampusDTO campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM PatrimonioDTO patrimonio WHERE LOWER (patrimonio.descricao) LIKE  '%");
            sql.append(patrimonioDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' AND patrimonio.setorDTO.campusDTO.codigo = :campus ");
            sql.append("ORDER BY patrimonio.setorDTO.campusDTO.descricao, patrimonio.localizacaoDTO.descricao, patrimonio.descricao");

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

    public List<PatrimonioDTO> pesquisarPorLocalizacao(LocalizacaoDTO localizacaoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM PatrimonioDTO patrimonio WHERE patrimonio.localizacaoDTO.codigo = :localizacao ORDER BY patrimonio.descricao")
                    .setParameter("localizacao", localizacaoDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<PatrimonioDTO> pesquisarSemBaixaPorLocalizacao(LocalizacaoDTO localizacaoDTO){
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM PatrimonioDTO patrimonio WHERE patrimonio.localizacaoDTO.codigo = :localizacao AND patrimonio.dataBaixa IS NULL ORDER BY patrimonio.descricao")
                    .setParameter("localizacao", localizacaoDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<PatrimonioDTO> pesquisarPatrimonioPorSetoresDoUsuario(UsuarioDTO usuario)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p ");
            sql.append("FROM PatrimonioDTO p ");
            sql.append("WHERE p.setorDTO.codigo ");
            sql.append("IN ( SELECT us.setorDTO.codigo ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario) ");
            sql.append("ORDER BY p.descricao");
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
    
    public List<PatrimonioDTO> pesquisarPatrimonioPorSetores(String setores)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            if(!setores.isEmpty()){
              sql.append("SELECT p ");
              sql.append("FROM PatrimonioDTO p ");
              sql.append("WHERE p.setorDTO.codigo ");
              sql.append("IN (");  
              sql.append(setores);
              sql.append(" ) ");
              sql.append("ORDER BY p.descricao");
            }
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<PatrimonioDTO> pesquisarPatrimonioPorSetoresDoUsuarioOrdenadoPorCampusELocalizacao(UsuarioDTO usuario)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p ");
            sql.append("FROM PatrimonioDTO p ");
            sql.append("WHERE p.setorDTO.codigo ");
            sql.append("IN ( SELECT us.setorDTO.codigo ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario) ");
            sql.append("ORDER BY p.localizacaoDTO.campusDTO.codigo, p.localizacaoDTO.codigo");
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

    public List<PatrimonioDTO> pesquisarPatrimonioSemBaixaPorSetoresDoUsuarioOrdenadoPorCampusELocalizacao(Contexto contexto)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();            
            sql.append("FROM PatrimonioDTO patrimonio ");
            sql.append("WHERE patrimonio.setorDTO.codigo ");
            sql.append("IN ( SELECT us.setorDTO.codigo ");
                sql.append("FROM UsuarioSetorDTO us ");
                sql.append("WHERE us.usuarioDTO.codigo = :usuario) ");
            sql.append("AND patrimonio.dataBaixa IS NULL ");
            sql.append("AND patrimonio.setorDTO.campusDTO.codigo = :campus ");
            sql.append("ORDER BY patrimonio.localizacaoDTO.campusDTO.codigo, patrimonio.localizacaoDTO.codigo");
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

    @Override
    public List<PatrimonioDTO> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM PatrimonioDTO patrimonio ORDER BY patrimonio.setorDTO.campusDTO.descricao, patrimonio.localizacaoDTO.descricao, patrimonio.descricao").getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<PatrimonioDTO> pesquisarPatrimoniosPeloFiltro(PatrimonioDTO patrimonio, CampusDTO campus) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM PatrimonioDTO patrimonio ");
            sql.append("WHERE LOWER (patrimonio.descricao) LIKE '%");
            sql.append(patrimonio.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("%'");
            if (campus != null) {
                sql.append(" AND patrimonio.setorDTO.campusDTO.codigo = ");
                sql.append(campus.getCodigo());
            }
            if (patrimonio.getLocalizacaoDTO() != null) {
                sql.append(" AND patrimonio.localizacaoDTO.codigo = ");
                sql.append(patrimonio.getLocalizacaoDTO().getCodigo());
            }
            
            if (patrimonio.getFabricanteDTO() != null) {
                sql.append(" AND patrimonio.fabricanteDTO.codigo = ");
                sql.append(patrimonio.getFabricanteDTO().getCodigo());
            }
            
            if (patrimonio.getTipoPatrimonioDTO() != null) {
                sql.append(" AND patrimonio.tipoPatrimonioDTO.codigo = ");
                sql.append(patrimonio.getTipoPatrimonioDTO().getCodigo());
            }
            
            sql.append(" ORDER BY patrimonio.setorDTO.campusDTO.descricao, patrimonio.localizacaoDTO.descricao, patrimonio.descricao");
            
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}