package com.dao;

import com.core.GenericDAO;
import com.dto.ReservaDTO;
import com.dto.UsuarioDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ReservaDAO extends GenericDAO<ReservaDTO> {
    
    private static final long serialVersionUID = 1016L;
    private volatile static ReservaDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(ReservaDAO.class.getName());
    
    public static ReservaDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (ReservaDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new ReservaDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public ReservaDAO() {
        super(ReservaDTO.class);
    }      
    
        
    public ReservaDTO verificaUsuario (UsuarioDTO usuarioDTO, ReservaDTO reservaDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            return (ReservaDTO) em.createQuery("FROM ReservaDTO AS r WHERE r.codigo = :reserva AND r.usuarioAutorDTO.codigo = :usuario")
                    .setParameter("reserva", reservaDTO.getCodigo())
                    .setParameter("usuario", usuarioDTO.getCodigo())
                    .getSingleResult();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        } finally {
            em.close();
        }
    }
}