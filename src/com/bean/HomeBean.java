package com.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named("homeBean")
public class HomeBean {   
    private boolean mostraRss;
    private boolean mostraLinkCarregar = true;    
                                
    public HomeBean() {
    }
    
    public void mostrar(){
        setMostraRss(true);
        setMostraLinkCarregar(false);                  
    }
    
    public boolean isMostraRss() {
        return mostraRss;
    }

    public void setMostraRss(boolean mostraRss) {
        this.mostraRss = mostraRss;
    }

    public boolean isMostraLinkCarregar() {
        return mostraLinkCarregar;
    }

    public void setMostraLinkCarregar(boolean mostraLinkCarregar) {
        this.mostraLinkCarregar = mostraLinkCarregar;
    }
        
    public String getNomeUsuario() {
        return ContextoBean.getContexto().getUsuarioLogado().getNome();
    }   

    public long getId() {
        return ContextoBean.getContexto().getUsuarioLogado().getCodigo();        
    }
}