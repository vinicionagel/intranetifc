package com.bean;

import com.bo.SetorBO;
import com.bo.TipoServicoBO;
import com.core.GenericBean;
import com.core.IValidaContexto;
import com.dto.SetorDTO;
import com.dto.TipoServicoDTO;
import com.util.FacesUtil;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "tipoServicoBean")
@ViewScoped
public class TipoServicoBean extends GenericBean<TipoServicoDTO> implements Serializable, IValidaContexto{
    
    private static final long serialVersionUID = 100021L;
    private SetorBO setorBO = SetorBO.getInstance();    
    private TipoServicoDTO tipoServicoOld = new TipoServicoDTO();             

    public TipoServicoBean() {
        tituloLayoutUnit = "Tipo Serviço";
        objeto = new TipoServicoDTO();
        getCodigo();
        validaContexto();
        tipoServicoOld = objeto;
    }

    @Override
    public String novo() {
        return pesquisar();
    }
    
    @Override
    public List<TipoServicoDTO> dadosPesquisa(){
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar());
        return lista = getBO().pesquisarNomeECampus(getCampoPesquisar(), ContextoBean.getContexto().getCampusAtual());
    }

    @Override
    public String save() throws Throwable {
        return super.save();
    }
        
    @Override
    public void validaCampo(List<String> erros, TipoServicoDTO tipoServico) {
        ValidadorCampo.validarCampoVazio(tipoServico.getDescricao(), "DESCRICAO", erros);
        ValidadorCampo.validarCampoNulo(tipoServico.getSetorDTO(), "SETOR", erros);
    }

    @Override
    public boolean clausulaUnico() {
        return !objeto.getDescricao().equals(getTipoServicoOld().getDescricao()) 
                || !objeto.getSetorDTO().getDescricao().equals(getTipoServicoOld().getSetorDTO().getDescricao());
    }
    
    @Override
    public String incluirAlterar() {
        return "tipoServicoIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "tipoServicoPesquisar";
    }

    @Override
    public TipoServicoBO getBO() {
        return TipoServicoBO.getInstance();
    }
            
    public List<SetorDTO> getListaSetor() throws Throwable {
        return setorBO.findAll();
    }

    public TipoServicoDTO getTipoServicoOld() {
        return tipoServicoOld;
    }

    public void setTipoServicoOld(TipoServicoDTO tipoServicoOld) {
        this.tipoServicoOld = tipoServicoOld;
    }
           
    public List<SetorDTO> getSetoresUsuario() {
        return setorBO.pesquisarPorCampus(ContextoBean.getContexto().getCampusAtual());
    }

    @Override
    public void validaContexto() {
        if (alterando){
            if (!objeto.getSetorDTO().getCampusDTO().equals(ContextoBean.getContexto().campusAtual)){
                FacesUtil.adicionarMensagemErro("Mude seu contexto para acessar o campus desejado!");
                FacesUtil.redirect("erroPaginaNaoEncontrada");
            }
        }
    }
}