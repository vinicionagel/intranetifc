package com.bean;

import com.bo.ChamadoServicoBO;
import com.bo.PrioridadeBO;
import com.bo.ServicoBO;
import com.bo.SetorBO;
import com.bo.TipoServicoBO;
import com.dto.ChamadoServicoDTO;
import com.dto.ServicoDTO;
import com.dto.SetorDTO;
import com.dto.StatusDTO;
import com.dto.TipoServicoDTO;
import static com.util.FacesUtil.*;
import com.util.SelectBoxUtil;
import com.util.StringUtils;
import com.util.ValidadorCampo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named(value = "chamadoServicoBean")
@ViewScoped
public class ChamadoServicoBean extends ChamadoBean<ChamadoServicoDTO> {
                             
    private static final long serialVersionUID = 100000L;
    private TipoServicoDTO tipoServicoDTO = new TipoServicoDTO();
    private ChamadoServicoDTO chamadoServicoFiltro = new ChamadoServicoDTO();
    private boolean buscaAvancada;     
    private String opBusca = "";
    private String local = "";    
            
    public ChamadoServicoBean(){
        objeto = new ChamadoServicoDTO();        
        chamadoServicoFiltro = new ChamadoServicoDTO();    
        setCodigoSetor(0l);    
        tituloLayoutUnit = "Chamado Serviço";
    }
    
    @Override    
    public String incluirAlterar() {    
        objeto = new ChamadoServicoDTO();
        tipoServicoDTO = new TipoServicoDTO();        
        super.setAlterando(false);  
        setCodigoSetor(0l);    
        local = "";
        return "chamadoServicoIncluirAlterar";
    }
    
    public void refreshServico() {
        getListaServico().clear();
    }
    
    public void carregarBuscaAvancada(){
        buscaAvancada = true;
    }
        
    public void refreshTipoServico() throws Throwable{
        getListaTipoServico().clear();
        if (getCodigoSetor() != 0){
            getListaTipoServico();
        }
    }
            
    @Override
    public void validaCampo(List<String> erros, ChamadoServicoDTO chamado) {
        ValidadorCampo.validarCampoVazio(chamado.getTitulo(), "TÍTULO", erros);  
        ValidadorCampo.validarCampoLong(getCodigoSetor(), "SETOR", erros);
        ValidadorCampo.validarCampoNulo(getTipoServicoDTO(), "TIPO SERVIÇO", erros);
        ValidadorCampo.validarCampoNulo(chamado.getServicoDTO(), "SERVIÇO", erros);
        ValidadorCampo.validarCampoVazio(chamado.getDescricao(), "DESCRIÇÂO", erros);        
    }
    
    @Override
    public String save() throws Throwable {
        try {                      
            if (super.validacao(objeto)) {                
                return null;
            }            
            chamadoServicoBO.save(criarChamadoServico());   
            this.objeto.setArquivos(getArquivos());
            chamado.setCodigo(objeto.getCodigo());
            chamadoServicoBO.update(this.objeto);
            chamado = objeto;
            super.criarAndamento();
            super.enviarEmailResponsavel(usuarioBO.pesquisarResponsaveisDoSetorQueQueremReceberEmail(this.objeto.getServicoDTO().getTipoServicoDTO().getSetorDTO()), 
                    criarTextoEnvioDeEmail(chamado));
            inseridoSucesso();
            return super.novo();

        } catch (Exception e) {
            adicionarMensagemErro(e);            
            return null;
        }
    }
             
    public ChamadoServicoDTO criarChamadoServico() {        
        try {
            this.objeto.setStatusDTO(statusBO.findById(1l));
        } catch (Throwable ex) {
            Logger.getLogger(ChamadoServicoBean.class.getName()).log(Level.SEVERE, null, ex);
        }        
        this.objeto.setDataHoraAbertura(new Date());
        this.objeto.setProgresso(0);
        this.objeto.setPrioridadeDTO(PrioridadeBO.getInstance().findById(2));
        this.objeto.setUsuarioAutorDTO(ContextoBean.getContexto().getUsuarioLogado());
        this.objeto.setUsuarioAtribuidoDTO(usuarioBO.balanceamentoDeUsuariosAtribuidos(objeto.getServicoDTO().getTipoServicoDTO().getSetorDTO()));
        StringBuilder desc = new StringBuilder();
        desc.append("Descrição: ");
        desc.append(this.objeto.getDescricao());
        desc.append("<br /><br />Local: ");
        desc.append(getLocalizacaoDTO() != null ? getLocalizacaoDTO().getDescricao() : "Não informado");
        desc.append(" ");
        desc.append(StringUtils.isEmpty(getLocal()) ==  false ? getLocal() : "" );
        this.objeto.setDescricao(desc.toString());
        return this.objeto;
    }   
    
