package com.bean;

import com.bean.filter.chamadoInfra.ChamadoAtribuido;
import com.bean.filter.chamadoInfra.ChamadoInfraPorSetor;
import com.bean.filter.chamadoInfra.ChamadoPorTitutlo;
import com.bean.filter.chamadoInfra.Filtrando;
import com.bean.filter.chamadoInfra.MeusChamados;
import com.bean.filter.chamadoInfra.StatusGrafico;
import com.bo.ChamadoInfraBO;
import com.bo.PatrimonioBO;
import com.bo.PrioridadeBO;
import com.dto.ChamadoInfraDTO;
import com.dto.ChamadoInfraModel;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.StatusDTO;
import static com.util.FacesUtil.*;
import com.util.SelectBoxUtil;
import com.util.ValidadorCampo;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.extensions.util.RequestParameterBuilder;

@Named(value = "chamadoInfraBean")
@ViewScoped
public class ChamadoInfraBean extends ChamadoBean<ChamadoInfraDTO> {
    
    private static final long serialVersionUID = 100001L;
    
    private List<PatrimonioDTO> patrimonioDestino = new ArrayList<>();
    
    private PatrimonioBO patrimonioBO = PatrimonioBO.getInstance();
    private int opcaoVarios;
    private boolean variosPatrimonios;
    private boolean buscaAvancada;
    private ChamadoInfraDTO chamadoInfraFiltro = new ChamadoInfraDTO();
    private String opBusca = "";
    private Long codigoPatrimonio = 0l;    
    private List<ChamadoInfraDTO> ChamadosInfraSelecionados= new ArrayList<>();
    private Collection<Integer> list = new ArrayList<>(); 

    RequestParameterBuilder rpBuilder = new RequestParameterBuilder(getContextPath()+"/pages/chamado/andamentoMultiplosChamados.jsf");  
    
    public ChamadoInfraBean() {
        tituloLayoutUnit = "Chamado Infra-Estrutura";
        objeto = new ChamadoInfraDTO();
        localizacaoDTO = new LocalizacaoDTO();
        chamadoInfraFiltro = new ChamadoInfraDTO();
        variosPatrimonios = false;
        opBusca = "";        
        buscaAvancada = false;
    }

    @Override
    public String incluirAlterar() {
        objeto = new ChamadoInfraDTO();
        localizacaoDTO = new LocalizacaoDTO();
        super.setAlterando(false);
        return "chamadoInfraIncluirAlterar";
    }
    
    public void carregarBuscaAvancada(){
        buscaAvancada = true;
    }

    public ChamadoInfraDTO criarChamadoInfra(ChamadoInfraDTO chamado) {                
        chamado.setDataHoraAbertura(new Date());
        chamado.setStatusDTO(new StatusDTO(1l));
        chamado.setUsuarioAtribuidoDTO(usuarioBO.balanceamentoDeUsuariosAtribuidos(objeto.getPatrimonioDTO().getSetorDTO()));
        chamado.setProgresso(0);
        chamado.setPrioridadeDTO(PrioridadeBO.getInstance().findById(2));
        chamado.setUsuarioAutorDTO(ContextoBean.getContexto().getUsuarioLogado());
        return chamado;
    }

