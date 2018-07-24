package com.bean;

import com.bo.TipoPatrimonioBO;
import com.core.GenericBean;
import com.dto.TipoPatrimonioDTO;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "tipoPatrimonioBean")
@ViewScoped
public class TipoPatrimonioBean extends GenericBean<TipoPatrimonioDTO> implements Serializable{
    
    private static final long serialVersionUID = 100019L;
    private String descricaoOld;        

    public TipoPatrimonioBean() {        
        tituloLayoutUnit = "Tipo Patrimônio";
        objeto = new TipoPatrimonioDTO();
        getCodigo();
        descricaoOld = objeto.getDescricao();
    }

    @Override
    public String incluirAlterar() {
        return "tipoPatrimonioIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "tipoPatrimonioPesquisar";
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
    public void validaCampo(List<String> erros, TipoPatrimonioDTO tipoPatrimonio) {
        ValidadorCampo.validarCampoVazio(tipoPatrimonio.getDescricao(), "DESCRICAO", erros);
    }

    @Override
    public List<TipoPatrimonioDTO> dadosPesquisa(){
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarNome(getCampoPesquisar());
    }

    @Override
    public TipoPatrimonioBO getBO() {
        return TipoPatrimonioBO.getInstance();
    }
            
    public String getDescricaoOld() {
        return descricaoOld;
    }

    public void setDescricaoOld(String descricaoOld) {
        this.descricaoOld = descricaoOld;
    }   
}