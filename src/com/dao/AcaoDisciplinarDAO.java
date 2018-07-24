package com.dao;

import com.core.GenericDAO;
import com.dto.AcaoDisciplinarDTO;
import com.dto.TipoOcorrenciaDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class AcaoDisciplinarDAO extends GenericDAO<AcaoDisciplinarDTO> {

    private static final long serialVersionUID = 20002L;
    private volatile static AcaoDisciplinarDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(AcaoDisciplinarDAO.class.getName());

    public static AcaoDisciplinarDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (AcaoDisciplinarDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new AcaoDisciplinarDAO();
                }
            }
        }
        return uniqueInstance;
    }

    public AcaoDisciplinarDAO() {
        super(AcaoDisciplinarDTO.class);
    }

    public List<AcaoDisciplinarDTO> pesquisarAcaoDisciplinarPorTipoOcorrencia(TipoOcorrenciaDTO tipoOcorrencia) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" FROM AcaoDisciplinarDTO ad");
            sql.append(" WHERE ad.tipoOcorrenciaDTO.codigo = :tipoOcorrencia ");
            return em.createQuery(sql.toString())
                    .setParameter("tipoOcorrencia", tipoOcorrencia.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<AcaoDisciplinarDTO> pesquisarNome(String acaoDisciplinarDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM AcaoDisciplinarDTO acao WHERE LOWER (acao.descricao) LIKE '%");
            sql.append(acaoDisciplinarDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY acao.descricao");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public boolean unique(AcaoDisciplinarDTO acaoDisciplinarDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("Oie");
            StringBuilder sql = new StringBuilder();
            sql.append("FROM AcaoDisciplinarDTO as c WHERE LOWER (c.descricao) = '");
            sql.append(acaoDisciplinarDTO.getDescricao().toLowerCase(new Locale("pt", "BR")));
            sql.append("' AND c.tipoOcorrenciaDTO.codigo = :codigoTipoOcorrencia");
            return !em.createQuery(sql.toString()).
                    setParameter("codigoTipoOcorrencia", acaoDisciplinarDTO.getTipoOcorrenciaDTO().getCodigo()).
                    getResultList().isEmpty();
        } catch (Throwable e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }
}
