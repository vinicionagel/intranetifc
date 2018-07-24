package com.dao;


import com.core.GenericDAO;
import com.dto.AlunoDTO;
import com.dto.OcorrenciaDTO;
import com.dto.SetorDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class OcorrenciaDAO extends GenericDAO<OcorrenciaDTO>{
    
    private static final long serialVersionUID = 20013L;   
    private volatile static OcorrenciaDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(OcorrenciaDAO.class.getName());
    
    public static OcorrenciaDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (OcorrenciaDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new OcorrenciaDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public OcorrenciaDAO() {
        super(OcorrenciaDTO.class);
    }      
    
    public List<OcorrenciaDTO> pesquisarPorAluno(AlunoDTO alunoDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM OcorrenciaDTO ocorrencia");
            sql.append(" WHERE (ocorrencia.acaoDisciplinarDTO.codigo > 4");
            sql.append(" OR ((ocorrencia.acaoDisciplinarDTO.codigo = 1 OR ocorrencia.acaoDisciplinarDTO.codigo = 2)");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data1)");
            sql.append(" OR ((ocorrencia.acaoDisciplinarDTO.codigo = 3 OR ocorrencia.acaoDisciplinarDTO.codigo = 4)");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data2))");
            sql.append(" AND ocorrencia.alunoDTO.codigo = :aluno");
            sql.append(" ORDER BY ocorrencia.dataHoraAbertura");
            //cal1 Grave e Gravissima
            Calendar cal1 =  Calendar.getInstance();
            cal1.setTime(new Date());
            cal1.add(Calendar.YEAR, -1);
            //cal2 Leve e Media
            Calendar cal2 =  Calendar.getInstance();
            cal2.setTime(new Date());
            cal2.add(Calendar.YEAR, -3);
            return em.createQuery(sql.toString()).setParameter("aluno", alunoDTO.getCodigo()).setParameter("data1", cal1.getTime()).setParameter("data2", cal2.getTime()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
        
        public List<OcorrenciaDTO> pesquisarPorAlunoIntervalo(AlunoDTO alunoDTO, Date dataInicial, Date dataFinal)  {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM OcorrenciaDTO ocorrencia");
            sql.append(" WHERE ((ocorrencia.statusOcorrenciaDTO.codigo < 4");
            if(dataInicial !=  null && dataFinal != null){
                sql.append(" AND ocorrencia.dataHoraAbertura >= :dataInicial");
                sql.append(" AND ocorrencia.dataHoraAbertura <= :dataFinal");
            }
            sql.append(")");
            sql.append(" OR((ocorrencia.acaoDisciplinarDTO.codigo < 4");
            sql.append(" OR (ocorrencia.acaoDisciplinarDTO.codigo < 3");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data1)");
            sql.append(" OR (ocorrencia.acaoDisciplinarDTO.codigo > 2");
            sql.append(" AND ocorrencia.acaoDisciplinarDTO.tipoOcorrenciaDTO.codigo = 2");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data2))");
            if(dataInicial !=  null && dataFinal != null){
                sql.append(" AND ocorrencia.dataHoraFechamento >= :dataInicial");
                sql.append(" AND ocorrencia.dataHoraFechamento <= :dataFinal");
            }
            sql.append(" AND ocorrencia.statusOcorrenciaDTO.codigo = 4)");
            sql.append(" OR (ocorrencia.statusOcorrenciaDTO.codigo = 4");
             sql.append(" AND ocorrencia.acaoDisciplinarDTO.codigo > 4))");
            sql.append(" AND ocorrencia.alunoDTO.codigo = :aluno");
            sql.append(" ORDER BY ocorrencia.dataHoraAbertura");
            //cal1 Grave e Gravissima
            Calendar cal1 =  Calendar.getInstance();
            cal1.setTime(new Date());
            cal1.add(Calendar.YEAR, -1);
            //cal2 Leve e Media
            Calendar cal2 =  Calendar.getInstance();
            cal2.setTime(new Date());
            cal2.add(Calendar.YEAR, -3);
            //DataFinal
            Calendar calf =  Calendar.getInstance();
            calf.setTime(dataFinal);
            calf.add(Calendar.DAY_OF_YEAR, 1);
            return em.createQuery(sql.toString()).setParameter("aluno", alunoDTO.getCodigo()).setParameter("data1", cal1.getTime()).setParameter("data2", cal2.getTime())
                    .setParameter("dataInicial", dataInicial).setParameter("dataFinal", calf.getTime()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
        
    public List<OcorrenciaDTO> pesquisarObservadas(List<SetorDTO> listaSetorDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            HashSet<OcorrenciaDTO> ocorrenciaDTOs = new HashSet<>();
            for(SetorDTO setorDTO : listaSetorDTO){
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT DISTINCT ocorrencia");
                sql.append(" FROM OcorrenciaDTO ocorrencia");
                sql.append(" WHERE ocorrencia.statusOcorrenciaDTO.codigo = 1");
                sql.append(" AND :setorDTO MEMBER OF ocorrencia.setoresObervadores");
                sql.append(" ORDER BY ocorrencia.dataHoraAbertura");
                ocorrenciaDTOs.addAll(em.createQuery(sql.toString()).setParameter("setorDTO", setorDTO).getResultList());
            } 
            return new ArrayList<OcorrenciaDTO>(ocorrenciaDTOs);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<OcorrenciaDTO> pesquisarObservadasPorAluno(AlunoDTO alunoDTO, List<SetorDTO> listaSetorDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            HashSet<OcorrenciaDTO> ocorrenciaDTOs = new HashSet<>();
            for(SetorDTO setorDTO : listaSetorDTO){
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT DISTINCT ocorrencia");
                sql.append(" FROM OcorrenciaDTO ocorrencia WHERE ocorrencia.alunoDTO.codigo = :aluno");
                sql.append(" AND :setorDTO MEMBER OF ocorrencia.setoresObervadores");
                sql.append(" ORDER BY ocorrencia.dataHoraAbertura");
                ocorrenciaDTOs.addAll(em.createQuery(sql.toString()).setParameter("setorDTO", setorDTO).setParameter("aluno", alunoDTO.getCodigo()).getResultList());
            } 
            return new ArrayList<OcorrenciaDTO>(ocorrenciaDTOs);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<OcorrenciaDTO> pesquisarPorAlunoEAtribuidosParaSetor(AlunoDTO alunoDTO, List<SetorDTO> listaSetorDTO)  {
        EntityManager em = emf.createEntityManager();
        try {
            String lista = gerarListaIN(listaSetorDTO);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT ocorrencia");
            sql.append(" FROM OcorrenciaDTO ocorrencia");
            sql.append(" WHERE (ocorrencia.acaoDisciplinarDTO.codigo > 4");
            sql.append(" OR ((ocorrencia.acaoDisciplinarDTO.codigo = 1 OR ocorrencia.acaoDisciplinarDTO.codigo = 2)");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data1)");
            sql.append(" OR ((ocorrencia.acaoDisciplinarDTO.codigo = 3 OR ocorrencia.acaoDisciplinarDTO.codigo = 4)");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data2))");
            sql.append(" AND ocorrencia.alunoDTO.codigo = :aluno");
            sql.append(" AND (ocorrencia.setorDTOAtribuido.codigo IN (");
            sql.append(lista);
            sql.append(")");
            sql.append(" OR ocorrencia.setorDTOAutor.codigo IN (");
            sql.append(lista);
            sql.append(")) ORDER BY ocorrencia.dataHoraAbertura");
            //cal1 Grave e Gravissima
            Calendar cal1 =  Calendar.getInstance();
            cal1.setTime(new Date());
            cal1.add(Calendar.YEAR, -1);
            //cal2 Leve e Media
            Calendar cal2 =  Calendar.getInstance();
            cal2.setTime(new Date());
            cal2.add(Calendar.YEAR, -3);
            return em.createQuery(sql.toString()).setParameter("aluno", alunoDTO.getCodigo()).setParameter("data1", cal1.getTime()).setParameter("data2", cal2.getTime()).getResultList();       
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
     
    public List<OcorrenciaDTO> pesquisarAtribuidosParaSetor(List<SetorDTO> listaSetorDTO){
        EntityManager em = emf.createEntityManager();
        try {
            String lista = gerarListaIN(listaSetorDTO);
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT ocorrencia");
            sql.append(" FROM OcorrenciaDTO ocorrencia");
            sql.append(" WHERE (ocorrencia.acaoDisciplinarDTO.codigo > 4");
            sql.append(" OR ((ocorrencia.acaoDisciplinarDTO.codigo = 1 OR ocorrencia.acaoDisciplinarDTO.codigo = 2)");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data1)");
            sql.append(" OR ((ocorrencia.acaoDisciplinarDTO.codigo = 3 OR ocorrencia.acaoDisciplinarDTO.codigo = 4)");
            sql.append(" AND ocorrencia.dataHoraFechamento > :data2))");
            sql.append(" AND ocorrencia.statusOcorrenciaDTO.codigo < 3");
            sql.append(" AND (ocorrencia.setorDTOAtribuido.codigo IN (");
            sql.append(lista);
            sql.append(")");
            sql.append(" OR ocorrencia.setorDTOAutor.codigo IN (");
            sql.append(lista);
            sql.append(")) ORDER BY ocorrencia.dataHoraAbertura");
            //cal1 Grave e Gravissima
            Calendar cal1 =  Calendar.getInstance();
            cal1.setTime(new Date());
            cal1.add(Calendar.YEAR, -1);
            //cal2 Leve e Media
            Calendar cal2 =  Calendar.getInstance();
            cal2.setTime(new Date());
            cal2.add(Calendar.YEAR, -3);
            return em.createQuery(sql.toString()).setParameter("data1", cal1.getTime()).setParameter("data2", cal2.getTime()).getResultList();  
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    private String gerarListaIN(List<SetorDTO> lista) {
        String result = "";
        int contador = 0;
        for (SetorDTO c : lista) {
            result = result.concat(c.getCodigo().toString());
            if (lista.size() > (contador + 1)) {
                result = result.concat(",");
            }
            contador++;
        }
        return result;
    }
}
