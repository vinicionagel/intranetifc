package com.bean;


import com.bo.AndamentoBO;
import com.bo.ChamadoBO;
import com.bo.ConfiguracaoBO;
import com.bo.HoraBO;
import com.bo.PrioridadeBO;
import com.bo.StatusBO;
import com.bo.UsuarioBO;
import com.core.GenericBean;
import com.dto.AndamentoDTO;
import com.dto.ArquivoDTO;
import com.dto.ChamadoDTO;
import com.dto.HoraDTO;
import com.dto.PrioridadeDTO;
import com.dto.StatusDTO;
import com.dto.UsuarioDTO;
import com.util.DataUtil;
import static com.util.FacesUtil.*;
import com.util.StringUtils;
import com.util.ThreadEmail;
import com.util.ValidadorCampo;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.annotation.ManagedProperty;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

@Named(value = "andamentoBean")
@ViewScoped
public class AndamentoBean extends GenericBean<ChamadoDTO> implements Serializable {

    private static final long serialVersionUID = 100002L;
    
    private ArquivoDTO arquivo;
    private AndamentoDTO andamentoDTO = new AndamentoDTO();
    private UsuarioDTO observador = new UsuarioDTO();
    private ChamadoDTO chamadoDTO = new ChamadoDTO();
    private ChamadoDTO chamadoDTOold = new ChamadoDTO();
    private AndamentoBO andamentoBO = AndamentoBO.getInstance();
    private ConfiguracaoBO configuracaoBO = ConfiguracaoBO.getInstance();
    private ChamadoBO chamadoBO = ChamadoBO.getInstance();
    private StringBuilder builder = new StringBuilder();
    private String campoDescricaoAndamento;
    private boolean mostrarPanel;
    private boolean alterandoAndamento;
    private boolean mostrarObservador;
    private HoraDTO horaDTO = new HoraDTO();
    private HoraBean horaBean = new HoraBean();
    private Long codigoChamadoPesquisa;
    private Part file;
    @ManagedProperty(value = "#{chamadoBean}")
    private ChamadoBean chamadoBean;

    public AndamentoBean() {        
    }

    @Override
    public String novo() {
        return "chamadoPesquisar";
    }

    private void gerarLog(String... arqs) {
        builder.append(arqs[0]);
        builder.append(arqs[1]);
        builder.append(" para ");
        builder.append(arqs[2]);
        alteradoPor();
    }

    private void alteradoPor() {
        builder.append(" por ");
        builder.append(getUsuarioDTO().getNome());
        builder.append("<br/>");
    }

    private void logMudancaUsuarioAtribuido() {
        int inicio = builder.length();
        builder.append("* ");
        int fim = builder.length();
        boolean entro = false;
        if (getChamadoDTO().getUsuarioAtribuidoDTO() == null && getChamadoDTOold().getUsuarioAtribuidoDTO() != null) {
            builder.append("Atribuído");
            entro = true;
        } else if (getChamadoDTOold().getUsuarioAtribuidoDTO() != null) {
            builder.append("Usuário modificado de ");
            builder.append("( ");
            builder.append(getChamadoDTOold().getUsuarioAtribuidoDTO().getNome());
            builder.append(" )");
            entro = true;
        } else if (getChamadoDTO().getUsuarioAtribuidoDTO() != null) {
            builder.append("Atribuído");
            entro = true;
        }
        if (entro) {
            builder.append(" para ");
        }
        if (getChamadoDTO().getUsuarioAtribuidoDTO() == null && getChamadoDTOold().getUsuarioAtribuidoDTO() != null) {
            builder.append(" o setor ");
            builder.append(getChamadoBean().getSetorAndamento().getDescricao());
            entro = true;
        } else if (getChamadoDTO().getUsuarioAtribuidoDTO() != null) {
            builder.append("( ");
            builder.append(getChamadoDTO().getUsuarioAtribuidoDTO().getNome());
            builder.append(" )");
            entro = true;
        }
        if (entro) {
            alteradoPor();
        } else {
            builder.delete(inicio, fim);
        }
    }

