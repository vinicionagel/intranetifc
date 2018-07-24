package com.bean;

import com.bo.ServicoBO;
import com.bo.TipoServicoBO;
import com.core.GenericBean;
import com.core.IValidaContexto;
import com.dto.ServicoDTO;
import com.dto.TipoServicoDTO;
import com.util.FacesUtil;
import com.util.ValidadorCampo;
import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "servicoBean")
@ViewScoped
public class ServicoBean extends GenericBean<ServicoDTO> implements Serializable, IValidaContexto{
              
    private static final long serialVersionUID = 100017L;
    private ServicoDTO servicoOLD = new ServicoDTO();        
    
    public ServicoBean() {        
        tituloLayoutUnit = "Serviço";
        objeto = new ServicoDTO();
        getCodigo();
        servicoOLD = objeto;
        validaContexto();
    }

    @Override
    public String novo() throws Throwable {  
        dadosPesquisa();        
        return pesquisar();
    }

    @Override
    public List<ServicoDTO> dadosPesquisa(){
        setCampoPesquisar(campoPesquisar == null ? campoPesquisar = "" : getCampoPesquisar()); 
        return lista = getBO().pesquisarNomeECampus(getCampoPesquisar(), ContextoBean.getContexto().getCampusAtual());
    }

    @Override
    public void validaCampo(List<String> erros, ServicoDTO servico) {
        ValidadorCampo.validarCampoVazio(servico.getDescricaoCompleta(), "DESCRICAO COMPLETA", erros);
        ValidadorCampo.validarCampoVazio(servico.getDescricaoCurta(), "DESCRICAO CURTA", erros);
        ValidadorCampo.validarCampoNulo(servico.getTipoServicoDTO(), "TIPO SERVICO", erros);
    }

    @Override
    public boolean clausulaUnico() {
        return !getObjeto().getDescricaoCurta().equals(getServicoOLD().getDescricaoCurta()) 
                || !getObjeto().getTipoServicoDTO().getDescricao().equals(getServicoOLD().getTipoServicoDTO().getDescricao());
    }       

    
    @Override
    public String incluirAlterar() {
        return "servicoIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "servicoPesquisar";
    }
    
    @Override
    public ServicoBO getBO() {
        return ServicoBO.getInstance();
    }
   
    public List<TipoServicoDTO> getTiposServicos() {
        return TipoServicoBO.getInstance().pesquisarPorCampus(ContextoBean.getContexto().getCampusAtual());
    }
        
    public ServicoDTO getServicoOLD() {
        return servicoOLD;
    }

    public void setServicoOLD(ServicoDTO servicoOLD) {
        this.servicoOLD = servicoOLD;
    }          

    @Override
    public void validaContexto() {
        if (alterando) {
            if (!objeto.getTipoServicoDTO().getSetorDTO().getCampusDTO().equals(ContextoBean.getContexto().campusAtual)) {
                FacesUtil.adicionarMensagemErro("Mude seu contexto para acessar o campus desejado!");
                FacesUtil.redirect("erroPaginaNaoEncontrada");
            }
        }
    }
}