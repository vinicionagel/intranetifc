package com.bean;

import com.bo.FuncaoBO;
import com.core.GenericBean;
import com.dto.FuncaoDTO;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "funcaoBean")
@ViewScoped
public class FuncaoBean extends GenericBean<FuncaoDTO> implements Serializable{

    private static final long serialVersionUID = 100014L;
    private String descricaoOld;
        
    public FuncaoBean() {        
        tituloLayoutUnit = "Função";
        objeto = new FuncaoDTO();
        getCodigo();
        descricaoOld = objeto.getDescricao();   
    }

    @Override
    public String incluirAlterar() {
        return "funcaoIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "funcaoPesquisar";
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
    public String update() {                
        setDescricaoOld(getObjeto().getDescricao());
        return super.update();
    }

    @Override
    public String include() {        
        objeto = new FuncaoDTO();
        return super.include();
    }

    @Override
    public void validaCampo(List<String> erros, FuncaoDTO funcao) {
        ValidadorCampo.validarCampoVazio(funcao.getDescricao(), "DESCRIÇÃO", erros);
    }
    
    @Override
    public List<FuncaoDTO> dadosPesquisa(){
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarNome(getCampoPesquisar());
    }

    @Override
    public FuncaoBO getBO() {
        return FuncaoBO.getInstance();
    }
           
    public String getDescricaoOld() {
        return descricaoOld;
    }

    public void setDescricaoOld(String descricaoOld) {
        this.descricaoOld = descricaoOld;
    }
}