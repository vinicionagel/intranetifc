package com.bean;

import com.bo.AcaoDisciplinarBO;
import com.bo.AndamentoOcorrenciaBO;
import com.bo.AndamentoOcorrenciaEditadoBO;
import com.bo.ConfiguracaoBO;
import com.bo.OcorrenciaBO;
import com.bo.PrioridadeBO;
import com.bo.SetorBO;
import com.bo.StatusOcorrenciaBO;
import com.bo.TipoOcorrenciaBO;
import com.bo.UsuarioBO;
import com.core.GenericBean;
import com.dto.AcaoDisciplinarDTO;
import com.dto.AndamentoOcorrenciaDTO;
import com.dto.AndamentoOcorrenciaEditadoDTO;
import com.dto.ArquivoOcorrenciaDTO;
import com.dto.OcorrenciaDTO;
import com.dto.PrioridadeDTO;
import com.dto.SetorDTO;
import com.dto.StatusOcorrenciaDTO;
import com.dto.TipoOcorrenciaDTO;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

@ManagedBean(name = "andamentoOcorrenciaBean")
@ViewScoped
public class AndamentoOcorrenciaBean extends GenericBean<OcorrenciaDTO> implements Serializable {

    private static final long serialVersionUID = 100002L;

    private ArquivoOcorrenciaDTO arquivoOcorrencia;
    private AndamentoOcorrenciaDTO andamentoOcorrenciaDTO = new AndamentoOcorrenciaDTO();
    private SetorDTO observador = new SetorDTO();
    private OcorrenciaDTO ocorrenciaDTO;
    private OcorrenciaDTO ocorrenciaDTOold;
    private AndamentoOcorrenciaBO andamentoOcorrenciaBO = AndamentoOcorrenciaBO.getInstance();
    private ConfiguracaoBO configuracaoBO = ConfiguracaoBO.getInstance();
    private OcorrenciaBO ocorrenciaBO = OcorrenciaBO.getInstance();
    private StringBuilder builder = new StringBuilder();
    private String campoDescricaoAndamento;
    private boolean mostrarPanel;
    private boolean alterandoAndamento;
    private boolean mostrarObservador;
    private boolean somenteAndamento;
    private Long codigoOcorrenciaPesquisa;
    private Part file;
    @ManagedProperty(value = "#{ocorrenciaBean}")
    private OcorrenciaBean ocorrenciaBean;
    private List<SetorDTO> observadores;

    public AndamentoOcorrenciaBean() {
        HttpServletRequest request = getRequest();
        if (request.getParameter("codigo") != null) {
            ocorrenciaDTO = ocorrenciaBO.findById(Long.parseLong(request.getParameter("codigo")));
            alterando = true;
        }
    }

    @Override
    public String novo() {
        return "ocorrenciaPesquisar";
    }

    private void gerarLog(String... arqs) {
        builder.append(arqs[0]);
        builder.append(arqs[1]);
        builder.append(" para ");
        builder.append(arqs[2]);
        alteradoPor();
    }
    
    private void gerarLogEditar(String... arqs) {
        builder.append(arqs[0]);
        builder.append(" Editado em ");
        builder.append(arqs[1]);
        alteradoPor();
    }

    private void alteradoPor() {
        builder.append(" por ");
        builder.append(getUsuarioDTO().getNome());
        builder.append("<br/>");
    }
    
