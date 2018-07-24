package com.dao;

import com.core.GenericDAO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioPermissaoSetorDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class UsuarioPermissaoSetorDAO extends GenericDAO<UsuarioPermissaoSetorDTO> {
   
    private static final long serialVersionUID = 1026L;
    private volatile static UsuarioPermissaoSetorDAO uniqueInstance;
    
    public static UsuarioPermissaoSetorDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (UsuarioPermissaoSetorDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new UsuarioPermissaoSetorDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public List<UsuarioPermissaoSetorDTO> pesquisarPorUsuario(UsuarioDTO usuarioDTO){
        EntityManager em = emf.createEntityManager();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("FROM UsuarioPermissaoSetorDTO AS ups WHERE ups.usuarioDTO.codigo = :usuario ORDER BY ups.setorDTO.campusDTO.descricao");
            return  em.createQuery(sql.toString()).setParameter("usuario", usuarioDTO.getCodigo()).getResultList();
        }catch(Exception e){
            Logger.getLogger(UsuarioPermissaoSetorDAO.class.getName()).log(Level.SEVERE, e.getMessage(), e);    
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<SetorDTO> pesquisarSetorPermissaoRelatorio(UsuarioDTO usuarioDTO){
        EntityManager em = emf.createEntityManager();
        try{
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ups.setorDTO FROM UsuarioPermissaoSetorDTO AS ups WHERE ups.usuarioDTO.codigo = :usuario ORDER BY ups.setorDTO.campusDTO.descricao");
            return  em.createQuery(sql.toString()).setParameter("usuario", usuarioDTO.getCodigo()).getResultList();
        }catch(Exception e){
            Logger.getLogger(UsuarioPermissaoSetorDAO.class.getName()).log(Level.SEVERE, e.getMessage(), e);    
            return null;
        } finally {
            em.close();
        }
    }
    
    public UsuarioPermissaoSetorDAO() {
        super(UsuarioPermissaoSetorDTO.class);
    }   
}