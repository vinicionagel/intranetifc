package com.bean;

import com.bo.CampusBO;
import com.core.GenericBean;
import com.dto.CampusDTO;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "campusBean")
@ViewScoped
public class CampusBean extends GenericBean<CampusDTO> implements Serializable{
    
    private static final long serialVersionUID = 100011L;
    private String descricaoOld;
            
    public CampusBean() {
        tituloLayoutUnit = "Unidade";
        objeto = new CampusDTO();
        getCodigo();
        descricaoOld = objeto.getDescricao();
    }

    @Override
    public String incluirAlterar() {
        return "campusIncluirAlterar";
    }

    @Override
    public String pesquisar() {        
        return "campusPesquisar";
    }

    @Override
    public String novo() throws Throwable {
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());    
        dadosPesquisa();
        return pesquisar();
    }

    @Override
    public boolean clausulaUnico() {
        return !objeto.getDescricao().equals(getDescricaoOld());
    }

    @Override
    public void validaCampo(List<String> erros, CampusDTO campus) {
        ValidadorCampo.validarCampoVazio(campus.getDescricao(), "DESCRIÇÃO", erros);
    }

    @Override
    public CampusBO getBO() {
        return CampusBO.getInstance();
    }        
    
    @Override
    public List<CampusDTO> dadosPesquisa() {
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarNome(getCampoPesquisar());
    }    

    public String getDescricaoOld() {
        return descricaoOld;
    }

    public void setDescricaoOld(String descricaoOld) {
        this.descricaoOld = descricaoOld;
    }    
}