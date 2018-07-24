package com.bean;

import com.bo.FabricanteBO;
import com.core.GenericBean;
import com.dto.FabricanteDTO;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "fabricanteBean")
@ViewScoped
public class FabricanteBean extends GenericBean<FabricanteDTO> implements Serializable{
        
    private static final long serialVersionUID = 100013L;
    private String descricaoOld;        
    
    public FabricanteBean() {        
        tituloLayoutUnit = "Fabricante"; 
        objeto = new FabricanteDTO();
        getCodigo();
        descricaoOld = objeto.getDescricao();
    }

    @Override
    public String incluirAlterar() {
        return "fabricanteIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "fabricantePesquisar";
    }

    @Override
    public String novo() throws Throwable {
        setAlterando(false);
        dadosPesquisa();
        return pesquisar();
    }

    @Override
    public boolean clausulaUnico() {
        return !objeto.getDescricao().equals(getDescricaoOld());
    }
           

    @Override
    public void validaCampo(List<String> erros, FabricanteDTO fabricante) {
        ValidadorCampo.validarCampoVazio(fabricante.getDescricao(), "DESCRICAO", erros);
    }

    @Override
    public List<FabricanteDTO> dadosPesquisa(){
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarNome(getCampoPesquisar());
    }

    @Override
    public FabricanteBO getBO() {
        return FabricanteBO.getInstance();
    }
    
    public String getDescricaoOld() {
        return descricaoOld;
    }

    public void setDescricaoOld(String descricaoOld) {
        this.descricaoOld = descricaoOld;
    }
}