package com.bo;

import com.core.GenericBO;
import com.dao.UsuarioPermissaoSetorDAO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioPermissaoSetorDTO;
import java.util.List;

public class UsuarioPermissaoSetorBO extends GenericBO<UsuarioPermissaoSetorDTO>{
    
    private static final long serialVersionUID = 126L;
    private UsuarioPermissaoSetorDAO dao = UsuarioPermissaoSetorDAO.getInstance();
    
    public UsuarioPermissaoSetorBO() {
        super(UsuarioPermissaoSetorDTO.class);
    }
    
    private volatile static UsuarioPermissaoSetorBO uniqueInstance;

    public static UsuarioPermissaoSetorBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (UsuarioPermissaoSetorBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new UsuarioPermissaoSetorBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public List<UsuarioPermissaoSetorDTO> pesquisarPorUsuario(UsuarioDTO usuarioDTO){
        try{
            return dao.pesquisarPorUsuario(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<SetorDTO> pesquisarSetorPermissaoRelatorio(UsuarioDTO usuarioDTO){
        try{
            return dao.pesquisarSetorPermissaoRelatorio(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
}
