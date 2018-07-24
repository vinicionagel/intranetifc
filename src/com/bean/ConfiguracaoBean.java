package com.bean;

import com.bo.ConfiguracaoBO;
import com.bo.UsuarioBO;
import com.dto.ConfiguracaoDTO;
import com.dto.UsuarioDTO;
import static com.util.FacesUtil.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "configuracaoBean")
@ViewScoped
public class ConfiguracaoBean implements Serializable{

    private UsuarioBO usuarioBO = UsuarioBO.getInstance();
    private List<ConfiguracaoDTO> configuracoesEmailSelecionadas = new ArrayList<>();        
                                    
    public void definirConfiguracoes(){
        try {
            UsuarioDTO usuario = getUsuario();
            usuario.setConfiguracoes(configuracoesEmailSelecionadas);                    
            usuarioBO.update(usuario);            
            adicionarMensagemAviso("Definições de recebimento de e-mails atualizadas");
        } catch (Exception e) {
            Logger.getLogger(ConfiguracaoBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }        
    }   
    
    public List<ConfiguracaoDTO> getConfiguracoesEmail(){
        return getBO().consultarConfiguracoesPorTipo("Email");
    }    
    
    public ConfiguracaoBO getBO(){
        return ConfiguracaoBO.getInstance();
    }
    
    public List<ConfiguracaoDTO> getConfiguracoesEmailSelecionadas() {
        return getBO().consultarConfiguracoesUsuario(getUsuario());
    }    
    
    private UsuarioDTO getUsuario(){
        return ContextoBean.getContexto().getUsuarioLogado();
    }
    
    public void setConfiguracoesEmailSelecionadas(List<ConfiguracaoDTO> configuracoesEmailSelecionadas) {
        this.configuracoesEmailSelecionadas = configuracoesEmailSelecionadas;
    }
}