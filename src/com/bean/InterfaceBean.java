package com.bean;

import com.bo.InterfaceBO;
import com.dto.InterfaceDTO;
import com.util.FacesUtil;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.NavigationHandler;
import javax.inject.Named;


@RequestScoped
@Named(value = "interfaceBean")
public class InterfaceBean implements Serializable{

    private InterfaceBO interfaceBO = InterfaceBO.getInstance();

    public String execute(String url,String actives) {
        FacesUtil.setSessionAttribute("activeIndex", actives);
        return url;
    }

    
    public List<InterfaceDTO> getPais(){
        try {
            return interfaceBO.pesquisarPorUsuarioInterfaceSomentePai(ContextoBean.getContexto().getUsuarioLogado());
        } catch (NullPointerException e) {
            NavigationHandler handler = FacesUtil.getFacesContext().getApplication().getNavigationHandler();
            handler.handleNavigation(FacesUtil.getFacesContext(), null, "loginPage");
        }
        return null;
    }

    public List<InterfaceDTO> getFilhos(InterfaceDTO pai){
	return interfaceBO.pesquisarPorUsuarioInterfacePorPai(ContextoBean.getContexto().getUsuarioLogado(),pai);
    }
    
    public String getCDU(){
        return FacesUtil.getCDU();        
    }
    
    public boolean getInterfaceChamado(){
	return interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), "chamado", 1);
    }
    
    public boolean getVerificarIncluir(){        
        return interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), getCDU(), 2l);
    }
    
    public boolean getVerificarEditar(){        
        return interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), getCDU(), 3l);
    }
    
    public boolean getVerificarAgendar(){        
        return interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), getCDU(), 6l);
    }
    
    public boolean getVerificaConfigurar(){        
        return interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), getCDU(), 7l);
    }
    
    public boolean getVerificarExcluir(){     
        return interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), getCDU(), 4l);
    }    

    public boolean getVerificarSetorTemPermissaoCriarSetores() {
        return interfaceBO.verificaVisualizacaoBotao(ContextoBean.getContexto().getUsuarioLogado(), "setor", 7l);
    }
    
    public String getActiveIndex() {
        return (String) FacesUtil.getSessionAttribute("activeIndex");
    }

    
}