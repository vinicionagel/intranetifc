package com.dao;

import com.core.GenericDAO;
import com.dto.CampusDTO;
import com.dto.ChamadoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class UsuarioDAO extends GenericDAO<UsuarioDTO> {

    private static final long serialVersionUID = 1025L;
    static final Logger logger = Logger.getLogger(UsuarioDAO.class.getName());
    private volatile static UsuarioDAO uniqueInstance;    
    
    public static UsuarioDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (UsuarioDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new UsuarioDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    
    public UsuarioDAO() {
        super(UsuarioDTO.class);
    } 
   
    public UsuarioDTO pesquisarPorLogin (UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {                        
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioDTO user WHERE user.login = '");
            sql.append(usuarioDTO.getLogin());
            sql.append("'");
            return(UsuarioDTO) em.createQuery(sql.toString()).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UsuarioDTO> pesquisarNome(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {                                  
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioDTO user WHERE LOWER (user.nome) LIKE '%");
            sql.append(usuarioDTO.getNome().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' ORDER BY user.nome");
            
            return em.createQuery(sql.toString()).getResultList();                        
        } catch (Exception e) {   
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<UsuarioDTO> pesquisarPorNomeUsuariosPeloCampus(UsuarioDTO usuarioDTO, CampusDTO campus) {
        EntityManager em = emf.createEntityManager();
        try {                                  
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioDTO user WHERE LOWER (user.nome) LIKE '%");
            sql.append(usuarioDTO.getNome().toLowerCase(new Locale("pt", "BR")));
            sql.append("%' AND user.codigo in (SELECT us.usuarioDTO.codigo FROM UsuarioSetorDTO us WHERE us.setorDTO.campusDTO.codigo = :campus)");
            sql.append(" ORDER BY user.nome ");
            return em.createQuery(sql.toString())
                    .setParameter("campus", campus.getCodigo())
                    .getResultList();                                    
        } catch (Exception e) {       
            return null;
        } finally {
            em.close();
        }
    }
    
     public UsuarioDTO pesquisarEmailUsuario(UsuarioDTO usuarioDTO) {
         EntityManager em = emf.createEntityManager();
         try {
             StringBuilder sql = new StringBuilder();
             sql.append("FROM UsuarioDTO user WHERE LOWER (user.email)='");
             sql.append(usuarioDTO.getEmail().toLowerCase(new Locale("pt", "BR")));
             sql.append("'");

             return (UsuarioDTO) em.createQuery(sql.toString()).getSingleResult();
         } catch (Exception e) {
             logger.log(Level.SEVERE, e.getMessage(), e);
             return null;
         } finally {
            em.close();
        }
    }
     
    public UsuarioDTO pesquisarPorLoginESenha(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {                                    
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioDTO user WHERE LOWER (user.login)='");
            sql.append(usuarioDTO.getLogin().toLowerCase(new Locale("pt", "BR")));
            sql.append("'");            
            UsuarioDTO obj = (UsuarioDTO) em.createQuery(sql.toString()).getSingleResult();
            if (obj != null && obj.getSenha().equals(usuarioDTO.getSenha())) {
                return obj;
            } else {
                return null;
            }                    
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
      
    public UsuarioDTO pesquisarVerificacao(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {                                    
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioDTO user WHERE LOWER (user.verificacao)='");
            sql.append(usuarioDTO.getVerificacao().toLowerCase(new Locale("pt", "BR")));
            sql.append("'");
            return (UsuarioDTO) em.createQuery(sql.toString()).getSingleResult();                        
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }     
    
    public List<UsuarioDTO> consultarUsuariosCampusChamado(ChamadoDTO chamadoDTO) {
        EntityManager em = emf.createEntityManager();
        try {                        
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT DISTINCT us.usuarioDTO ");
            sql.append(" FROM UsuarioSetorDTO us ");
            sql.append(" WHERE us.setorDTO.camposDTO.codigo ");
            sql.append(" IN ( SELECT ci.patrimonioDTO.localizacaoDTO.camposDTO.codigo ");
                sql.append(" FROM ChamadoInfraDTO ci ");
                sql.append(" WHERE ci.codigo = :chamado ");
                    sql.append(" UNION ");                
                sql.append(" SELECT cs.servicoDTO.tipoServicoDTO.setorDTO.campusDTO.codigo ");
                sql.append(" FROM ChamadoServicoDTO cs ");
                sql.append(" WHERE cs.codigo = :chamado )");                                
            
            return em.createQuery(sql.toString())
                    .setParameter("chamado", chamadoDTO.getCodigo())
                    .getResultList();
            /*
            lista = session.createSQLQuery(" SELECT DISTINCT usuario.* "+
                                           " FROM usuario, setor, usuario_setor, campus "+
                                           " WHERE usuario.codigo = usuario_setor.usuario_codigo "+
                                           " AND usuario_setor.setor_codigo = setor.codigo "+
                                           " AND setor.campus_codigo = campus.codigo "+
                                           " AND setor.campus_codigo "+
                                           " IN ( SELECT localizacao.campus_codigo "+
                                           " FROM chamado, patrimonio, localizacao "+
                                           " WHERE chamado.codigo = :chamado"+                    
                                           " AND chamado.patrimonio_codigo = patrimonio.codigo "+
                                           " AND patrimonio.localizacao_codigo = localizacao.codigo ) ")
                                            .addEntity(UsuarioDTO.class)
                                            .setParameter("chamado", chamadoDTO.getCodigo())
                                            .getResultList();  
             * 
             */
        } catch (Exception e) {    
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }        
    
    public List<UsuarioDTO> consultarUsuariosCampusUsuarioAutorChamado(ChamadoDTO chamadoDTO) {
        EntityManager em = emf.createEntityManager();
        try {            
            StringBuilder query = new StringBuilder();
            query.append(" SELECT DISTINCT u ");
            query.append(" FROM ChamadoDTO as c, UsuarioSetorDTO as us, UsuarioDTO as u ");            
            query.append(" WHERE us.usuarioDTO.estado = 1 ");
            query.append(" AND us.usuarioDTO.codigo = u.codigo ");
            query.append(" AND ( us.setorDTO.campusDTO.codigo");
            query.append(" IN (SELECT usuSetor.setorDTO.campusDTO.codigo ");
                query.append(" FROM UsuarioSetorDTO usuSetor, ChamadoDTO c ");
                query.append(" WHERE usuSetor.usuarioDTO.codigo = c.usuarioAutorDTO.codigo ");
                query.append(" AND c.codigo = :chamado ) ) ");                     
            query.append("ORDER BY u.nome");    
            return em.createQuery(query.toString())
                    .setParameter("chamado", chamadoDTO.getCodigo())
                    .getResultList();                        
        } catch (Exception e) { 
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }

    public List<UsuarioDTO> consultarUsuarioMesmoCampusESemCampus(UsuarioDTO usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder query = new StringBuilder();
            query.append(" SELECT DISTINCT u ");
            query.append(" FROM UsuarioDTO as u ");
            query.append(" WHERE u.estado = 1 ");
            query.append(" AND u.codigo IN ( ");
            query.append(" SELECT us.usuarioDTO.codigo ");
            query.append(" FROM UsuarioSetorDTO as us ");
            query.append(" WHERE us.setorDTO.campusDTO.codigo ");
            query.append(" IN ( SELECT uss.setorDTO.campusDTO.codigo ");
            query.append(" FROM UsuarioSetorDTO as uss ");
            query.append(" WHERE uss.usuarioDTO.codigo = :usuario ))");
            query.append(" OR u.codigo ");
            query.append(" NOT IN ( SELECT us.usuarioDTO.codigo FROM UsuarioSetorDTO as us )");
            query.append(" ORDER BY u.nome ");

            return em.createQuery(query.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<UsuarioDTO> consultarUsuariosMesmoCampus(UsuarioDTO usuario) {
        EntityManager em = emf.createEntityManager();
        try {                                    
            StringBuilder query = new StringBuilder();
            
            query.append(" SELECT DISTINCT us.usuarioDTO ");
            query.append(" FROM UsuarioSetorDTO as us ");                        
            query.append(" WHERE us.setorDTO.campusDTO.codigo ");
            query.append(" IN ( SELECT uss.setorDTO.campusDTO.codigo ");
                query.append(" FROM UsuarioSetorDTO as uss ");
                query.append(" WHERE uss.usuarioDTO.codigo = :usuario )"); 
            query.append(" AND us.usuarioDTO.estado = 1 ");                        
                                        
             return em.createQuery(query.toString())
                    .setParameter("usuario", usuario.getCodigo())
                    .getResultList();            
        } catch (Exception e) {       
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<UsuarioDTO> consultarUsuariosSemCampus() {
        EntityManager em = emf.createEntityManager();
        try {                                    
            StringBuilder query = new StringBuilder();                        
            query.append(" SELECT DISTINCT u FROM UsuarioDTO u ");
            query.append(" WHERE u.codigo ");
            query.append(" NOT IN ( SELECT us.usuarioDTO.codigo FROM UsuarioSetorDTO as us )");
            query.append(" AND u.estado = 1 ");            
                                        
            return em.createQuery(query.toString())                    
                    .getResultList();                        
        } catch (Exception e) {  
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public boolean loginUnico(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioDTO user ");
            sql.append("WHERE LOWER (user.login) = '");
            sql.append(usuarioDTO.getLogin().toLowerCase(new Locale("pt", "BR")));
            sql.append("'");
            
            return !em.createQuery(sql.toString()).getResultList().isEmpty();     
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean emailUnico(UsuarioDTO usuarioDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioDTO user ");
            sql.append("WHERE LOWER (user.email) = '");
            sql.append(usuarioDTO.getEmail().toLowerCase(new Locale("pt", "BR")));
            sql.append("'");
            
            return !em.createQuery(sql.toString()).getResultList().isEmpty();  
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }
    
    @Override
    public List<UsuarioDTO> findAll(){
        EntityManager em = emf.createEntityManager();
        try {                                    
            StringBuilder query = new StringBuilder();
            query.append("FROM UsuarioDTO user ORDER BY user.nome");
            return em.createQuery(query.toString()).getResultList();                        
        } catch (Exception e) {    
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<UsuarioDTO> pesquisarTodosAtivos(){
        EntityManager em = emf.createEntityManager();
        try {                                    
            StringBuilder query = new StringBuilder();
            query.append("FROM UsuarioDTO user WHERE user.estado = 1 ORDER BY user.nome");
            return em.createQuery(query.toString()).getResultList();                        
        } catch (Exception e) {   
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
            
    public List<UsuarioDTO> pesquisarAdmin(CampusDTO campus) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT DISTINCT us.usuarioDTO ");
            sql.append(" FROM UsuarioSetorDTO us ");
            sql.append(" WHERE us.setorDTO.campusDTO.codigo = :campus ");
            sql.append(" AND LOWER (us.setorDTO.descricao) = 'administrador' ");
            
            return em.createQuery(sql.toString())
                    .setParameter("campus", campus.getCodigo())
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    
    public boolean validaAdmin(CampusDTO campus, UsuarioDTO usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT us.usuarioDTO ");
            sql.append(" FROM UsuarioSetorDTO us ");
            sql.append(" WHERE us.usuarioDTO.codigo = :usuario ");
            sql.append(" AND us.setorDTO.campusDTO.codigo = :campus ");
            sql.append(" AND LOWER (us.setorDTO.descricao) = 'administrador' ");
            
            return em.createQuery(sql.toString())
                    .setParameter("campus", campus.getCodigo())
                    .setParameter("usuario", usuario.getCodigo())
                    .getSingleResult() !=  null ? true : false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        } finally {
            em.close();
        }
    }

    public List<UsuarioDTO> pesquisarUsuariosPorSetores(String setoresUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder sql = new StringBuilder();
            if(!setoresUsuario.isEmpty()){  
              sql.append("SELECT DISTINCT us.usuarioDTO ");
              sql.append("FROM UsuarioSetorDTO as us ");
              sql.append("WHERE us.setorDTO.codigo ");
              sql.append("IN (");  
              sql.append(setoresUsuario);
              sql.append(" ) ");
            }
            return em.createQuery(sql.toString()) 
                    .getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }    
    }
    
    public List<UsuarioDTO> pesquisarResponsaveisDoSetorQueQueremReceberEmail(SetorDTO setorDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT usuarioSetor.usuarioDTO FROM UsuarioSetorDTO usuarioSetor WHERE usuarioSetor.setorDTO.codigo = :setor ");
            builder.append("AND usuarioSetor.responsavel = 1 AND usuarioSetor.usuarioDTO.estado = 1 ");
            builder.append("AND 4 IN (SELECT c.codigo FROM usuarioSetor.usuarioDTO.configuracoes as c)");
            return em.createQuery(builder.toString()).
                    setParameter("setor", setorDTO.getCodigo()).getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
    /**
     * faz o balanceamento pelos usuarios que são responsaveis no setor e tem menos chamados
     * com status de novo, atribuido e em andamento
     * @param setorDTO
     * @return 
     */
    public UsuarioDTO balanceamentoDeUsuariosAtribuidos(SetorDTO setorDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT DISTINCT ch.usuarioAtribuidoDTO FROM ChamadoDTO ch WHERE ch.usuarioAtribuidoDTO.codigo IN ( ");
                builder.append("SELECT usuarioSetor.usuarioDTO.codigo FROM UsuarioSetorDTO usuarioSetor ");
                builder.append("WHERE usuarioSetor.setorDTO.codigo = :setor ");
                builder.append("AND usuarioSetor.responsavel = 1 AND usuarioSetor.usuarioDTO.estado = 1 ) ");
            builder.append("AND ch.statusDTO.codigo IN (1,2,3) ");
            builder.append("GROUP BY ch.usuarioAtribuidoDTO ORDER BY COUNT(ch.usuarioAtribuidoDTO) ");
            return (UsuarioDTO) em.createQuery(builder.toString()).
                    setParameter("setor", setorDTO.getCodigo()).setMaxResults(1).getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}