package com.bean;

import com.bo.LocalizacaoBO;
import com.core.GenericBean;
import com.core.IValidaContexto;
import com.dto.LocalizacaoDTO;
import com.util.FacesUtil;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "localizacaoBean")
@ViewScoped
public class LocalizacaoBean extends GenericBean<LocalizacaoDTO> implements Serializable, IValidaContexto{
            
    private static final long serialVersionUID = 100015L;
    private LocalizacaoDTO localizacaoOld;        

    public LocalizacaoBean() {
        tituloLayoutUnit = "Localização";
        objeto = new LocalizacaoDTO();
        objeto.setCampusDTO(ContextoBean.getContexto().getCampusAtual());
        getCodigo();
        validaContexto();
        localizacaoOld = new LocalizacaoDTO(objeto.getDescricao(), objeto.getCampusDTO());
    }
       
    @Override
    public String incluirAlterar() {
        return "localizacaoIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "localizacaoPesquisar";
    }

    @Override
    public String novo() throws Throwable {
        this.setAlterando(false);
        dadosPesquisa();
        return pesquisar();
    }

    @Override
    public boolean clausulaUnico() {
        return !objeto.getDescricao().equals(localizacaoOld.getDescricao()) || 
                !objeto.getCampusDTO().equals(localizacaoOld.getCampusDTO());
    }    

    @Override
    public String update() {
        getLocalizacaoOld().setDescricao(getObjeto().getDescricao());
        getLocalizacaoOld().setCampusDTO(getObjeto().getCampusDTO());        
        return super.update();
    }
   
    @Override
    public void validaCampo(List<String> erros, LocalizacaoDTO localizacao) {
        ValidadorCampo.validarCampoVazio(localizacao.getDescricao(), "DESCRIÇÃO", erros);        
    }
    
    public LocalizacaoDTO getLocalizacaoOld() {
        return localizacaoOld;
    }
    
    @Override
    public List<LocalizacaoDTO> dadosPesquisa(){
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarNomeECampus(getCampoPesquisar(), ContextoBean.getContexto().getCampusAtual());
    }

    @Override
    public LocalizacaoBO getBO() {
        return LocalizacaoBO.getInstance();
    }              

    @Override
    public void validaContexto() {
        if (alterando) {
            if (!objeto.getCampusDTO().equals(ContextoBean.getContexto().campusAtual)) {
                FacesUtil.adicionarMensagemErro("Mude seu contexto para acessar o campus desejado!");
                FacesUtil.redirect("erroPaginaNaoEncontrada");
            }
        }
    }
}