    private void logMudancaSetorAtribuido() {
        int inicio = builder.length();
        builder.append("* ");
        int fim = builder.length();
        boolean entro = false;
        if (getOcorrenciaDTO().getSetorDTOAtribuido() == null && getOcorrenciaDTOold().getSetorDTOAtribuido() != null) {
            builder.append("Atribuído");
            entro = true;
        } else if (getOcorrenciaDTOold().getSetorDTOAtribuido() != null) {
            builder.append("Setor Atribuído modificado de ");
            builder.append("( ");
            builder.append(getOcorrenciaDTOold().getSetorDTOAtribuido().getDescricao());
            builder.append(" )");
            entro = true;
        } else if (getOcorrenciaDTO().getSetorDTOAtribuido() != null) {
            builder.append("Atribuído");
            entro = true;
        }
        if (entro) {
            builder.append(" para ");
        }
        if (getOcorrenciaDTO().getSetorDTOAtribuido() != null && getOcorrenciaDTOold().getSetorDTOAtribuido() != null) {
            builder.append("( ");
            builder.append(getOcorrenciaDTO().getSetorDTOAtribuido().getDescricao());
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
     *
     * @param nome exemplo trash.gif arquivo.getNome();
     * @param acao Excluido, Adicionado Vetor final String posição
     */
    private void logAdicionarArquivo(String nome, final String acao) {
        builder.append("* Arquivo ");
        builder.append(acao);
        builder.append(nome);
        alteradoPor();
    }

    private void criarAndamentoMudancaNasPropriedadesDoOcorrencia() throws Throwable {
        AndamentoOcorrenciaDTO andamentoOcorrencia = new AndamentoOcorrenciaDTO(getOcorrenciaDTO(), getUsuarioDTO(), new Date());
        if (builder.toString().equals("")) {
            andamentoOcorrencia.setLog(null);
        } else {
            builder.append("<br/>");
            andamentoOcorrencia.setLog(builder.toString());
        }

        if (StringUtils.isEmpty(getCampoDescricaoAndamento())) {
            andamentoOcorrencia.setDescricao(null);
        } else {
            andamentoOcorrencia.setDescricao(getCampoDescricaoAndamento());
        }

        andamentoOcorrenciaBO.save(andamentoOcorrencia);
        enviarEmailOcorrencia(andamentoOcorrencia);
        setCampoDescricaoAndamento("");
    }

    private void enviarEmailOcorrencia(AndamentoOcorrenciaDTO andamentoOcorrencia) {
        HashSet<UsuarioDTO> part = new HashSet<>();
        StringBuilder setores = new StringBuilder();
        if (andamentoOcorrencia.getOcorrenciaDTO().getSetorDTOAtribuido() != null){
            setores.append(andamentoOcorrencia.getOcorrenciaDTO().getSetorDTOAtribuido().getCodigo()).append(",");
        }
        int tamanho = andamentoOcorrencia.getOcorrenciaDTO().getSetoresObervadores().size();
        for (int i = 0; i<tamanho; i++){
            setores.append(andamentoOcorrencia.getOcorrenciaDTO().getSetoresObervadores().get(i).getCodigo());
            setores.append(",");
        }
        setores.append(andamentoOcorrencia.getOcorrenciaDTO().getSetorDTOAutor().getCodigo());
        part.addAll(UsuarioBO.getInstance().pesquisarUsuariosPorSetores(setores.toString()));
        StringBuilder msg = new StringBuilder();
        msg.append("<html><body><font size=3>");
        msg.append("<a href=\"");
        msg.append(getContextPath());
        msg.append("/pages/ocorrencia/andamentoOcorrencia.jsf?codigo=");
        msg.append(ocorrenciaDTO.getCodigo());
        msg.append("\">");
        msg.append("<b>Ocorrencia:</b> ");
        msg.append(this.ocorrenciaDTO.getTitulo());
        msg.append("</a>");
        msg.append("         <b>Data:</b> ");
        msg.append(DataUtil.dataForStringPadrao(andamentoOcorrencia.getDataOcorrencia()));
        msg.append("<br/><br/>");
        if (andamentoOcorrencia.getLog() != null) {
            msg.append("<hr><b>LOG</b><br>");
            msg.append(andamentoOcorrencia.getLog());
        }
        if (andamentoOcorrencia.getDescricao() != null) {
            msg.append("<hr><b>Comentario:</b><br/>");
            msg.append(andamentoOcorrencia.getDescricao());
        }
        msg.append("<hr></font></body></html>");

        ThreadEmail te = new ThreadEmail(new ArrayList<>(part), msg.toString(), "[Intranet - #" + this.ocorrenciaDTO.getCodigo() + "] " + this.ocorrenciaDTO.getTitulo());
        te.start();
    }

    private void modificacoesNoAndamentoEOcorrencia() throws Throwable {
        if (getOcorrenciaDTO().getStatusOcorrenciaDTO().getCodigo() == 4) {
            getOcorrenciaDTO().setProgresso(100);
            getOcorrenciaDTO().setDataHoraFechamento(new Date());
            getOcorrenciaDTO().setProgresso(100);
        }
        if (!getOcorrenciaDTO().getAcaoDisciplinarDTO().getTipoOcorrenciaDTO().equals(getOcorrenciaDTOold().getAcaoDisciplinarDTO().getTipoOcorrenciaDTO())) {
                gerarLog("* Convertido de  ", getOcorrenciaDTOold().getAcaoDisciplinarDTO().getTipoOcorrenciaDTO().getDescricao(), getOcorrenciaDTO().getAcaoDisciplinarDTO().getTipoOcorrenciaDTO().getDescricao());
        }
        
        if (!getOcorrenciaDTO().getStatusOcorrenciaDTO().equals(getOcorrenciaDTOold().getStatusOcorrenciaDTO())) {
            gerarLog("* Status modificado de ", getOcorrenciaDTOold().getStatusOcorrenciaDTO().getDescricao(), getOcorrenciaDTO().getStatusOcorrenciaDTO().getDescricao());
        }
        
        if (getOcorrenciaDTO().getAcaoDisciplinarDTO() != null){
            if (!getOcorrenciaDTO().getAcaoDisciplinarDTO().equals(getOcorrenciaDTOold().getAcaoDisciplinarDTO())) {
                gerarLog("* Ação disciplinar modificade de ", getOcorrenciaDTOold().getAcaoDisciplinarDTO().getDescricao(), getOcorrenciaDTO().getAcaoDisciplinarDTO().getDescricao());
            }
        }
        if (getOcorrenciaDTOold().getProgresso() != getOcorrenciaDTO().getProgresso()) {
            gerarLog("* Progresso modificado de ", String.valueOf(getOcorrenciaDTOold().getProgresso()), String.valueOf(getOcorrenciaDTO().getProgresso()));
        }

        if (!getOcorrenciaDTOold().getPrioridadeDTO().getCodigo().equals(getOcorrenciaDTO().getPrioridadeDTO().getCodigo())) {
            gerarLog("* Prioridade modificada de ", String.valueOf(getOcorrenciaDTOold().getPrioridadeDTO().getDescricao()), String.valueOf(getOcorrenciaDTO().getPrioridadeDTO().getDescricao()));
        }

        if (getOcorrenciaDTOold().getSetorDTOAtribuido() == null || getOcorrenciaDTO().getSetorDTOAtribuido() == null
                || !getOcorrenciaDTOold().getSetorDTOAtribuido().equals(getOcorrenciaDTO().getSetorDTOAtribuido())) {
            logMudancaSetorAtribuido();
        }

        if (getOcorrenciaDTOold().getDataPrevista() != null) {
            if (!getOcorrenciaDTOold().getDataPrevista().equals(getOcorrenciaDTO().getDataPrevista())) {
                gerarLog("* Data prevista modificada de ", new SimpleDateFormat("dd/MM/yyyy").format(getOcorrenciaDTOold().getDataPrevista()),
                        new SimpleDateFormat("dd/MM/yyyy").format(getOcorrenciaDTO().getDataPrevista()));
            }
        }

        if (!getOcorrenciaDTOold().getTitulo().equals(getOcorrenciaDTO().getTitulo())) {
            gerarLog("* Título modificado de ", getOcorrenciaDTOold().getTitulo(), getOcorrenciaDTO().getTitulo());
        }

        if (!(builder.toString().equals("") && (StringUtils.isEmpty(getCampoDescricaoAndamento())))) {
            criarAndamentoMudancaNasPropriedadesDoOcorrencia();
        }
        builder = new StringBuilder();
    }
    
    public List<AcaoDisciplinarDTO> getListaAcaoDisciplinar(){
        return mostrarPanel == true ? 
                AcaoDisciplinarBO.getInstance().
                        pesquisarAcaoDisciplinarPorTipoOcorrencia(getOcorrenciaDTO().
                                getAcaoDisciplinarDTO().getTipoOcorrenciaDTO()) : 
                AcaoDisciplinarBO.getInstance().pesquisarAcaoDisciplinarPorTipoOcorrencia(new TipoOcorrenciaDTO(2l));
    }
     
    public void mostrarPanelAtualizar(boolean somenteAndamento) {
        setAlterandoAndamento(false);
        campoDescricaoAndamento = null;
        if (!mostrarPanel) {
            mostrarPanel = true;
            this.somenteAndamento = somenteAndamento;
            ocorrenciaDTOold = copiarInstancia(ocorrenciaDTO);
        } else {
            mostrarPanel = false;
            ocorrenciaDTO = copiarInstancia(ocorrenciaDTOold);
        }
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
            for (ArquivoOcorrenciaDTO arq : ocorrenciaDTO.getArquivos()) {
                if (file.getSubmittedFileName().equals(arq.getNome())) {
                    valida = false;
                    break;
                }
            }
            if (valida) {
                ArquivoOcorrenciaDTO arquivoOcorrenciaDTO = new ArquivoOcorrenciaDTO();
                arquivoOcorrenciaDTO.setInicialBuilder(builder.length());
                logAdicionarArquivo(file.getSubmittedFileName(), "Adicionado ");
                arquivoOcorrenciaDTO.setFinalBuilder(builder.length());
                arquivoOcorrenciaDTO.setArquivo(IOUtils.toByteArray(file.getInputStream()));
                arquivoOcorrenciaDTO.setNome(file.getSubmittedFileName());
                arquivoOcorrenciaDTO.setOcorrenciaDTO(ocorrenciaDTO);
                ocorrenciaDTO.getArquivos().add(arquivoOcorrenciaDTO);
                file = null;
                adicionarMensagemAviso(mensagemNoArquivo(arquivoOcorrenciaDTO.getNome()));
            } else {
                adicionarMensagemErro("Já existe um arquivo adicionado com esse nome ");
            }
        } catch (Throwable t) {
            Logger.getLogger(AndamentoOcorrenciaBean.class.getName()).log(Level.SEVERE, null, t);
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public void removerArq() {
        for (int i = 0; i < ocorrenciaDTO.getArquivos().size(); i++) {
            if (ocorrenciaDTO.getArquivos().get(i).getNome().equals(arquivoOcorrencia.getNome())) {
                ocorrenciaDTO.getArquivos().remove(i);
                try {
                    ocorrenciaBO.update(ocorrenciaDTO);
                    logAdicionarArquivo(arquivoOcorrencia.getNome(), "Excluido ");
                    AndamentoOcorrenciaDTO andamento = new AndamentoOcorrenciaDTO(new Date(), builder.toString(), ocorrenciaDTO, getUsuarioDTO());
                    andamentoOcorrenciaBO.save(andamento);
                    if (arquivoOcorrencia.getInicialBuilder() > 0) {
                        builder.delete(arquivoOcorrencia.getInicialBuilder(), arquivoOcorrencia.getFinalBuilder());
                    } else {
                        builder = new StringBuilder();
                    }
                } catch (Exception e) {
                } catch (Throwable ex) {
                    Logger.getLogger(AndamentoOcorrenciaBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        ocorrenciaDTOold.getArquivos().remove(arquivoOcorrencia);
        arquivoOcorrencia = new ArquivoOcorrenciaDTO();
    }

    private OcorrenciaDTO copiarInstancia(OcorrenciaDTO copia) {
        return new OcorrenciaDTO(copia.getAlunoDTO(),copia.getDataPrevista(), copia.getProgresso(),
                copia.getTitulo(), copia.getPrioridadeDTO(), copia.getSetorDTOAtribuido(),
                copia.getStatusOcorrenciaDTO(), copia.getCodigo(), copia.getDescricao(),
                copia.getDataHoraAbertura(), copia.getSetorDTOAutor(),
                copia.getDataHoraFechamento(), copia.getSetoresObervadores(),
                copia.getArquivos(), copia.getSetorDTOAtribuido(), copia.getAcaoDisciplinarDTO());
    }

    @Override
    public String save() {
        try {
            modificacoesNoAndamentoEOcorrencia();
            ocorrenciaBO.update(getOcorrenciaDTO());
            mostrarPanel = false;
            editadoSucesso();
        } catch (Throwable e) {
            Logger.getLogger(HoraBean.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    public void converterAcao(){
        ocorrenciaDTOold = copiarInstancia(ocorrenciaDTO);
    }
    
    @Override
    public void validaCampo(List<String> erros, OcorrenciaDTO ocorrencia) {
        ValidadorCampo.validarCampoVazio(ocorrencia.getTitulo(), "ASSUNTO", erros);
        ValidadorCampo.validarCampoNulo(ocorrencia.getPrioridadeDTO(), "PRIORIDADE", erros);
        ValidadorCampo.validarCampoNulo(ocorrencia.getStatusOcorrenciaDTO(), "STATUS", erros);
        ValidadorCampo.validarCampoVazio(ocorrencia.getDescricao(), "DESCRIÇÂO", erros);
    }

    @Override
    public String update() {
        try {
            andamentoOcorrenciaBO.update(andamentoOcorrenciaDTO);
            AndamentoOcorrenciaEditadoDTO andamentoOcorrenciaEditadoDTO = new AndamentoOcorrenciaEditadoDTO();
            andamentoOcorrenciaEditadoDTO.setCodigo(andamentoOcorrenciaEditadoDTO.getCodigo());
            andamentoOcorrenciaEditadoDTO.setDataHoraUltimaEdicao(new Date());
            AndamentoOcorrenciaEditadoBO.getInstance().update(andamentoOcorrenciaEditadoDTO);
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

    public void removerObservador(SetorDTO dTO) {
        getOcorrenciaDTO().getSetoresObervadores().remove(dTO);
        ocorrenciaBO.update(getOcorrenciaDTO());
    }

    public void adicionarObservador(ActionEvent event) {
        try {
            if (getObservador() != null) {
                getOcorrenciaDTO().getSetoresObervadores().add(getObservador());;
                ocorrenciaBO.update(getOcorrenciaDTO());
            }
        } catch (Exception e) {
            Logger.getLogger(AndamentoOcorrenciaBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void downloadArquivo(ArquivoOcorrenciaDTO arquivoOcorrencia) {
        String nomeArquivo = arquivoOcorrencia.getNome();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + nomeArquivo + "\"");
        response.setBufferSize(arquivoOcorrencia.getArquivo().length);
        try {
            response.getOutputStream().write(arquivoOcorrencia.getArquivo());
        } catch (IOException ex) {
            Logger.getLogger(AndamentoOcorrenciaBean.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
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
        return "ocorrenciaPesquisar";
    }

    public String ocorrencia() {
        return "andamentoOcorrencia";
    }

    public List<AndamentoOcorrenciaDTO> getAndamentos() throws Throwable {
        return andamentoOcorrenciaBO.pesquisarAndamentosDaOcorrencia(getOcorrenciaDTO());
    }

    public List<StatusOcorrenciaDTO> getListaStatus() throws Throwable {
        return StatusOcorrenciaBO.getInstance().findAll();
    }

    public OcorrenciaDTO getOcorrenciaDTO() {
        return ocorrenciaDTO;
    }

    public void setOcorrenciaDTO(OcorrenciaDTO ocorrenciaDTO) {
        this.ocorrenciaDTO = ocorrenciaDTO;
    }

    public OcorrenciaDTO getOcorrenciaDTOold() {
        return ocorrenciaDTOold;
    }

    public void setOcorrenciaDTOold(OcorrenciaDTO ocorrenciaDTOold) {
        this.ocorrenciaDTOold = ocorrenciaDTOold;
    }

    public AndamentoOcorrenciaDTO getAndamentoOcorrenciaDTO() {
        return andamentoOcorrenciaDTO;
    }

    public void setAndamentoOcorrenciaDTO(AndamentoOcorrenciaDTO andamentoOcorrenciaDTO) {
        this.andamentoOcorrenciaDTO = andamentoOcorrenciaDTO;
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

    public boolean isMostrarObservador() {
        return mostrarObservador;
    }

    public void setMostrarObservador(boolean mostrarObservador) {
        this.mostrarObservador = mostrarObservador;
    }

    public UsuarioDTO getUsuarioDTO() {
        return ContextoBean.getContexto().getUsuarioLogado();
    }

    public ArquivoOcorrenciaDTO getArquivo() {
        return arquivoOcorrencia;
    }

    public void setArquivo(ArquivoOcorrenciaDTO arquivoOcorrencia) {
        this.arquivoOcorrencia = arquivoOcorrencia;
    }

    public Long getCodigoOcorrenciaPesquisa() {
        return codigoOcorrenciaPesquisa;
    }

    public void setCodigoOcorrenciaPesquisa(Long codigoOcorrenciaPesquisa) throws Throwable {
        this.codigoOcorrenciaPesquisa = codigoOcorrenciaPesquisa;
        this.setOcorrenciaDTO(ocorrenciaBO.findById(codigoOcorrenciaPesquisa));
    }

    public OcorrenciaBean getOcorrenciaBean() {
        return ocorrenciaBean;
    }

    public void setOcorrenciaBean(OcorrenciaBean ocorrenciaBean) {
        this.ocorrenciaBean = ocorrenciaBean;
    }

    public void setObservador(SetorDTO observador) {
        this.observador = observador;
    }

    @Override
    public List<OcorrenciaDTO> dadosPesquisa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public SetorDTO getObservador() {
        return observador;
    }
    
    public List<PrioridadeDTO> getListaPrioridade(){
        return PrioridadeBO.getInstance().findAll();
    }

    public List<SetorDTO> getObservadores() {
        List<SetorDTO> setorDTOs = SetorBO.getInstance().pesquisarPorCampusOcorrencia(ContextoBean.getContexto().campusAtual);
        setorDTOs.removeAll(ocorrenciaBO.findById(ocorrenciaDTO.getCodigo()).getSetoresObervadores());
        return setorDTOs;
    }

    public void setObservadores(List<SetorDTO> observadores) {
        this.observadores = observadores;
    }

    public boolean isSomenteAndamento() {
        return somenteAndamento;
    }

    public void setSomenteAndamento(boolean somenteAndamento) {
        this.somenteAndamento = somenteAndamento;
    }
    
}
