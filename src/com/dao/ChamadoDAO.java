package com.dao;

import com.auxiliar.Contexto;
import com.core.GenericDAO;
import com.dto.ChamadoDTO;
import com.dto.UsuarioDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ChamadoDAO extends GenericDAO<ChamadoDTO> {

    private static final long serialVersionUID = 1005L;    
    static final Logger logger = Logger.getLogger(ChamadoDAO.class.getName());
    private volatile static ChamadoDAO uniqueInstance;

    public static ChamadoDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ChamadoDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ChamadoDAO();
                }
            }
        }
        return uniqueInstance;
    }

    public ChamadoDAO() {
        super(ChamadoDTO.class);
    }

    public List<ChamadoDTO> pesquisarChamadosProximaSemana(UsuarioDTO usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 7);
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM ChamadoDTO chamado");
            sql.append(" WHERE chamado.usuarioAtribuidoDTO.codigo = :usuario ");
            sql.append(" AND chamado.dataPrevista >= :data0 ");
            sql.append(" AND chamado.dataPrevista < :data1 ");
            sql.append(" AND chamado.statusDTO.codigo <= 3 "); // apenas chamados "abertos"
            sql.append(" ORDER BY chamado.dataPrevista ASC, chamado.prioridadeDTO.descricao DESC, chamado.codigo");
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .setParameter("data0", Calendar.getInstance().getTime())
                    .setParameter("data1", c.getTime()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }   
    }

    public List<ChamadoDTO> pesquisarMeusChamados(Contexto contexto) {
        EntityManager em = emf.createEntityManager();
        try {
            // Seleciona os 3 últimos chamados infra fechados
            StringBuilder sql0 = new StringBuilder();
            sql0.append("FROM ChamadoInfraDTO chamado ");
            sql0.append("WHERE chamado.patrimonioDTO.setorDTO.campusDTO.codigo = :campus ");
            sql0.append("AND chamado.statusDTO.codigo >= 4 ");
            sql0.append("AND chamado.usuarioAutorDTO.codigo = :usuario ");
            sql0.append("ORDER BY chamado.dataHoraFechamento DESC");
            
            Query q0 = em.createQuery(sql0.toString());
            q0.setParameter("usuario", contexto.getUsuarioLogado().getCodigo());
            q0.setParameter("campus", contexto.getCampusAtual().getCodigo());
            q0.setMaxResults(3);
            
            // Seleciona os 3 últimos chamados serv fechados
            StringBuilder sql1 = new StringBuilder();
            sql1.append("FROM ChamadoServicoDTO chamado ");
            sql1.append("WHERE chamado.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus ");
            sql1.append("AND chamado.statusDTO.codigo >= 4 ");
            sql1.append("AND chamado.usuarioAutorDTO.codigo = :usuario ");
            sql1.append("ORDER BY chamado.dataHoraFechamento DESC");
            
            Query q1 = em.createQuery(sql1.toString());
            q1.setParameter("usuario", contexto.getUsuarioLogado().getCodigo());
            q1.setParameter("campus", contexto.getCampusAtual().getCodigo());
            q1.setMaxResults(3);
            
            // Seleciona todos os chamados infra abertos pelo usuário logado não fechados
            StringBuilder sql2 = new StringBuilder();
            sql2.append("FROM ChamadoInfraDTO chamado ");
            sql2.append("WHERE chamado.patrimonioDTO.setorDTO.campusDTO.codigo = :campus ");
            sql2.append("AND chamado.statusDTO.codigo < 4 ");
            sql2.append("AND chamado.usuarioAutorDTO.codigo = :usuario ");
            
            Query q2 = em.createQuery(sql2.toString());
            q2.setParameter("usuario", contexto.getUsuarioLogado().getCodigo());
            q2.setParameter("campus", contexto.getCampusAtual().getCodigo());
            
            // Seleciona todos os chamados infra abertos pelo usuário logado não fechados
            StringBuilder sql3 = new StringBuilder();
            sql3.append("FROM ChamadoServicoDTO chamado ");
            sql3.append("WHERE chamado.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus ");
            sql3.append("AND chamado.statusDTO.codigo < 4 ");
            sql3.append("AND chamado.usuarioAutorDTO.codigo = :usuario ");
            
            Query q3 = em.createQuery(sql3.toString());
            q3.setParameter("usuario", contexto.getUsuarioLogado().getCodigo());
            q3.setParameter("campus", contexto.getCampusAtual().getCodigo());
            StringBuilder sql = new StringBuilder();
            
            sql.append("FROM ChamadoDTO chamado ");
            
            if (q0.getResultList().size() + q1.getResultList().size() + q2.getResultList().size() + q3.getResultList().size() > 0) {
                StringBuilder sql4 = new StringBuilder();
                sql.append("WHERE chamado.codigo IN ( ");
                if(!gerarListaIN(q0.getResultList()).isEmpty()){
                    sql4.append(gerarListaIN(q0.getResultList()));
                    sql4.append(",");
                }
                if(!gerarListaIN(q1.getResultList()).isEmpty()){
                    sql4.append(gerarListaIN(q1.getResultList()));
                    sql4.append(",");
                }
                if(!gerarListaIN(q2.getResultList()).isEmpty()){
                    sql4.append(gerarListaIN(q2.getResultList()));
                    sql4.append(",");
                }
                if(!gerarListaIN(q3.getResultList()).isEmpty()){
                    sql4.append(gerarListaIN(q3.getResultList()));
                }
                String ids = sql4.toString().trim();
                if (ids.lastIndexOf(",") == ids.length() - 1) {
                    ids = ids.substring(0, ids.length() - 1);
                }
                if (ids.indexOf(",") == 0) {
                    ids = ids.substring(1);
                }
                if (ids.startsWith(",")){
                    ids = ids.replaceFirst(",", "");
                }
                sql.append(ids);
                sql.append(" ) ");
            } else {
                return new ArrayList<>();
            }
            
            sql.append(" ORDER BY chamado.dataHoraAbertura DESC");
            return em.createQuery(sql.toString()).getResultList();
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }

    }

    private String gerarListaIN(List<ChamadoDTO> lista) {
        String result = "";
        int contador = 0;
        for (ChamadoDTO c : lista) {
            result = result.concat(c.getCodigo().toString());
            if (lista.size() > (contador + 1)) {
                result = result.concat(",");
            }
            contador++;
        }
        return result;
    }

    public String pesquisarTituloChamado(ChamadoDTO c) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT chamado.titulo FROM ChamadoDTO chamado WHERE chamado.codigo = :chamado ")
                    .setParameter("chamado", c.getCodigo()).getSingleResult().toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}