    /**
     * Log de arquivo
     * @param nome exemplo trash.gif arquivo.getNome();
     * @param acao Excluido, Adicionado Vetor final String posição
     */
    private void logAdicionarArquivo(String nome, final String acao) {
        builder.append("* Arquivo ");
        builder.append(acao);
        builder.append(nome);
        alteradoPor();
    }

    public String redirecionarHora() {
        return "horaIncluirAlterar";
    }

    private void criarAndamentoMudancaNasPropriedadesDoChamado() throws Throwable {
        AndamentoDTO andamento = new AndamentoDTO(getChamadoDTO(), getUsuarioDTO(), new Date());
        if (builder.toString().equals("")) {
            andamento.setLog(null);
        } else {
            builder.append("<br/>");
            andamento.setLog(builder.toString());
        }

        if (getCampoDescricaoAndamento().isEmpty()) {
            andamento.setDescricao(null);
        } else {
            andamento.setDescricao(getCampoDescricaoAndamento());
        }

        andamentoBO.save(andamento);
        enviarEmailParaObservadores(andamento);
        setCampoDescricaoAndamento("");
    }

    private void enviarEmailParaObservadores(AndamentoDTO andamento) {
        HashSet<UsuarioDTO> part = new HashSet<>();

        if(configuracaoBO.verificarUsuarioAutorRecebeEmail(this.chamadoDTO.getUsuarioAutorDTO())){                    
            part.add(this.chamadoDTO.getUsuarioAutorDTO());
        }
        
        if (!part.contains(this.chamadoDTO.getUsuarioAtribuidoDTO()) && this.chamadoDTO.getUsuarioAtribuidoDTO() != null) {
            if(configuracaoBO.verificarUsuarioAtribuidoRecebeEmail(this.chamadoDTO.getUsuarioAtribuidoDTO())){                    
                part.add(this.chamadoDTO.getUsuarioAtribuidoDTO());
            }            
        }

        for (UsuarioDTO u : this.chamadoDTO.getObservadores()) {
            if (!part.contains(u)) {
                if(configuracaoBO.verificarUsuarioObservadorRecebeEmail(u)){                    
                    part.add(u);
                }
            }
        }
        StringBuilder msg = new StringBuilder();
        msg.append("<html><body><font size=3>");
        msg.append("<a href=\"");
        msg.append(getContextPath());
        msg.append("/pages/chamado/andamentoChamado.jsf?codigo=");
        msg.append(chamadoDTO.getCodigo());
        msg.append("\">");
        msg.append("<b>Chamado:</b> ");
        msg.append(this.chamadoDTO.getTitulo());
        msg.append("</a>");
        msg.append("         <b>Data:</b> ");
        msg.append(DataUtil.dataForStringPadrao(andamento.getDataHora()));
        msg.append("<br/><br/>");
        if (andamento.getLog() != null) {
            msg.append("<hr><b>LOG</b><br>");
            msg.append(andamento.getLog());
        }
        if (andamento.getDescricao() != null) {
            msg.append("<hr><b>Comentario:</b><br/>");
            msg.append(andamento.getDescricao());
        }
        msg.append("<hr></font></body></html>");

        ThreadEmail te = new ThreadEmail(new ArrayList<>(part), msg.toString(), "[Intranet - #" + this.chamadoDTO.getCodigo() + "] " + this.chamadoDTO.getTitulo());        
        te.start();
    }

