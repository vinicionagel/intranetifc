package com.bean;

import com.bo.TipoPermissaoBO;
import com.core.GenericBean;
import com.dto.TipoPermissaoDTO;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

@Named(value = "tipoPermissaoBean")
@SessionScoped
public class TipoPermissaoBean extends GenericBean<TipoPermissaoDTO> implements Serializable{
    
    private static final long serialVersionUID = 100020L;
    private String descricaoOld;
        
    public TipoPermissaoBean() {
        tituloLayoutUnit = "Tipo Permissão";
    }

    @Override
    public String novo() throws Throwable {
        setAlterando(false);
        dadosPesquisa();
        return pesquisar();
    }    

    @Override
    public void validaCampo(List<String> erros, TipoPermissaoDTO tipoPermissao) {
        ValidadorCampo.validarCampoVazio(tipoPermissao.getDescricao(), "DESCRICAO", erros);
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
        this.setObjeto(new TipoPermissaoDTO());
        return super.include();
    }

    @Override
    public String incluirAlterar() {
        return "tipoPermissaoIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "tipoPermissaoPesquisar";
    }

    @Override
    public List<TipoPermissaoDTO> dadosPesquisa(){
        objeto = new TipoPermissaoDTO();
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarNome(objeto);
    }

    @Override
    public TipoPermissaoBO getBO() {
        return TipoPermissaoBO.getInstance();
    }
            
    public String getDescricaoOld() {
        return descricaoOld;
    }

    public void setDescricaoOld(String descricaoOld) {
        this.descricaoOld = descricaoOld;
    }   
}