    private String criarVariosChamadoInfra() {
        ChamadoInfraDTO chamadosInfraAux = new ChamadoInfraDTO();
        chamadosInfraAux.setDescricao(this.objeto.getDescricao());
        chamadosInfraAux.setTitulo(this.objeto.getTitulo());                
        chamadosInfraAux.setDataPrevista(this.objeto.getDataPrevista());
        chamadosInfraAux.setDataHoraAbertura(new Date());
        StringBuilder builder = new StringBuilder();
        for (PatrimonioDTO patrimonioDTOV : patrimonioDestino) {            
            this.objeto.setStatusDTO(statusBO.findById(1l));
            this.objeto.setDataHoraAbertura(chamadosInfraAux.getDataHoraAbertura());
            this.objeto.setProgresso(0);
            this.objeto.setPrioridadeDTO(PrioridadeBO.getInstance().findById(2));
            this.objeto.setDescricao(chamadosInfraAux.getDescricao());            
            this.objeto.setTitulo(chamadosInfraAux.getTitulo());
            this.objeto.setDataPrevista(chamadosInfraAux.getDataPrevista());
            this.objeto.setUsuarioAutorDTO(ContextoBean.getContexto().getUsuarioLogado());            
            this.objeto.setPatrimonioDTO(patrimonioDTOV);
            this.objeto.setUsuarioAtribuidoDTO(usuarioBO.balanceamentoDeUsuariosAtribuidos(patrimonioDTOV.getSetorDTO()));
            chamadoInfraBO.save(this.objeto);
            builder.append(criarTextoEnvioDeEmail(this.objeto)).append(" | ");
            this.objeto.setArquivos(getArquivos());
            chamado.setCodigo(objeto.getCodigo());
            chamadoInfraBO.update(this.objeto);
            criarAndamento();
            //getChamadosInfra().setWrappedData(this.objeto);
            this.objeto = new ChamadoInfraDTO();
        }
        this.chamado = chamadosInfraAux;
        return builder.toString();
    }
    
    public void editarStatus(int id){
        for(ChamadoInfraDTO dTO : getChamadosInfraSelecionados()){
            dTO.setStatusDTO(statusBO.findById(id));
            chamadoInfraBO.update(dTO);
        }
    }
    
    public void editarPrioridade(int id){
        for(ChamadoInfraDTO dTO : getChamadosInfraSelecionados()){
            dTO.setPrioridadeDTO(PrioridadeBO.getInstance().findById(id));
            chamadoInfraBO.update(dTO);
        }
    }
    
    public void editarProgresso(int progresso){
        for(ChamadoInfraDTO dTO : getChamadosInfraSelecionados()){
            dTO.setProgresso(progresso);
            chamadoInfraBO.update(dTO);
        }
    }
    
    public String getMultiploAndamento(){
        return rpBuilder.build();
    }
    
