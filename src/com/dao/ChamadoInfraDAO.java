package com.dao;

import com.auxiliar.Contexto;
import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.ChamadoInfraDTO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.SetorDTO;
import com.dto.StatusDTO;
import com.dto.TipoPatrimonioDTO;
import com.dto.UsuarioDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class ChamadoInfraDAO extends GenericDAO<ChamadoInfraDTO> {
    
    private static final long serialVersionUID = 1006L;
    static final Logger logger = Logger.getLogger(ChamadoInfraDAO.class.getName());
    private volatile static ChamadoInfraDAO uniqueInstance;
    
    public ChamadoInfraDAO() {
        super(ChamadoInfraDTO.class);
    }               
            
    public static ChamadoInfraDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (ChamadoInfraDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new ChamadoInfraDAO();
                }
            }
        }
        return uniqueInstance;
    }  
    
    public ChamadoInfraDTO pesquisarSeChamadoPertenceUsuario(UsuarioDTO usuario, long id){
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();   
            sql.append("SELECT chInfra ");
            sql.append("FROM ChamadoInfraDTO as chInfra ");
            sql.append("WHERE chInfra.codigo = :id AND (chInfra.patrimonioDTO.setorDTO.codigo ");
            sql.append("IN  (");
                sql.append(setorCampusIN2());
                sql.append(" ) ");
            sql.append("OR chInfra.usuarioAutorDTO.codigo = :usuarioCodigo  ");
            sql.append("OR chInfra.usuarioAtribuidoDTO.codigo = :usuarioCodigo  ");
            sql.append("OR :usuarioCodigo IN (SELECT u.codigo FROM chInfra.observadores as u)) ");
            return (ChamadoInfraDTO) em.createQuery(sql.toString())
                    .setParameter("usuarioCodigo", usuario.getCodigo())
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) { 
            return null;       
        } finally {
            em.close();
        }
    }
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorTitulo(String titulo, Contexto contexto) {  
        EntityManager em = emf.createEntityManager();
        try {                                                                        
            StringBuilder sql = new StringBuilder();            
            sql.append("SELECT DISTINCT chInfra ");
            sql.append("FROM ChamadoInfraDTO as chInfra ");            
            sql.append("WHERE LOWER(chInfra.titulo) LIKE '%");
            sql.append(titulo.toLowerCase());
            sql.append("%' ");
            sql.append("AND (chInfra.patrimonioDTO.setorDTO.codigo ");        
            sql.append("IN ( ");
                sql.append(setorCampusIN());
                sql.append(" ) ");
            sql.append("OR chInfra.usuarioAutorDTO.codigo = :usuario) ");
            sql.append("AND chInfra.patrimonioDTO.setorDTO.campusDTO.codigo = :campus ");
            sql.append("AND chInfra.statusDTO.codigo != 6");                       
            
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorSetorUsuario(Contexto contexto) {        
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder sql = new StringBuilder();            
            sql.append("SELECT chInfra ");
            sql.append("FROM ChamadoInfraDTO as chInfra ");            
            sql.append("WHERE (chInfra.patrimonioDTO.setorDTO.codigo ");    
            sql.append("IN ( ");
                sql.append(setorCampusIN());
                sql.append(" ) ");
            sql.append("OR chInfra.usuarioAutorDTO.codigo = :usuario) ");
            sql.append("AND chInfra.patrimonioDTO.setorDTO.campusDTO.codigo = :campus ");
            sql.append("AND chInfra.statusDTO.codigo != 6 "); 
            sql.append("ORDER BY chInfra.dataHoraAbertura DESC");
            
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraObservados(Contexto contexto) {
        try {
            List<ChamadoInfraDTO> lista = new ArrayList<>();                                      
            List<ChamadoInfraDTO> listaTodos = findAll();            
          
            for(ChamadoInfraDTO chInfra : listaTodos){
                if(chInfra.getObservadores().contains(contexto.getUsuarioLogado()) 
                        && chInfra.getPatrimonioDTO().getSetorDTO().getCampusDTO().getCodigo().equals(contexto.getCampusAtual().getCodigo())){
                    lista.add(chInfra);
                }
            }            
            return lista;            
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }    
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraAtribuidos(Contexto contexto) {
       EntityManager em = emf.createEntityManager();
       try {
            return em.createQuery("FROM ChamadoInfraDTO chamado WHERE chamado.usuarioAtribuidoDTO.codigo = :usuario AND chamado.patrimonioDTO.setorDTO.campusDTO.codigo = :campus")
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraAtribuidosEntreDatas(UsuarioDTO usuario, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM ChamadoInfraDTO AS chInfra WHERE chInfra.usuarioAtribuidoDTO.codigo = :usuario AND chInfra.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorPatrimonioEntreDatas(PatrimonioDTO patrimonio, Date dataInicial, Date dataFinal){
        EntityManager em = emf.createEntityManager();
        try {            
            return em.createQuery("FROM ChamadoInfraDTO AS chInfra WHERE chInfra.patrimonioDTO.codigo = :patrimonio AND chInfra.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
                    .setParameter("patrimonio", patrimonio.getCodigo())
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
    
    public List<ChamadoInfraDTO> pesquisarEquipamentosEmManutencaoEntreDatasContexto(Date dataInicial, Date dataFinal, String setores) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ChamadoInfraDTO AS chInfra WHERE chInfra.statusDTO.codigo = 7 AND chInfra.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal AND chInfra.patrimonioDTO.setorDTO.codigo IN (");
            sql.append(setores);
            sql.append(")");
            return em.createQuery(sql.toString())
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorTipoPatrimonioEntreDatas(TipoPatrimonioDTO tipoPatrimonio, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM ChamadoInfraDTO AS chInfra WHERE chInfra.patrimonioDTO.tipoPatrimonioDTO.codigo = :tipoPatrimonio AND chInfra.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
                    .setParameter("tipoPatrimonio", tipoPatrimonio.getCodigo())
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorSetorEntreDatas(SetorDTO setor, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("FROM ChamadoInfraDTO AS chInfra WHERE chInfra.patrimonioDTO.setorDTO.codigo = :setor AND chInfra.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorCampusEntreDatas(CampusDTO campus, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {                                    
            return em.createQuery("FROM ChamadoInfraDTO AS chInfra WHERE chInfra.patrimonioDTO.setorDTO.campusDTO.codigo = :campus AND chInfra.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
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
    
    public List<ChamadoInfraDTO> pesquisarMeusChamadosInfra(Contexto contexto) {
       EntityManager em = emf.createEntityManager();
       try {            
            return em.createQuery("FROM ChamadoInfraDTO chamado WHERE chamado.usuarioAutorDTO.codigo = :usuario AND chamado.patrimonioDTO.setorDTO.campusDTO.codigo = :campus")
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosInfraPeloFiltro(ChamadoInfraDTO chamado, SetorDTO setor, Contexto contexto, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {                    
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT chInfra ");
            sql.append("FROM ChamadoInfraDTO as chInfra ");
            sql.append("WHERE (chInfra.patrimonioDTO.setorDTO.codigo ");
            sql.append("IN ( ");
                sql.append(setorCampusIN());
                sql.append(" ) ");                
            sql.append("OR chInfra.usuarioAutorDTO.codigo = :usuario) ");
            sql.append("AND chInfra.patrimonioDTO.setorDTO.campusDTO.codigo = :campus ");

            if(chamado.getUsuarioAtribuidoDTO() != null){
                sql.append(" AND chInfra.usuarioAtribuidoDTO.codigo = ");
                sql.append(chamado.getUsuarioAtribuidoDTO().getCodigo());
            }

            if(chamado.getPatrimonioDTO() != null){
                sql.append(" AND chInfra.patrimonioDTO.codigo = ");
                sql.append(chamado.getPatrimonioDTO().getCodigo());
            }

            if(chamado.getStatusDTO() != null){
                sql.append(" AND chInfra.statusDTO.codigo = ");
                sql.append(chamado.getStatusDTO().getCodigo());
            }

            if(setor != null){
                sql.append(" AND chInfra.patrimonioDTO.setorDTO.codigo = ");
                sql.append(setor.getCodigo());
            }

            if(chamado.getPrioridadeDTO() !=  null){
                sql.append(" AND chInfra.prioridadeDTO = ");
                sql.append(chamado.getPrioridadeDTO().getCodigo());            
            }                                      
                                    
            if(dataInicial != null) {
                sql.append(" AND chInfra.dataHoraAbertura >= :dataInicial ");
            } else if(dataFinal != null){                
                sql.append(" AND chInfra.dataHoraAbertura <= :dataFinal ");                
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
    
    public List<ChamadoInfraDTO> pesquisarChamadosPorStatusPorSetor(StatusDTO status, SetorDTO setor, Date dataInicial, Date dataFinal) {        
        EntityManager em = emf.createEntityManager();
        try {                        
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT c ");
            sql.append("FROM ChamadoInfraDTO c ");
            sql.append("WHERE c.statusDTO.codigo = :status ");
            sql.append("AND c.patrimonioDTO.setorDTO.codigo = :setor ");  
            
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
            sql.append("SELECT c FROM ChamadoInfraDTO c WHERE c.patrimonioDTO.setorDTO.codigo = :setor");
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
            sql.append("SELECT c FROM ChamadoInfraDTO c ");
            sql.append("WHERE c.statusDTO.codigo = :status ");
            sql.append("AND c.patrimonioDTO.setorDTO.codigo IN ");
            sql.append("( SELECT usuarioSetorPK.setorDTO.codigo FROM UsuarioSetorDTO ");
            sql.append("WHERE usuarioSetorPK.usuarioDTO.codigo = :usuario ) ");
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

    public List<ChamadoInfraDTO> pesquisarChamadosInfraPorLocalizacaoEntreDatas(LocalizacaoDTO localizacao, Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try {            
            return em.createQuery("FROM ChamadoInfraDTO AS chInfra WHERE chInfra.patrimonioDTO.localizacaoDTO.codigo = :localizacao AND chInfra.dataHoraAbertura BETWEEN :dataInicial AND :dataFinal")
                    .setParameter("localizacao", localizacao.getCodigo())
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
    
    public String setorCampusIN(){
        StringBuilder retorno = new StringBuilder();
        retorno.append("SELECT usuSetor.setorDTO.codigo ");
        retorno.append("FROM UsuarioSetorDTO as usuSetor, UsuarioDTO as usu ");            
        retorno.append("WHERE usu.codigo = usuSetor.usuarioDTO.codigo ");                
        retorno.append("AND usuSetor.usuarioDTO.codigo = :usuario "); 
        return retorno.toString();
    }
    
    public String setorCampusIN2(){
        StringBuilder retorno = new StringBuilder();
        retorno.append("SELECT usuSetor.setorDTO.codigo ");
        retorno.append("FROM UsuarioSetorDTO as usuSetor ");              
        retorno.append("WHERE usuSetor.usuarioDTO.codigo = :usuarioCodigo "); 
        return retorno.toString();
    }
}