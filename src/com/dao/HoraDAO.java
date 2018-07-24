package com.dao;

import com.core.GenericDAO;
import com.dto.ChamadoDTO;
import com.dto.HoraDTO;
import com.dto.UsuarioDTO;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class HoraDAO extends GenericDAO<HoraDTO> {
    
    private static final long serialVersionUID = 1000L;
    static final Logger logger = Logger.getLogger(HoraDAO.class.getName());
    private volatile static HoraDAO uniqueInstance;
    
    public static HoraDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (HoraDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new HoraDAO();
                }
            }
        }
        return uniqueInstance;
    }  

    public HoraDAO() {
        super(HoraDTO.class);
    }
    
    public float tempoGastoPorChamado(ChamadoDTO chamadoDTO) {
        EntityManager em = emf.createEntityManager();
        try {                        
            Double retorno = (Double) em.createQuery(" SELECT SUM(hora.tempo) FROM HoraDTO hora WHERE hora.chamadoDTO.codigo = :chamado")
                    .setParameter("chamado", chamadoDTO.getCodigo())
                    .getSingleResult();
            return retorno != null ? retorno.floatValue() : 0.0f;
        } catch (NullPointerException e) {         
            logger.log(Level.SEVERE, e.getMessage(), e);
            return 0;
        } finally {
            em.close();
        }        
    }
    
    public List<HoraDTO> pesquisarHorasTrabalhadasEntreDatas(Date dataInicial, Date dataFinal) {
        EntityManager em = emf.createEntityManager();
        try{    
            return em.createQuery("FROM HoraDTO hora WHERE hora.dataTrabalho BETWEEN :dataInicial AND :dataFinal")
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
    
    public List<HoraDTO> horasPorChamado(ChamadoDTO chamadoDTO) {
        EntityManager em = emf.createEntityManager();
        try {            
            return em.createQuery("FROM HoraDTO hora WHERE hora.chamadoDTO.codigo = :chamado")
                    .setParameter("chamado", chamadoDTO.getCodigo())
                    .getResultList();                                                               
        } catch (Exception e) {      
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public float consultarHorasSeteDias(UsuarioDTO usuario)  {
        EntityManager em = emf.createEntityManager();
        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -7);
            
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SUM(hora.tempo) FROM HoraDTO hora");
            sql.append(" WHERE hora.usuarioDTO.codigo = :usuario ");
            sql.append(" AND hora.dataTrabalho > :data ");
            sql.append(" AND hora.dataTrabalho <= CURRENT_DATE ");
            
            Double retorno = (Double) em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("data", c.getTime()).getSingleResult();
            if(retorno == null) {
                return 0;
            }            
            return retorno.floatValue();            
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return 0;
        } finally {
            em.close();
        }        
    }
    
    public float consultarHorasMesAnterior(UsuarioDTO usuario)  {
        EntityManager em = emf.createEntityManager();
        try {
            Calendar c0 = Calendar.getInstance();
            // seta para primeiro dia do mes anterior
            c0.add(Calendar.MONTH,  -1);
            c0.set(Calendar.DAY_OF_MONTH, 1);
            
            Calendar c1 = Calendar.getInstance();
            c1.set(Calendar.DAY_OF_MONTH, 1); // seta para primeiro dia desse mes
            
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SUM(hora.tempo) FROM HoraDTO hora ");
            sql.append(" WHERE hora.usuarioDTO.codigo = :usuario ");
            sql.append(" AND hora.dataTrabalho >= :data0 ");
            sql.append(" AND hora.dataTrabalho < :data1 " );
            
            Double retorno = (Double) em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("data0", c0.getTime())
                    .setParameter("data1", c1.getTime()).getSingleResult();
            
            if(retorno == null) {
                return 0;
            }            
            return retorno.floatValue();            
        } catch (NullPointerException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return 0;
        } finally {
            em.close();
        }
    }
    
    public float consultarHorasMesAtual(UsuarioDTO usuario)  {
        EntityManager em = emf.createEntityManager();
        try {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH, 1);
            
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT SUM(hora.tempo) FROM HoraDTO hora ");
            sql.append(" WHERE hora.usuarioDTO.codigo = ");
            sql.append(usuario.getCodigo());
            sql.append(" AND hora.dataTrabalho >= :data");
            sql.append(" AND hora.dataTrabalho <= CURRENT_DATE ");
            
            Double retorno = (Double) em.createQuery(sql.toString()).setParameter("data", c.getTime()).getSingleResult();
            
            if(retorno == null) {
                return 0;
            }            
            return retorno.floatValue();            
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return 0;
        } finally {
            em.close();
        }
    }
    
    public List<HoraDTO> listarHorasSemana(UsuarioDTO usuario)  {
        EntityManager em = emf.createEntityManager();
        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -7);
            
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM HoraDTO hora");
            sql.append(" WHERE hora.usuarioDTO.codigo = ");
            sql.append(usuario.getCodigo());
            sql.append(" AND hora.dataTrabalho > :data");
            sql.append(" AND hora.dataTrabalho <= CURRENT_DATE");
            sql.append(" ORDER BY hora.dataTrabalho DESC, hora.chamadoDTO.codigo");
            
            return em.createQuery(sql.toString()).setParameter("data", c.getTime()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}