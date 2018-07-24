package com.bo;

import com.core.GenericBO;
import com.dao.UsuarioSetorDAO;
import com.dto.FuncaoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.dto.UsuarioSetorDTO;
import java.util.List;

public class UsuarioSetorBO extends GenericBO<UsuarioSetorDTO> {

    private static final long serialVersionUID = 127L;
    private UsuarioSetorDAO dao = UsuarioSetorDAO.getInstance();
    private volatile static UsuarioSetorBO uniqueInstance;

    public UsuarioSetorBO() {
        super(UsuarioSetorDTO.class);
    }

    public static UsuarioSetorBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (UsuarioSetorBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new UsuarioSetorBO();
                }
            }
        }
        return uniqueInstance;
    }

    public List<UsuarioSetorDTO> pesquisarUsuariosSetor(SetorDTO setorDTO)  {
        try {
            return dao.pesquisarUsuariosSetor(setorDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<UsuarioSetorDTO> pesquisarSetoresUsuario(UsuarioDTO usuarioDTO)  {
        try {
            return dao.pesquisarSetoresUsuario(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean unique(UsuarioDTO usuarioDTO, SetorDTO setorDTO, FuncaoDTO funcaoDTO)  {
        try {
            return dao.unique(usuarioDTO, setorDTO, funcaoDTO);
        } catch (Exception e) {
            return false;
        }
    }

    public UsuarioSetorDTO pesquisarUsuarioSetorUsuarioSemSetor(UsuarioDTO usuarioDTO) {
        try {
            return dao.pesquisarUsuarioSetorUsuarioSemSetor(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public int pesquisarUsuarioSetorPorSetorEUsuario(UsuarioDTO usuarioDTO, SetorDTO setorDTO) {
        try {
            return dao.pesquisarUsuarioSetorPorSetorEUsuario(usuarioDTO,setorDTO);
        } catch (Exception e) {
            return 0;
        }
    }
}