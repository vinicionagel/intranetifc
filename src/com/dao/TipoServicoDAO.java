package com.dao;

import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.TipoServicoDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class TipoServicoDAO extends GenericDAO<TipoServicoDTO> { 
    
    private static final long serialVersionUID = 1024L;
    static final Logger logger = Logger.getLogger(TipoServicoDAO.class.getName());
    private volatile static TipoServicoDAO uniqueInstance;
    
    public TipoServicoDAO(){
        super(TipoServicoDTO.class);
    }
        
    public static TipoServicoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TipoServicoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TipoServicoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
        
    @Override
    public boolean unique(TipoServicoDTO tipoServicoDTO){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM TipoServicoDTO tipoServico ");
            sql.append(" WHERE LOWER (tipoServico.descricao) = '");
            sql.append(tipoServicoDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));        
            sql.append("' AND tipoServico.setorDTO.codigo = ");
            sql.append(tipoServicoDTO.getSetorDTO().getCodigo());        
            return !em.createQuery(sql.toString()).getResultList().isEmpty();               
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }
    
    public List<TipoServicoDTO> pesquisarNome(TipoServicoDTO tipoServico)  { 
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoServicoDTO tipoServico WHERE LOWER (tipoServico.descricao) LIKE '%");
            sql.append(tipoServico.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY tipoServico.descricao, tipoServico.setorDTO.descricao");
            return  em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<TipoServicoDTO> pesquisarNomeECampus(String tipoServico, CampusDTO campus)  {  
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoServicoDTO tipoServico WHERE LOWER (tipoServico.descricao) LIKE '%");
            sql.append(tipoServico.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' AND tipoServico.setorDTO.campusDTO.codigo = :campus ");
            sql.append("ORDER BY tipoServico.descricao, tipoServico.setorDTO.descricao");
            return  em.createQuery(sql.toString()).setParameter("campus", campus.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<TipoServicoDTO> pesquisarPorCampus(CampusDTO campusDTO){
        EntityManager em = emf.createEntityManager();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoServicoDTO tipoServico ");
            sql.append("WHERE tipoServico.setorDTO.campusDTO.codigo = :campus ");            
            sql.append("ORDER BY tipoServico.setorDTO.descricao, tipoServico.descricao");
            
            return em.createQuery(sql.toString()).setParameter("campus", campusDTO.getCodigo()).getResultList();
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<TipoServicoDTO> pesquisarPorSetor(long setorDTO){
        EntityManager em = emf.createEntityManager();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoServicoDTO tipoServico ");
            sql.append("WHERE tipoServico.setorDTO.codigo = ");
            sql.append(setorDTO);
            sql.append(" ORDER BY tipoServico.descricao ");
            
            return  em.createQuery(sql.toString()).getResultList();
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public TipoServicoDTO pesquisarPorSetorEDescricao(Long setorDTO, TipoServicoDTO ts){
        EntityManager em = emf.createEntityManager();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoServicoDTO tipoServico ");
            sql.append(" WHERE tipoServico.setorDTO.codigo = ");
            sql.append(setorDTO);            
            sql.append(" AND tipoServico.descricao = '");
            sql.append(ts.getDescricao());
            sql.append("'");            
            
            return (TipoServicoDTO) em.createQuery(sql.toString()).getSingleResult();
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<TipoServicoDTO> findAll(){
        EntityManager em = emf.createEntityManager();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("FROM TipoServicoDTO tipoServico ORDER BY tipoServico.descricao");
            return  em.createQuery(sql.toString()).getResultList();
        }catch(Exception e){
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}