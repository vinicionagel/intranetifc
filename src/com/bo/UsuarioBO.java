package com.bo;

import com.core.GenericBO;
import com.dao.UsuarioDAO;
import com.dto.CampusDTO;
import com.dto.ChamadoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import java.util.List;

public class UsuarioBO extends GenericBO<UsuarioDTO>{

    private static final long serialVersionUID = 125L;
    private UsuarioDAO dao = UsuarioDAO.getInstance();
    
    private volatile static UsuarioBO uniqueInstance;

    public static UsuarioBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (UsuarioBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new UsuarioBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public UsuarioBO() {
        super(UsuarioDTO.class);
    }

    public UsuarioDTO pesquisarPorLoginESenha (UsuarioDTO usuarioDTO) {
        try{
            return dao.pesquisarPorLoginESenha(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public List<UsuarioDTO> pesquisarNome(UsuarioDTO usuarioDTO) {
        try {
            return dao.pesquisarNome(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<UsuarioDTO> pesquisarNomeECampus(UsuarioDTO usuarioDTO, CampusDTO campusDTO) {
        try {
            return dao.pesquisarNomeECampus(usuarioDTO, campusDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public UsuarioDTO pesquisarEmail(UsuarioDTO usuarioDTO) {
        try {
            return dao.pesquisarEmail(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public UsuarioDTO pesquisarPorLogin(UsuarioDTO usuarioDTO) {
        try {
            return dao.pesquisarPorLogin(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public UsuarioDTO pesquisarVerificacao(UsuarioDTO usuarioDTO) {
        try {
            return dao.pesquisarVerificacao(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<UsuarioDTO> consultarUsuariosMesmoCampus(UsuarioDTO usuarioDTO)  {
        try {
            return dao.consultarUsuariosMesmoCampus(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<UsuarioDTO> consultarUsuarioMesmoCampusESemCampus(UsuarioDTO usuarioDTO)  {
        try {
            return dao.consultarUsuarioMesmoCampusESemCampus(usuarioDTO);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean loginUnico(UsuarioDTO usuarioDTO) {
        try {
            return dao.loginUnico(usuarioDTO);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean emailUnico(UsuarioDTO usuarioDTO) {
        try {
            return dao.emailUnico(usuarioDTO);
        } catch (Exception e) {
            return false;
        }
    }

    public List<UsuarioDTO> consultarUsuariosCampusUsuarioAutorChamado(ChamadoDTO chamadoDTO) {
        try {
            return dao.consultarUsuariosCampusUsuarioAutorChamado(chamadoDTO);
        } catch (Exception e) {
            return null;
        }
    }
    
    @Override
    public List<UsuarioDTO> findAll(){
        try {
            return dao.findAll();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<UsuarioDTO> pesquisarTodosAtivos(){
        try {
            return dao.pesquisarTodosAtivos();
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<UsuarioDTO> pesquisarAdmin(CampusDTO campus)  {
        try{
            return dao.pesquisarAdmin(campus);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validaAdmin(CampusDTO campus, UsuarioDTO usuario) {
        try{
            return dao.validaAdmin(campus, usuario);
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<UsuarioDTO> pesquisarUsuariosPorSetores(String setoresUsuario) {
        try{
            return dao.pesquisarUsuariosPorSetores(setoresUsuario);
        } catch (Exception e) {
            return null;
        }
    }
    public List<UsuarioDTO> pesquisarResponsaveisDoSetorQueQueremReceberEmail(SetorDTO setorDTO) {
        try{
            return dao.pesquisarResponsaveisDoSetorQueQueremReceberEmail(setorDTO);
        } catch (Exception e){
            return null;
        }
    }
    public UsuarioDTO balanceamentoDeUsuariosAtribuidos(SetorDTO setorDTO) {
        try{
            return dao.balanceamentoDeUsuariosAtribuidos(setorDTO);
        } catch (Exception e){
            return null;
        }
    }
}