    private void modificacoesNoAndamentoEChamado() throws Throwable {
        if (getChamadoDTO().getStatusDTO().getCodigo() == 6) {
            getChamadoDTO().setDataHoraFechamento(new Date());
        }
        
        if (!getChamadoDTO().getStatusDTO().equals(getChamadoDTOold().getStatusDTO())) {
            gerarLog("* Status modificado de ", getChamadoDTOold().getStatusDTO().getDescricao(), getChamadoDTO().getStatusDTO().getDescricao());
        }

        if (getChamadoDTOold().getProgresso() != getChamadoDTO().getProgresso()) {
            gerarLog("* Progresso modificado de ", String.valueOf(getChamadoDTOold().getProgresso()), String.valueOf(getChamadoDTO().getProgresso()));
        }

        if (!getChamadoDTOold().getPrioridadeDTO().equals(getChamadoDTO().getPrioridadeDTO())) {
            gerarLog("* Prioridade modificada de ", getChamadoDTOold().getPrioridadeDTO().getDescricao(), getChamadoDTO().getPrioridadeDTO().getDescricao());
        }

        if (getChamadoDTOold().getUsuarioAtribuidoDTO() == null || getChamadoDTO().getUsuarioAtribuidoDTO() == null
                || !getChamadoDTOold().getUsuarioAtribuidoDTO().equals(getChamadoDTO().getUsuarioAtribuidoDTO())) {
            logMudancaUsuarioAtribuido();
        }

        if (getChamadoDTOold().getDataPrevista() != null) {
            if (!getChamadoDTOold().getDataPrevista().equals(getChamadoDTO().getDataPrevista())) {
                gerarLog("* Data prevista modificada de ", new SimpleDateFormat("dd/MM/yyyy").format(getChamadoDTOold().getDataPrevista()),
                        new SimpleDateFormat("dd/MM/yyyy").format(getChamadoDTO().getDataPrevista()));
            }
        }

        if (!getChamadoDTOold().getTitulo().equals(getChamadoDTO().getTitulo())) {
            gerarLog("* Título modificado de ", getChamadoDTOold().getTitulo(), getChamadoDTO().getTitulo());
        }

        if (!(builder.toString().equals("") && (StringUtils.isEmpty(getCampoDescricaoAndamento())))) {
            criarAndamentoMudancaNasPropriedadesDoChamado();
        }
        builder = new StringBuilder();
    }

    public void mostrarPanelAtualizar() {
        setAlterandoAndamento(false);
        if (!mostrarPanel) {
            mostrarPanel = true;
            chamadoDTOold = copiarInstancia(chamadoDTO);
        } else {
            try {
                chamadoDTO = copiarInstancia(chamadoDTOold);
            } catch (Throwable ex) {
            }
            mostrarPanel = false;
        }
    }
    
    public List<PrioridadeDTO> getListaPrioridade(){
        return PrioridadeBO.getInstance().findAll();
    }

    private String mensagemNoArquivo(String nome) { //acao adicionado ou deletado
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Arquivo ");
        mensagem.append(nome);
        return mensagem.append(" adicionado com sucesso").toString();
    }