    public void editarSelecionados(){
        Collection<Integer> list = new ArrayList<>(); 
        try{
            getChamadosInfraSelecionados().forEach((dTO) -> {
                list.add(dTO.getCodigo().intValue());
            });
            rpBuilder.paramJson("list", list, getTypeGenericList());  
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
    
    public String getTypeGenericList() {  
        return "java.util.Collection<java.lang.Integer>";  
    }

    @Override
    public String save() throws Throwable {
        try {
            if (super.validacao(objeto)) {
                return null;
            }
            if (!variosPatrimonios) {
                chamadoInfraBO.save(criarChamadoInfra(objeto)); 
                this.objeto.setArquivos(getArquivos());
                chamado.setCodigo(objeto.getCodigo());
                chamadoInfraBO.update(this.objeto);
                chamado = objeto;
                super.enviarEmailResponsavel(usuarioBO.pesquisarResponsaveisDoSetorQueQueremReceberEmail(this.objeto.getPatrimonioDTO().getSetorDTO()),criarTextoEnvioDeEmail(chamado));
                super.criarAndamento();
            } else {
                String msg = criarVariosChamadoInfra();
                super.enviarEmailResponsavel(usuarioBO.pesquisarResponsaveisDoSetorQueQueremReceberEmail(patrimonioDestino.get(0).getSetorDTO()),msg);
                patrimonioDestino = null;
            }
            inseridoSucesso();
            return super.novo();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String update() {
        return andamento();
    }

    public void mostrarPatrimonios() {
        setVariosPatrimonios(!isVariosPatrimonios());
    }

    public void refreshPatrimonio() {
        getListaPatrimonio().clear();
        if (getLocalizacaoDTO() != null) {
            getListaPatrimonio();
        }
    }

    @Override
    public void validaCampo(List<String> erros, ChamadoInfraDTO chamado) {
        ValidadorCampo.validarCampoVazio(chamado.getTitulo(), "TÍTULO", erros);
        ValidadorCampo.validarCampoVazio(chamado.getDescricao(), "DESCRIÇÂO", erros);
        ValidadorCampo.validarCampoNulo(getLocalizacaoDTO(), "LOCALIZAÇÃO", erros);
        if (!variosPatrimonios) {
            ValidadorCampo.validarCampoNulo(chamado.getPatrimonioDTO(), "PATRIMÔNIO", erros);
        } else {
            if (patrimonioDestino.isEmpty()) {
                erros.add("Selecione ao menos um patrimônio");
            }
        }
    }

    @Override
    public ChamadoInfraBO getBO() {
        return ChamadoInfraBO.getInstance();
    }
    
    public ChamadoInfraModel getChamadosInfra(){
        ChamadoInfraModel retorno;
        Filtrando filtrando = new Filtrando(
                new MeusChamados(
                new ChamadoAtribuido(
                new ChamadoPorTitutlo(
                new StatusGrafico(
                new ChamadoInfraPorSetor(null)), getCampoPesquisar()))));
        retorno = filtrando.filtrando(getOpBusca());
        removerAtributosSessao();
        return retorno;
    }
    
    public List<PatrimonioDTO> getListaPatrimonio() {
        return localizacaoDTO != null ? patrimonioBO.pesquisarSemBaixaPorLocalizacao(localizacaoDTO) : new ArrayList<>();
    }

    public List<SelectItem> getPatrimoniosFiltro() throws Throwable {
        return getBuscaAvancada() == true ? new SelectBoxUtil().retornaListaPatrimonioSelectItem(patrimonioBO.pesquisarPatrimonioSemBaixaPorSetoresDoUsuarioOrdenadoPorCampusELocalizacao(ContextoBean.getContexto())) : new ArrayList<>();
    }
    
    public String getOpBusca() {
        return opBusca;
    }

    public void setOpBusca(String opBusca) {
        this.opBusca = opBusca;
    }

    public ChamadoInfraDTO getChamadoInfraFiltro() {
        return chamadoInfraFiltro;
    }

    public void setChamadoInfraFiltro(ChamadoInfraDTO chamadoInfraFiltro) {
        this.chamadoInfraFiltro = chamadoInfraFiltro;
    }

    public int getOpcaoVarios() {
        return opcaoVarios;
    }

    public void setOpcaoVarios(int opcaoVarios) {
        this.opcaoVarios = opcaoVarios;
    }

    public boolean isVariosPatrimonios() {
        return variosPatrimonios;
    }

    public void setVariosPatrimonios(boolean variosPatrimonios) {
        this.variosPatrimonios = variosPatrimonios;
    }

    public List<PatrimonioDTO> getPatrimonioDestino() {
        return patrimonioDestino;
    }

    public void setPatrimonioDestino(List<PatrimonioDTO> patrimonioDestino) {
        this.patrimonioDestino = patrimonioDestino;
    }

    public Long getCodigoPatrimonio() {
        return codigoPatrimonio;
    }

    public void setCodigoPatrimonio(Long codigoPatrimonio) {
        this.codigoPatrimonio = codigoPatrimonio;
    }

    public boolean getBuscaAvancada() {
        return buscaAvancada;
    }
    
    public void setBuscaAvancada(boolean buscaAvancada) {
        this.buscaAvancada = buscaAvancada;
    }

    public List<ChamadoInfraDTO> getChamadosInfraSelecionados() {
        return ChamadosInfraSelecionados;
    }

    public void setChamadosInfraSelecionados(List<ChamadoInfraDTO> ChamadosInfraSelecionados) {
        this.ChamadosInfraSelecionados = ChamadosInfraSelecionados;
    }

    public Collection<Integer> getList() {
        return list;
    }

    public void setList(Collection<Integer> list) {
        this.list = list;
    }
}