    public List<ChamadoServicoDTO> getChamadosServico() throws Throwable {
	List<ChamadoServicoDTO> retorno;
	                
        if(getOpBusca().equals("filtrando")) {
            retorno = chamadoServicoBO.pesquisarChamadosServicoPeloFiltro(chamadoServicoFiltro, setorBO.findById(getCodigoSetor()), ContextoBean.getContexto(), null, null);            
        } else if(getOpBusca().equals("meusChamados")) {
            retorno = chamadoServicoBO.pesquisarMeusChamadosServico(ContextoBean.getContexto());        
        } else if(getOpBusca().equals("chamadosAtribuidos")) {
            retorno = chamadoServicoBO.pesquisarChamadosServicoAtribuidos(ContextoBean.getContexto());        
        } else if(getOpBusca().equals("chamadosObservados")) {
            retorno = chamadoServicoBO.pesquisarChamadosServicoObservados(ContextoBean.getContexto());       
        } else if(getOpBusca().equals("porTitulo") && !StringUtils.checkNull(getCampoPesquisar()).equals("")) {
            retorno = chamadoServicoBO.pesquisarChamadosServicoPorTitulo(getCampoPesquisar(), ContextoBean.getContexto());
        } else if (getSessionAttribute("statusGrafico") != null) {
            ChamadoServicoDTO chamadoServico = new ChamadoServicoDTO();
            chamadoServico.setStatusDTO((StatusDTO) getSessionAttribute("statusGrafico"));                 
            retorno = chamadoServicoBO.pesquisarChamadosServicoPeloFiltro(chamadoServico, (SetorDTO) getSessionAttribute("setorGrafico"), ContextoBean.getContexto(), (Date) getSessionAttribute("dataInicial"), (Date) getSessionAttribute("dataFinal"));
        } else {
            retorno = chamadoServicoBO.pesquisarChamadosServicoPorSetorUsuario(ContextoBean.getContexto());
        }
        
        removerAtributosSessao();
        
	return retorno;
    }
    
    @Override
    public ChamadoServicoBO getBO() {
        return ChamadoServicoBO.getInstance();
    }

    public String getOpBusca() {
        return opBusca;
    }

    public void setOpBusca(String opBusca) {
        this.opBusca = opBusca;
    }
           
    public ChamadoServicoDTO getChamadoServicoFiltro() {
        return chamadoServicoFiltro;
    }

    public void setChamadoServicoFiltro(ChamadoServicoDTO chamadoServicoFiltro) {
        this.chamadoServicoFiltro = chamadoServicoFiltro;
    }

    public List<ServicoDTO> getServicosUsuario() throws Throwable {
        return ServicoBO.getInstance().pesquisarServicoPorSetoresDoUsuario(ContextoBean.getContexto().getUsuarioLogado());
    }

    public List<SelectItem> getTodosSetores() throws Throwable {
        return new SelectBoxUtil().retornaListaEmSelectItem(SetorBO.getInstance().consultarSetoresOrdenadosPorCampus(ContextoBean.getContexto()));
    }
    
    public ChamadoServicoDTO getChamadoDTO() {
        return objeto;
    }

    public void setChamadoDTO(ChamadoServicoDTO objeto) {
        this.objeto = objeto;
    }
    
    public TipoServicoDTO getTipoServicoDTO() {
        return tipoServicoDTO;
    }

    public void setTipoServicoDTO(TipoServicoDTO tipoServicoDTO) {
        this.tipoServicoDTO = tipoServicoDTO;
    }

    public List<TipoServicoDTO> getListaTipoServico() throws Throwable {
        return getCodigoSetor() != 0 ? TipoServicoBO.getInstance().pesquisarPorSetor(getCodigoSetor()) : new ArrayList<TipoServicoDTO>();        
    }

    public List<ServicoDTO> getListaServico() {
        return tipoServicoDTO != null ? ServicoBO.getInstance().pesquisarPorTipoServico(tipoServicoDTO) : new ArrayList<ServicoDTO>();         
    }
   
    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public boolean getBuscaAvancada() {
        return buscaAvancada;
    }

    public void setBuscaAvancada(boolean buscaAvancada) {
        this.buscaAvancada = buscaAvancada;
    }
}   