    public void criarArquivo() {
        try {
            boolean valida = true;
            for(ArquivoDTO arq : chamadoDTO.getArquivos()){
                if(file.getSubmittedFileName().equals(arq.getNome())){
                    valida = false;
                    break;
                }
            }
            if(valida){
                ArquivoDTO arquivoDTO = new ArquivoDTO();
                arquivoDTO.setInicialBuilder(builder.length());
                logAdicionarArquivo(file.getSubmittedFileName(), "Adicionado ");
                arquivoDTO.setFinalBuilder(builder.length());
                arquivoDTO.setArquivo(IOUtils.toByteArray(file.getInputStream()));
                arquivoDTO.setNome(file.getSubmittedFileName());
                arquivoDTO.setChamadoDTO(chamadoDTO);
                chamadoDTO.getArquivos().add(arquivoDTO);
                file = null;
                adicionarMensagemAviso(mensagemNoArquivo(arquivoDTO.getNome()));
            }else{
                adicionarMensagemErro("Já existe um arquivo adicionado com esse nome ");
            }
        } catch (Throwable t) {
            Logger.getLogger(AndamentoBean.class.getName()).log(Level.SEVERE, null, t);
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void removerArq(){
        for (int i = 0; i < chamadoDTO.getArquivos().size(); i++) {
            if (chamadoDTO.getArquivos().get(i).getNome().equals(arquivo.getNome())) {
                chamadoDTO.getArquivos().remove(i);
                try {
                    chamadoBO.update(chamadoDTO);
                    logAdicionarArquivo(arquivo.getNome(), "Excluido ");
                    AndamentoDTO andamento = new AndamentoDTO(new Date(), builder.toString(), chamadoDTO, getUsuarioDTO());
                    andamentoBO.save(andamento);
                    if (arquivo.getInicialBuilder() > 0) {
                        builder.delete(arquivo.getInicialBuilder(), arquivo.getFinalBuilder());
                    } else {
                        builder = new StringBuilder();
                }
                } catch (Exception e) {
                } catch (Throwable ex) {
                    Logger.getLogger(AndamentoBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        chamadoDTOold.getArquivos().remove(arquivo);
        arquivo = new ArquivoDTO();
    }

    private ChamadoDTO copiarInstancia(ChamadoDTO copia) {
        return new ChamadoDTO(copia.getDataPrevista(), copia.getProgresso(),
                copia.getTitulo(), copia.getPrioridadeDTO(), copia.getUsuarioAtribuidoDTO(),
                copia.getStatusDTO(), copia.getCodigo(), copia.getDescricao(),
                copia.getDataHoraAbertura(), copia.getUsuarioAutorDTO(),
                copia.getDataHoraFechamento(), copia.getObservadores(),
                copia.getArquivos(), copia.getUsuarioAtribuidoDTO());
    }

    @Override
    public String save() {
        getHoraDTO().setChamadoDTO(chamadoDTO);
        try {
            setSessionAttribute("hora", getHoraDTO());
            if (super.validacao(getChamadoDTO())) {
                return null;
            }
            if (getHoraDTO().getDataTrabalho() != null && getHoraDTO().getTempo() != 0) {
                horaBean.save();
            }
            modificacoesNoAndamentoEChamado();
            chamadoBO.update(getChamadoDTO());
            mostrarPanel = false;
            editadoSucesso();
        } catch (Throwable e) {
            Logger.getLogger(HoraBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public void validaCampo(List<String> erros, ChamadoDTO chamado) {
        horaBean.validaCampo(erros, getHoraDTO());
        ValidadorCampo.validarCampoVazio(chamado.getTitulo(), "TÍTULO", erros);
        ValidadorCampo.validarCampoNulo(chamado.getPrioridadeDTO(), "PRIORIDADE", erros);
        ValidadorCampo.validarCampoNulo(chamado.getStatusDTO(), "STATUS", erros);
        ValidadorCampo.validarCampoVazio(chamado.getDescricao(), "DESCRIÇÂO", erros);
    }

    @Override
    public String update() {
        try {
            andamentoBO.update(andamentoDTO);
        } catch (Throwable t) {
        }
        setAlterandoAndamento(false);
        return null;
    }

    public void habilitarEditarAndamento() {
        setAlterandoAndamento(!isAlterandoAndamento());
    }

    public void mostrarObservadorAtualizar() {
        setMostrarObservador(!isMostrarObservador());
    }

    public void removerObservador(UsuarioDTO dTO){
        getChamadoDTO().getObservadores().remove(dTO);
        getChamadoDTOold().getObservadores().remove(dTO);
        chamadoBO.update(getChamadoDTO());
    }
    
    public void adicionarObservador(ActionEvent event){
        try{
            if (getObservador() != null) {
                getChamadoDTO().getObservadores().add(getObservador());
                getChamadoDTOold().getObservadores().add(getObservador());
                chamadoBO.update(getChamadoDTO());
            }
        } catch (Exception e){      
            Logger.getLogger(AndamentoBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
    
    public void downloadArquivo(ArquivoDTO arquivo) {
        String nomeArquivo = arquivo.getNome();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"");
        response.setBufferSize(arquivo.getArquivo().length);                 
        try {                            
            response.getOutputStream().write(arquivo.getArquivo());
        } catch (IOException ex) {
            Logger.getLogger(AndamentoBean.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String arrumarData(Date d) throws ParseException {
        return DataUtil.dataHoraForStringPadrao(d);
    }

    @Override
    public String include() {
        return incluirAlterar();
    }

    @Override
    public String incluirAlterar() {
        return "andamentoIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "chamadoPesquisar";
    }

    public String chamado() {
        return "andamentoChamado";
    }

    public List<AndamentoDTO> getAndamentos() throws Throwable {
        return andamentoBO.pesquisarAndamentosPorChamada(getChamadoDTO());
    }

    public List<StatusDTO> getListaStatus() throws Throwable {
        return StatusBO.getInstance().findAll();
    }

    public ChamadoDTO getChamadoDTO() {
        return chamadoDTO;
    }

    public void setChamadoDTO(ChamadoDTO chamadoDTO) {
        this.chamadoDTO = chamadoDTO;
    }

    public ChamadoDTO getChamadoDTOold() {
        return chamadoDTOold;
    }

    public void setChamadoDTOold(ChamadoDTO chamadoDTOold) {
        this.chamadoDTOold = chamadoDTOold;
    }

    public AndamentoDTO getAndamentoDTO() {
        return andamentoDTO;
    }

    public void setAndamentoDTO(AndamentoDTO andamentoDTO) {
        this.andamentoDTO = andamentoDTO;
    }

    public String getCampoDescricaoAndamento() {
        return campoDescricaoAndamento;
    }

    public void setCampoDescricaoAndamento(String campoDescricaoAndamento) {
        this.campoDescricaoAndamento = campoDescricaoAndamento;
    }

    public boolean isMostrarPanel() {
        return mostrarPanel;
    }

    public void setMostrarPanel(boolean mostrarPanel) {
        this.mostrarPanel = mostrarPanel;
    }

    public boolean isAlterandoAndamento() {
        return alterandoAndamento;
    }

    public void setAlterandoAndamento(boolean alterandoAndamento) {
        this.alterandoAndamento = alterandoAndamento;
    }

    public HoraBean getHoraBean() {
        return horaBean;
    }

    public void setHoraBean(HoraBean horaBean) {
        this.horaBean = horaBean;
    }

    public HoraDTO getHoraDTO() {
        return horaDTO;
    }

    public void setHoraDTO(HoraDTO horaDTO) {
        this.horaDTO = horaDTO;
    }

    public boolean isMostrarObservador() {
        return mostrarObservador;
    }

    public void setMostrarObservador(boolean mostrarObservador) {
        this.mostrarObservador = mostrarObservador;
    }

    public UsuarioDTO getUsuarioDTO() {
        return ContextoBean.getContexto().getUsuarioLogado();
    }

    public List<UsuarioDTO> getListaUsuarioObservador() throws Throwable {
        List<UsuarioDTO> usuarioDTOs = UsuarioBO.getInstance().consultarUsuariosCampusUsuarioAutorChamado(chamadoDTO);
        usuarioDTOs.removeAll(chamadoBO.findById(chamadoDTO.getCodigo()).getObservadores());
        return usuarioDTOs;
    }

    public float getTempoGasto() throws Throwable {
        return HoraBO.getInstance().tempoGastoPorChamado(chamadoDTO);
    }

    public ArquivoDTO getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoDTO arquivo) {
        this.arquivo = arquivo;
    }

    public Long getCodigoChamadoPesquisa() {
        return codigoChamadoPesquisa;
    }

    public void setCodigoChamadoPesquisa(Long codigoChamadoPesquisa) {
        this.codigoChamadoPesquisa = codigoChamadoPesquisa;
        this.setChamadoDTO(chamadoBO.findById(codigoChamadoPesquisa));
    }

    public ChamadoBean getChamadoBean() {
        return chamadoBean;
    }

    public void setChamadoBean(ChamadoBean chamadoBean) {
        this.chamadoBean = chamadoBean;
    }

    public UsuarioDTO getObservador() {
        return observador;
    }

    public void setObservador(UsuarioDTO observador) {
        this.observador = observador;
    }

    @Override
    public List<ChamadoDTO> dadosPesquisa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
