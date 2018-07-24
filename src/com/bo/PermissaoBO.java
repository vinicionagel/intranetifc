package com.bo;

import com.core.GenericBO;
import com.dao.ChamadoInfraDAO;
import com.dao.ChamadoServicoDAO;
import com.dao.PermissaoDAO;
import com.dto.InterfaceDTO;
import com.dto.PermissaoDTO;
import com.dto.UsuarioDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermissaoBO extends GenericBO<PermissaoDTO>{

    private static final long serialVersionUID = 114L;
    private PermissaoDAO dao = PermissaoDAO.getInstance();

    public PermissaoBO() {
        super(PermissaoDTO.class);
    }

    private volatile static PermissaoBO uniqueInstance;

    public static PermissaoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (PermissaoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new PermissaoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public PermissaoDTO pesquisarPermissaoPorInterface(InterfaceDTO interfaceDTO) {
        try{
            return dao.pesquisarPermissaoPorInterface(interfaceDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<PermissaoDTO> pesquisarPermisoesPorInterface(InterfaceDTO interfaceDTO) {
        try {
            return dao.pesquisarPermisoesPorInterface(interfaceDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<PermissaoDTO> permissoesUsuario(UsuarioDTO usuarioDTO)  {
        try {
            return dao.permissoesUsuario(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean verificarPermissaoParaVerChamado(UsuarioDTO usuario, long idChamado){
        try {
            return ChamadoInfraDAO.getInstance().pesquisarSeChamadoPertenceUsuario(usuario, idChamado) == null 
                    && ChamadoServicoDAO.getInstance().pesquisarSeChamadoPertenceUsuario(usuario, idChamado) == null;
        } catch (Exception e) {
            Logger.getLogger(PermissaoBO.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }
}