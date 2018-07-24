package com.bo;

import com.core.GenericBO;
import com.dao.InterfaceDAO;
import com.dto.InterfaceDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceBO extends GenericBO<InterfaceDTO>{
    
    private static final long serialVersionUID = 111L;
    static final Logger logger = Logger.getLogger(InterfaceBO.class.getName());
    
    public InterfaceBO() {
        super(InterfaceDTO.class);
    }
    
    InterfaceDAO dao = InterfaceDAO.getInstance();

    private volatile static InterfaceBO uniqueInstance;

    public static InterfaceBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (InterfaceBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new InterfaceBO();
                }
            }
        }
        return uniqueInstance;
    }
    
   public List<InterfaceDTO> pesquisarPorUsuarioInterfacePorPai(UsuarioDTO usuarioDTO,InterfaceDTO interfacePai) {
        try{
            return dao.pesquisarPorUsuarioInterfacePorPai(usuarioDTO,interfacePai);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }    
    }
    
    public List<InterfaceDTO> pesquisarPorUsuarioInterfaceSomentePai(UsuarioDTO usuarioDTO) {
        try {
            return dao.pesquisarPorUsuarioInterfaceSomentePai(usuarioDTO);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
   
    public List<InterfaceDTO> pesquisarPorUsuarioInterface(UsuarioDTO usuarioDTO) {
        try{
            return dao.pesquisarPorUsuarioInterface(usuarioDTO);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }    
    }
    
    public InterfaceDTO pesquisarInterfacePorCdu(String cdu){
        try{
            return dao.pesquisarInterfacePorCdu(cdu);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }    
    }
    
    public boolean verificaVisualizacaoBotao(UsuarioDTO usuario, String cdu, long op) {
        try{
            return dao.verificaVisualizacaoBotao(usuario, cdu, op);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }    
    }

    public boolean pesquisarPorUsuarioInterfaceTemAcesso(UsuarioDTO usuarioDTO, Long codigoInterface) {
        try{    
            return dao.pesquisarPorUsuarioInterfaceTemAcesso(usuarioDTO, codigoInterface);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }                
    }

    public List<InterfaceDTO> pesquisarPorInterfaceRecursiva(InterfaceDTO interfaceDTO) {
        try{
            return dao.pesquisarPorInterfaceRecursiva(interfaceDTO);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return null;
        }                
    }

    public boolean pesquisarPorUsuarioInterfaceTemAcesso(UsuarioDTO usuario, String cdu) {
        try{
            return dao.pesquisarPorUsuarioInterfaceTemAcesso(usuario, cdu);        
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }                
    }
    public List<InterfaceDTO> interfaceOrganizadosPorPais() {
        try {
            return dao.interfaceOrganizadosPorPais();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
    public List<InterfaceDTO> interfacesPais() {
        try {
            return dao.interfacesPais();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }
}