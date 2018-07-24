package com.dao;

import com.auxiliar.Contexto;
import com.core.GenericDAO;
import com.dto.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ChamadoServicoDAO extends GenericDAO<ChamadoServicoDTO> {
    
    private static final long serialVersionUID = 1008L;
    static final Logger logger = Logger.getLogger(ChamadoServicoDAO.class.getName());
    private volatile static ChamadoServicoDAO uniqueInstance;
    
    public ChamadoServicoDAO() {
        super(ChamadoServicoDTO.class);
    }           
            
    public static ChamadoServicoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (ChamadoServicoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new ChamadoServicoDAO();
                }
            }
        }
        return uniqueInstance;
    }      
        
    public ChamadoServicoDTO pesquisarSeChamadoPertenceUsuario(UsuarioDTO usuario, long id){
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT chServ ");
            sql.append("FROM ChamadoServicoDTO as chServ ");
            sql.append("WHERE chServ.codigo = :id ");
            sql.append("AND (chServ.servicoDTO.tipoServicoDTO.setorDTO.codigo ");
            sql.append("IN ( SELECT usuSetor.setorDTO.codigo ");
                sql.append("FROM UsuarioSetorDTO as usuSetor ");
                sql.append("WHERE usuSetor.usuarioDTO.codigo = :usuario ) ");
            sql.append("OR chServ.usuarioAutorDTO.codigo = :usuario ");
            sql.append("OR chServ.usuarioAtribuidoDTO.codigo = :usuario ");
            sql.append("OR :usuario IN (SELECT u.codigo FROM chServ.observadores as u)) ");
            
            return (ChamadoServicoDTO) em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("id", id)
                    .getSingleResult();             
        } catch (NoResultException e) {            
            return null;       
        } finally {
            em.close();
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorTitulo(String titulo, Contexto contexto) {        
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();            
            sql.append("SELECT DISTINCT chServ ");
            sql.append("FROM ChamadoServicoDTO as chServ ");
            sql.append("WHERE LOWER(chServ.titulo) LIKE '%");
            sql.append(titulo.toLowerCase());
            sql.append("%'");                  
            sql.append("AND (chServ.servicoDTO.tipoServicoDTO.setorDTO.codigo ");            
            sql.append("IN ( ");
                sql.append(setorCampusIN());
                sql.append(" ) ");                
            sql.append("OR chServ.usuarioAutorDTO.codigo = :usuario) ");
            sql.append("AND chServ.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus ");
            sql.append("AND chServ.statusDTO.codigo != 6");            
            
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
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorSetorUsuario(Contexto contexto) {        
        EntityManager em = emf.createEntityManager();
        try {                                                          
            StringBuilder sql = new StringBuilder();            
            sql.append("SELECT chServ ");
            sql.append("FROM ChamadoServicoDTO as chServ ");
            sql.append("WHERE (chServ.servicoDTO.tipoServicoDTO.setorDTO.codigo ");            
            sql.append("IN ( ");
                sql.append(setorCampusIN());
                sql.append(" ) "); 
            sql.append("OR chServ.usuarioAutorDTO.codigo = :usuario) ");
            sql.append("AND chServ.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus ");
            sql.append("AND chServ.statusDTO.codigo != 6 "); 
            sql.append("ORDER BY chServ.dataHoraAbertura DESC");  
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
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoObservados(Contexto contexto) {      
        try {
            List<ChamadoServicoDTO> lista = new ArrayList<>();                        
            List<ChamadoServicoDTO> listaTodos = findAll();           
            
            for(ChamadoServicoDTO chServ : listaTodos) {
                if(chServ.getObservadores().contains(contexto.getUsuarioLogado())
                    && chServ.getServicoDTO().getTipoServicoDTO().getSetorDTO().getCampusDTO().getCodigo().equals(contexto.getCampusAtual().getCodigo())){
                    lista.add(chServ);
                }                    
            }                
            return lista;
        } catch (Exception e) {            
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }    
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoAtribuidos(Contexto contexto) {
       EntityManager em = emf.createEntityManager();
       try {                                    
            return em.createQuery("FROM ChamadoServicoDTO servico WHERE servico.usuarioAtribuidoDTO.codigo = :usuario AND servico.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus")
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
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorUsuarioEntreDatas(UsuarioDTO usuario, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {            
            return em.createQuery("FROM ChamadoServicoDTO servico WHERE servico.usuarioAtribuidoDTO.codigo = :usuario AND servico.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();                                                    
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorSetorEntreDatas(SetorDTO setor, Date dataInicial, Date dataFinal) {
       EntityManager em = emf.createEntityManager();
       try {
            return em.createQuery("FROM ChamadoServicoDTO servico WHERE servico.servicoDTO.tipoServicoDTO.setorDTO.codigo = :setor AND servico.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
                    .setParameter("setor", setor.getCodigo())
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();                                                    
        } catch (Exception e) {          
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
           em.close();
       }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPorCampusEntreDatas(CampusDTO campus, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {                        
            return em.createQuery("FROM ChamadoServicoDTO servico WHERE servico.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus AND servico.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
                    .setParameter("campus", campus.getCodigo())
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();                                                    
        } catch (Exception e) {          
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarMeusChamadosServico(Contexto contexto) {
       EntityManager em = emf.createEntityManager();
       try {                        
            return em.createQuery("FROM ChamadoServicoDTO chamado WHERE chamado.usuarioAutorDTO.codigo = :usuario AND chamado.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus")
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
    
    public List<ChamadoServicoDTO> pesquisarChamadosServicoPeloFiltro(ChamadoServicoDTO chamado, SetorDTO setor, Contexto contexto, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT chServ ");
            sql.append("FROM ChamadoServicoDTO as chServ ");
            sql.append("WHERE (chServ.servicoDTO.tipoServicoDTO.setorDTO.codigo ");            
            sql.append("IN ( ");
                sql.append(setorCampusIN());
                sql.append(" ) "); 
            sql.append("OR chServ.usuarioAutorDTO.codigo = :usuario) ");
            sql.append("AND chServ.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus ");

            if(chamado.getUsuarioAtribuidoDTO() != null){
                sql.append("AND chServ.usuarioAtribuidoDTO.codigo = ");
                sql.append(chamado.getUsuarioAtribuidoDTO().getCodigo());
            }

            if(chamado.getServicoDTO() != null){
                sql.append(" AND chServ.servicoDTO.codigo = ");
                sql.append(chamado.getServicoDTO().getCodigo());
            }

            if(chamado.getStatusDTO() != null){
                sql.append(" AND chServ.statusDTO.codigo = ");
                sql.append(chamado.getStatusDTO().getCodigo());
            }

            if(setor != null){
                sql.append(" AND chServ.servicoDTO.tipoServicoDTO.setorDTO.codigo = ");
                sql.append(setor.getCodigo());
            }

            if(chamado.getPrioridadeDTO() != null){
                sql.append(" AND chServ.prioridadeDTO = ");
                sql.append(chamado.getPrioridadeDTO().getCodigo());
            }            
            
            if(dataInicial != null) {
                sql.append(" AND chServ.dataHoraAbertura >= :dataInicial ");
            } else if(dataFinal != null){                
                sql.append(" AND chServ.dataHoraAbertura <= :dataFinal ");                
            }             
            
            Query query = em.createQuery(sql.toString());
            query.setParameter("usuario", contexto.getUsuarioLogado().getCodigo());
            query.setParameter("campus", contexto.getCampusAtual().getCodigo());   
            
            if(dataInicial != null) {
                query.setParameter("dataInicial", dataInicial);
            } else if(dataFinal != null){                
                query.setParameter("dataFinal", dataFinal);
            }                        
            return query.getResultList();                                    
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<ChamadoServicoDTO> pesquisarChamadosPorStatusPorSetor(StatusDTO status, SetorDTO setor, Date dataInicial, Date dataFinal) {        
        EntityManager em = emf.createEntityManager();
        try {                                               
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT c ");
            sql.append("FROM ChamadoServicoDTO c ");
            sql.append("WHERE c.statusDTO.codigo = :status ");
            sql.append("AND c.servicoDTO.tipoServicoDTO.setorDTO.codigo = :setor ");            
            if(dataInicial != null && dataFinal != null) {
                sql.append(" AND c.dataHoraAbertura >= :dataInicial AND c.dataHoraAbertura <= :dataFinal ");                
                return em.createQuery(sql.toString())                    
                    .setParameter("status", status.getCodigo())
                    .setParameter("setor", setor.getCodigo())       
                    .setParameter("dataInicial", dataInicial)
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();                
            } else if(dataInicial != null) {
                sql.append(" AND c.dataHoraAbertura >= :dataInicial ");                
                return em.createQuery(sql.toString())                    
                    .setParameter("status", status.getCodigo())
                    .setParameter("setor", setor.getCodigo())       
                    .setParameter("dataInicial", dataInicial)                    
                    .getResultList();                
            } else if(dataFinal != null){
                sql.append(" AND c.dataHoraAbertura <= :dataFinal ");
                return em.createQuery(sql.toString())                    
                    .setParameter("status", status.getCodigo())
                    .setParameter("setor", setor.getCodigo())                           
                    .setParameter("dataFinal", dataFinal)
                    .getResultList();                
            } else {
                return em.createQuery(sql.toString())                    
                        .setParameter("status", status.getCodigo())
                        .setParameter("setor", setor.getCodigo())                               
                        .getResultList();         
            }            
        } catch (Exception e) {            
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }    
    
    public boolean verificarSeExisteChamadoSetor(SetorDTO setorDTO, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT c FROM ChamadoServicoDTO c WHERE c.servicoDTO.tipoServicoDTO.setorDTO.codigo = :setor");
            if(dataInicial != null && dataFinal != null) {
                sql.append(" AND c.dataHoraAbertura >= :dataInicial AND c.dataHoraAbertura <= :dataFinal ");                
                return !em.createQuery(sql.toString())
                        .setParameter("setor", setorDTO.getCodigo())  
                        .setParameter("dataInicial", dataInicial)
                        .setParameter("dataFinal", dataFinal)
                        .getResultList().isEmpty(); 
            } else if(dataInicial != null) {
                sql.append(" AND c.dataHoraAbertura >= :dataInicial ");                
                return !em.createQuery(sql.toString())
                        .setParameter("setor", setorDTO.getCodigo())                          
                        .setParameter("dataInicial", dataInicial)
                        .getResultList().isEmpty(); 
            } else if(dataFinal != null){
                sql.append(" AND c.dataHoraAbertura <= :dataFinal ");                
                return !em.createQuery(sql.toString())
                        .setParameter("setor", setorDTO.getCodigo())                          
                        .setParameter("dataFinal", dataFinal)
                        .getResultList().isEmpty(); 
            } else {            
                return !em.createQuery(sql.toString())
                        .setParameter("setor", setorDTO.getCodigo())                          
                        .getResultList().isEmpty(); 
            }
        } catch (Exception e) {            
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }        
    }
    
    public boolean verificarSeExisteChamadoStatusSetores(StatusDTO status, UsuarioDTO usuario, Date dataInicial, Date dataFinal){
        EntityManager em = emf.createEntityManager();
        try {       
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT c FROM ChamadoServicoDTO c ");
            sql.append("WHERE c.statusDTO.codigo = :status ");
            sql.append("AND c.servicoDTO.tipoServicoDTO.setorDTO.codigo IN ");
            sql.append("( SELECT usertor.setorDTO.codigo FROM UsuarioSetorDTO usertor ");
            sql.append("WHERE usertor.usuarioDTO.codigo = :usuario )");
            if(dataInicial != null && dataFinal != null) {
                sql.append(" AND c.dataHoraAbertura >= :dataInicial AND c.dataHoraAbertura <= :dataFinal ");                
                return !em.createQuery(sql.toString())
                        .setParameter("status", status.getCodigo())
                        .setParameter("usuario", usuario.getCodigo())
                        .setParameter("dataInicial", dataInicial)
                        .setParameter("dataFinal", dataFinal)
                        .getResultList().isEmpty();
            } else if(dataInicial != null) {
                sql.append(" AND c.dataHoraAbertura >= :dataInicial ");
                return !em.createQuery(sql.toString())
                        .setParameter("status", status.getCodigo())
                        .setParameter("usuario", usuario.getCodigo())
                        .setParameter("dataInicial", dataInicial)                        
                        .getResultList().isEmpty();                
            } else if(dataFinal != null){
                sql.append(" AND c.dataHoraAbertura <= :dataFinal ");
                return !em.createQuery(sql.toString())
                        .setParameter("status", status.getCodigo())
                        .setParameter("usuario", usuario.getCodigo())                        
                        .setParameter("dataFinal", dataFinal)
                        .getResultList().isEmpty();                
            } else {            
                return !em.createQuery(sql.toString())
                        .setParameter("status", status.getCodigo())
                        .setParameter("usuario", usuario.getCodigo())                        
                        .getResultList().isEmpty(); 
            }
        } catch (Exception e) { 
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }     
    
    public String setorCampusIN(){
        StringBuilder retorno = new StringBuilder();
        retorno.append("SELECT usuSetor.setorDTO.codigo ");
        retorno.append("FROM UsuarioSetorDTO as usuSetor, UsuarioDTO as usu ");            
        retorno.append("WHERE usu.codigo = usuSetor.usuarioDTO.codigo ");                
        retorno.append("AND usuSetor.usuarioDTO.codigo = :usuario ");                        
        
        return retorno.toString();
    }
}