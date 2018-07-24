package com.bo;

import com.core.GenericBO;
import com.dao.ConfiguracaoDAO;
import com.dto.ConfiguracaoDTO;
import com.dto.UsuarioDTO;
import java.util.List;

public class ConfiguracaoBO extends GenericBO<ConfiguracaoDTO>{
        
    private static final long serialVersionUID = 150L;
    private ConfiguracaoDAO dao = ConfiguracaoDAO.getInstance();

    private volatile static ConfiguracaoBO uniqueInstance;

    public static ConfiguracaoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (ConfiguracaoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new ConfiguracaoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public ConfiguracaoBO(){
        super(ConfiguracaoBO.class);
    }
    
    public List<ConfiguracaoDTO> consultarConfiguracoesPorTipo(String tipo){
        return dao.consultarConfiguracoesPorTipo(tipo);
    }
    
    public List<ConfiguracaoDTO> consultarConfiguracoesUsuario(UsuarioDTO usu){
        return dao.consultarConfiguracoesUsuario(usu);
    }    
    
    public boolean verificarUsuarioObservadorRecebeEmail(UsuarioDTO usu){        
        return dao.verificarUsuarioObservadorRecebeEmail(usu);
    }
    
    public boolean verificarUsuarioAtribuidoRecebeEmail(UsuarioDTO usu){
        return dao.verificarUsuarioAtribuidoRecebeEmail(usu);
    }
    
    public boolean verificarUsuarioAutorRecebeEmail(UsuarioDTO usu){
        return dao.verificarUsuarioAutorRecebeEmail(usu);
    }
}