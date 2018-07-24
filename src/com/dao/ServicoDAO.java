package com.dao;

import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.ServicoDTO;
import com.dto.TipoServicoDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ServicoDAO extends GenericDAO<ServicoDTO> {

    private static final long serialVersionUID = 1019L;
    static final Logger logger = Logger.getLogger(ServicoDAO.class.getName());
    private volatile static ServicoDAO uniqueInstance;

    public static ServicoDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ServicoDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ServicoDAO();
                }
            }
        }
        return uniqueInstance;
    }

    public ServicoDAO() {
        super(ServicoDTO.class);
    }

    @Override
    public boolean unique(ServicoDTO servicoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ServicoDTO servico ");
            sql.append("WHERE LOWER (servico.descricaoCurta) = '");
            sql.append(servicoDTO.getDescricaoCurta().toLowerCase(new Locale("pt", "BR")));
            sql.append("' AND servico.tipoServicoDTO.codigo = :tipoServico");

            return !em.createQuery(sql.toString())
                    .setParameter("tipoServico", servicoDTO.getTipoServicoDTO().getCodigo())
                    .getResultList()
                    .isEmpty();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<ServicoDTO> pesquisarNome(ServicoDTO servicoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ServicoDTO servico WHERE LOWER (servico.descricaoCompleta) LIKE '%");
            sql.append(servicoDTO.getDescricaoCompleta().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY servico.tipoServicoDTO.descricao, servico.descricaoCurta");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<ServicoDTO> pesquisarNomeECampus(String servicoDTO, CampusDTO campusDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ServicoDTO servico WHERE LOWER (servico.descricaoCompleta) LIKE '%");
            sql.append(servicoDTO.toLowerCase(new Locale("pt", "BR")));
            sql.append("%' AND servico.tipoServicoDTO.setorDTO.campusDTO.codigo = :campus ");
            sql.append("ORDER BY servico.tipoServicoDTO.descricao, servico.descricaoCurta");

            return em.createQuery(sql.toString()).setParameter("campus", campusDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<ServicoDTO> pesquisarServicoPorSetoresDoUsuario(UsuarioDTO usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT s ");
            sql.append("FROM ServicoDTO s ");
            sql.append("WHERE s.tipoServicoDTO.setorDTO.codigo ");
            sql.append("IN ( SELECT us.setorDTO.codigo ");
            sql.append("FROM UsuarioSetorDTO us ");
            sql.append("WHERE us.usuarioDTO.codigo = :usuario )");

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

    public List<ServicoDTO> pesquisarOutrosPorTipoServico(TipoServicoDTO tipoServicoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ServicoDTO servico ");
            sql.append("WHERE servico.tipoServicoDTO.codigo = :tipoServico ");
            sql.append("AND LOWER(servico.descricaoCurta) LIKE 'outr%' ");

            return em.createQuery(sql.toString())
                    .setParameter("tipoServico", tipoServicoDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public ServicoDTO pesquisarPorTipoServicoEDescricao(TipoServicoDTO tipoServicoDTO, ServicoDTO servico) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ServicoDTO servico ");
            sql.append("WHERE servico.tipoServicoDTO.codigo = :tipoServico ");
            sql.append("AND servico.descricaoCurta = '");
            sql.append(servico.getDescricaoCurta());
            sql.append("'");

            return (ServicoDTO) em.createQuery(sql.toString())
                    .setParameter("tipoServico", tipoServicoDTO.getCodigo())
                    .getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<ServicoDTO> pesquisarSemOutrosPorTipoServico(TipoServicoDTO tipoServicoDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ServicoDTO servico ");
            sql.append("WHERE servico.tipoServicoDTO.codigo = :tipoServico ");
            sql.append("AND LOWER(servico.descricaoCurta) NOT LIKE 'outr%' ");
            sql.append("ORDER BY servico.descricaoCurta");

            return em.createQuery(sql.toString())
                    .setParameter("tipoServico", tipoServicoDTO.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<ServicoDTO> pesquisarPorTipoServico(TipoServicoDTO tipoServicoDTO) {
        try {
            List<ServicoDTO> retorno = pesquisarSemOutrosPorTipoServico(tipoServicoDTO);
            retorno.addAll(pesquisarOutrosPorTipoServico(tipoServicoDTO));

            return retorno;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<ServicoDTO> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM ServicoDTO servico ORDER BY servico.descricaoCurta, servico.tipoServicoDTO");
            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}