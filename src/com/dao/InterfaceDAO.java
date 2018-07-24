package com.dao;

import com.core.GenericDAO;
import com.dto.InterfaceDTO;
import com.dto.UsuarioDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;

public class InterfaceDAO extends GenericDAO<InterfaceDTO> {

    private static final long serialVersionUID = 1011L;
    static final Logger logger = Logger.getLogger(InterfaceDAO.class.getName());
    private volatile static InterfaceDAO uniqueInstance;

    public static InterfaceDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (InterfaceDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new InterfaceDAO();
                }
            }
        }
        return uniqueInstance;
    }

    public InterfaceDAO() {
        super(InterfaceDTO.class);
    }
    
   public List<InterfaceDTO> pesquisarPorUsuarioInterfaceSomentePai(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT p.interfaceDTO ");
            sql.append("FROM UsuarioDTO as u LEFT JOIN u.permissoes as p ");
            sql.append("WHERE u.codigo = :usuario AND p.interfaceDTO.interfaceRecursiva = null ");
            sql.append("ORDER BY p.interfaceDTO.posicaoMenu");
            /*
             StringBuilder sql = new StringBuilder();
             sql.append("SELECT i.*");
             sql.append(" FROM usuario_permissao as us, permissao as pe, interface as i");
             sql.append(" WHERE us.usuario_codigo = :usuario");
             sql.append(" AND us.permissao_codigo = pe.codigo");
             sql.append(" AND pe.interface_codigo = i.codigo");
             sql.append(" ORDER BY i.descricao_completa");                                                           
             */
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .setHint("eclipselink.QUERY_RESULTS_CACHE_TYPE", "FULL")
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<InterfaceDTO> pesquisarPorUsuarioInterfacePorPai(UsuarioDTO usuarioDTO, InterfaceDTO interfacePai) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT DISTINCT p.interfaceDTO ");
            sql.append("FROM UsuarioDTO as u LEFT JOIN u.permissoes as p ");
            sql.append("WHERE u.codigo = :usuario AND p.interfaceDTO.interfaceRecursiva.codigo = :codigoInterface ");
            sql.append("ORDER BY p.interfaceDTO.posicaoMenu");
            /*
             StringBuilder sql = new StringBuilder();
             sql.append("SELECT i.*");
             sql.append(" FROM usuario_permissao as us, permissao as pe, interface as i");
             sql.append(" WHERE us.usuario_codigo = :usuario");
             sql.append(" AND us.permissao_codigo = pe.codigo");
             sql.append(" AND pe.interface_codigo = i.codigo");
             sql.append(" ORDER BY i.descricao_completa");                                                           
             */
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .setParameter("codigoInterface", interfacePai.getCodigo())
                    .setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE)
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<InterfaceDTO> pesquisarPorUsuarioInterface(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p.interfaceDTO ");
            sql.append("FROM UsuarioDTO as u LEFT JOIN u.permissoes as p ");
            sql.append("WHERE u.codigo = :usuario");
            /*
             StringBuilder sql = new StringBuilder();
             sql.append("SELECT i.*");
             sql.append(" FROM usuario_permissao as us, permissao as pe, interface as i");
             sql.append(" WHERE us.usuario_codigo = :usuario");
             sql.append(" AND us.permissao_codigo = pe.codigo");
             sql.append(" AND pe.interface_codigo = i.codigo");
             sql.append(" ORDER BY i.descricao_completa");                                                           
             */
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE)
                    .getResultList();     
        } catch (Exception e) {   
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public boolean pesquisarPorUsuarioInterfaceTemAcesso(UsuarioDTO usuarioDTO, String cdu) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p ");
            sql.append("FROM UsuarioDTO as u, PermissaoDTO as p ");
            sql.append("WHERE u.codigo = :usuario ");
            sql.append("AND p.interfaceDTO.descricaoCurta = '");
            sql.append(cdu);
            sql.append("' AND p.tipoPermissaoDTO.codigo = 1 ");
            sql.append(" AND p MEMBER OF u.permissoes ");

            /*
            StringBuilder query = new StringBuilder();
            query.append("SELECT i.*");
            query.append(" FROM usuario_permissao as up, permissao as pe, interface as i");
            query.append(" WHERE up.usuario_codigo = :usuario");
            query.append(" AND up.permissao_codigo = pe.codigo");
            query.append(" AND pe.interface_codigo = i.codigo");
            query.append(" AND i.descricao_curta = :cdu");                                
            */
            return em.createQuery(sql.toString())                    
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE)
                    .getSingleResult() != null ? true : false;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }

    }

    public boolean verificaVisualizacaoBotao(UsuarioDTO usuarioDTO, String cdu, long op) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();

            sql.append("SELECT p ");
            sql.append("FROM UsuarioDTO as u, PermissaoDTO as p ");
            sql.append("WHERE u.codigo = :usuario ");
            sql.append("AND p.interfaceDTO.descricaoCurta = '");
            sql.append(cdu);
            sql.append("' AND p.tipoPermissaoDTO.codigo = :op");
            sql.append(" AND p MEMBER OF u.permissoes ");
            /*
             sql.append("SELECT pe.*");
             sql.append(" FROM usuario_permissao up, permissao pe, interface i");
             sql.append(" WHERE up.usuario_codigo = :usuario");
             sql.append(" AND up.permissao_codigo = pe.codigo");
             sql.append(" AND pe.interface_codigo = i.codigo");
             sql.append(" AND i.descricao_curta = :cdu");                                 
             sql.append(" AND pe.tipo_permissao_codigo = :op");                        
             */
            return em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .setParameter("op", op)
                    .getSingleResult() != null ? true : false;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public boolean pesquisarPorUsuarioInterfaceTemAcesso(UsuarioDTO usuarioDTO, Long codigoInterface) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT interfaceDTO FROM PermissaoDTO as pdto ");
            sql.append("WHERE pdto.usuarioDTO.codigo = :usuario ");
            sql.append("AND pdto.interfaceDTO.codigo = :codigoInteface ");
            sql.append("ORDER BY pdto.interfaceDTO.descricaoCompleta ");

            return (InterfaceDTO) em.createQuery(sql.toString())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .setParameter("codigoInteface", codigoInterface)
                    .setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE)
                    .getSingleResult() == null ? false : true;                                
                        
        } catch (Exception e) {            
            return false;
        } finally {
            em.close();
        }
    }

    public List<InterfaceDTO> interfacesPais() {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM InterfaceDTO i WHERE i.interfaceRecursiva is null");
            sql.append(" ORDER BY i.codigo");
            return em.createQuery(sql.toString()).setHint("javax.persistence.cache.storeMode", CacheStoreMode.USE).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<InterfaceDTO> interfaceOrganizadosPorPais() {
        List<InterfaceDTO> interfaces = new ArrayList<>();
        try {
            for (InterfaceDTO interfaceDTO : interfacesPais()) {
                interfaces.addAll(pesquisarPorInterfaceRecursiva(interfaceDTO));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
        return interfaces;
    }

    public List<InterfaceDTO> pesquisarPorInterfaceRecursiva(InterfaceDTO interfaceDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM InterfaceDTO i WHERE i.interfaceRecursiva.codigo = ");
            sql.append(interfaceDTO.getCodigo());
            sql.append(" ORDER BY i.posicaoMenu");

            return em.createQuery(sql.toString()).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public InterfaceDTO pesquisarInterfacePorCdu(String cdu) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM InterfaceDTO i WHERE i.descricaoCurta = :cdu");
            return (InterfaceDTO) em.createQuery(sql.toString()).setParameter("cdu", cdu).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}
