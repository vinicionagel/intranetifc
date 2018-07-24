package com.bean;

import com.auxiliar.Contexto;
import com.bo.AndamentoBO;
import com.bo.ChamadoBO;
import com.bo.ChamadoInfraBO;
import com.bo.ChamadoServicoBO;
import com.bo.LocalizacaoBO;
import com.bo.PrioridadeBO;
import com.bo.SetorBO;
import com.bo.StatusBO;
import com.bo.UsuarioBO;
import com.core.GenericBean;
import com.dto.AndamentoDTO;
import com.dto.ArquivoDTO;
import com.dto.CampusDTO;
import com.dto.ChamadoDTO;
import com.dto.ChamadoInfraDTO;
import com.dto.ChamadoServicoDTO;
import com.dto.LocalizacaoDTO;
import com.dto.PatrimonioDTO;
import com.dto.PrioridadeDTO;
import com.dto.ServicoDTO;
import com.dto.SetorDTO;
import com.dto.UsuarioDTO;
import com.util.DataUtil;
import static com.util.FacesUtil.*;
import com.util.SelectBoxUtil;
import com.util.ThreadEmail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.TabChangeEvent;

@ManagedBean(name = "chamadoBean")
@ViewScoped
public class ChamadoBean<T> extends GenericBean<T> implements Serializable {
    
    private static final long serialVersionUID = 100003L;
    
    protected ChamadoBO chamadoBO = ChamadoBO.getInstance();
    protected UsuarioBO usuarioBO = UsuarioBO.getInstance();
    protected AndamentoBO andamentoBO = AndamentoBO.getInstance();
    protected SetorBO setorBO = SetorBO.getInstance();
    protected StatusBO statusBO = StatusBO.getInstance();
    protected ChamadoInfraBO chamadoInfraBO = ChamadoInfraBO.getInstance();
    protected ChamadoServicoBO chamadoServicoBO = ChamadoServicoBO.getInstance();
    private SetorDTO setorAndamento;
    private PatrimonioDTO patrimonioAndamento;
    private ServicoDTO servicoAndamento;
    private Long codigoPesquisa;
    private long codigoSetor = 0l;
    private List<ArquivoDTO> arquivos = new ArrayList<>();
    private Part file;
    protected ChamadoDTO chamado = new ChamadoDTO();
    protected ArquivoDTO arquivo;
    protected LocalizacaoDTO localizacaoDTO = new LocalizacaoDTO();
    @ManagedProperty(value = "#{contextoBean}")
    private ContextoBean contextoBean;
    private int progresso;
    
    public void removerArq(){
        for (int i=0; i<arquivos.size(); i++){
            if (arquivos.get(i).getNome().equals(arquivo.getNome())){
                arquivos.remove(i);
                arquivo = null;
                break;
            }
        }
    }
    
    public void criarArquivo() {
        try {
            boolean valida = true;
            for (ArquivoDTO arq : arquivos) {
                if (file.getSubmittedFileName().equals(arq.getNome())) {
                    valida = false;
                    break;
                }
            }
            if (valida) {
                ArquivoDTO arquivoDTO = new ArquivoDTO();
                arquivoDTO.setArquivo(IOUtils.toByteArray(file.getInputStream()));
                arquivoDTO.setNome(file.getSubmittedFileName());
                arquivoDTO.setChamadoDTO(chamado);
                arquivos.add(arquivoDTO);
                file = null;
                adicionarMensagemAviso((arquivoDTO.getNome()));
            } else {
                adicionarMensagemErro("Já existe um arquivo adicionado com esse nome ");
            }
        } catch (Throwable t) {
            Logger.getLogger(AndamentoBean.class.getName()).log(Level.SEVERE, null, t);
        }
    }

    
    public ChamadoBean() {
        tituloLayoutUnit = "Chamado";
    }

    @Override
    public String incluirAlterar() {
        return "chamadoIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "chamadoPesquisar";
    }

    public String andamento() {
        return "andamentoChamados";
    }

    @Override
    public String novo() {        
        this.setAlterando(false);
        return pesquisar();
    }

    @Override
    public String save() throws Throwable {
        return null;
    }

    public void criarAndamento(){
        andamentoBO.save(new AndamentoDTO(new Date(), "* Chamado aberto por ".concat(ContextoBean.getContexto().getUsuarioLogado().getNome()), (ChamadoDTO) this.objeto, ContextoBean.getContexto().getUsuarioLogado()));
    }
    
    protected String criarTextoEnvioDeEmail(ChamadoDTO chamadoLocal){
        StringBuilder msg = new StringBuilder();
        msg.append("<a href=\"");
        msg.append(getContextPath());
        msg.append("/pages/chamado/andamentoChamado.jsf?codigo=");
        msg.append(chamadoLocal.getCodigo());
        msg.append("\">");
        msg.append("<b>Chamado:</b> ");
        msg.append(chamadoLocal.getTitulo());
        msg.append("</a>");
        return msg.toString();
    }
    
    protected void enviarEmailResponsavel(List<UsuarioDTO> to,String texto){
        if (!to.isEmpty()){
            StringBuilder msg = new StringBuilder();
            msg.append("<html><body><font size=3>");
            msg.append("Parabéns seu setor tem um novo(s) chamado(s)! Você pode ver mais detalhes no(s) link(s) abaixo <br/>");
            msg.append(texto);
            msg.append("<br/>         <b>Data:</b> ");
            msg.append(DataUtil.dataForStringPadrao(chamado.getDataHoraAbertura()));
            msg.append("<br/><br/>");
            msg.append("<hr></font></body></html>");
            StringBuilder msgTitulo = new StringBuilder();
            msgTitulo.append("[Intranet - #");
            if (chamado.getCodigo() == null){
                msgTitulo.append("Vários patrimonio] ").append(chamado.getTitulo());
            }else {
                msgTitulo.append(chamado.getCodigo()).append("]").append(chamado.getTitulo());
            }
            ThreadEmail te = new ThreadEmail(new ArrayList<>(to), msg.toString(), msgTitulo.toString());
            te.start();
        }
    }
    
    @Override
    public String update() {
        setAlterando(true);
        return andamento();
    }

    @Override
    public String include() {
        this.setAlterando(false);
        return incluirAlterar();
    }

    public void onTabChange(TabChangeEvent event) {
        try {
            setTabIndex(Integer.parseInt(event.getTab().getId().charAt(event.getTab().getId().length() - 1) + ""));
        } catch (Exception e) {
            Logger.getLogger(ChamadoBean.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            setTabIndex(1);
        }
    }

    public int getTabIndex() {
        try {
            return Integer.parseInt(getSessionAttribute("tabIndexChamado").toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public void setTabIndex(int tabIndex) {
        setSessionAttribute("tabIndexChamado", tabIndex);
    }

    public CampusDTO getCampusDTO() {
        return ContextoBean.getContexto().getCampusAtual();
    }

    public List<UsuarioDTO> getListaUsuario() throws Throwable {
        return usuarioBO.consultarUsuariosMesmoCampus(ContextoBean.getContexto().getUsuarioLogado());
    }

    public List<SetorDTO> getSetoresUsuario() throws Throwable {
        return setorBO.pesquisarPorCampus(ContextoBean.getContexto().getCampusAtual());
    }

    public Long getCodigoPesquisa() {
        return codigoPesquisa;
    }

    public long getCodigoSetor() {
        return codigoSetor;
    }

    public void setCodigoSetor(long codigoSetor) {
        this.codigoSetor = codigoSetor;
    }

    public void setCodigoPesquisa(Long codigoPesquisa) throws Throwable {
        ChamadoServicoDTO servicoDTO = chamadoServicoBO.findById(codigoPesquisa);
        ChamadoInfraDTO infraDTO = chamadoInfraBO.findById(codigoPesquisa);
        Contexto c = ContextoBean.getContexto();
        if (infraDTO != null) {
            setorAndamento = infraDTO.getPatrimonioDTO().getSetorDTO();
            patrimonioAndamento = infraDTO.getPatrimonioDTO();
            setSessionAttribute("chamadoDTO", infraDTO);            
            c.setCampusAtual(infraDTO.getPatrimonioDTO().getSetorDTO().getCampusDTO());
            setSessionAttribute("contexto", c);
            getContextoBean().setSomenteCampus(infraDTO.getPatrimonioDTO().getSetorDTO().getCampusDTO());
        } else if (servicoDTO != null) {
            setorAndamento = servicoDTO.getServicoDTO().getTipoServicoDTO().getSetorDTO();
            servicoAndamento = servicoDTO.getServicoDTO();
            setSessionAttribute("chamadoDTO", servicoDTO);            
            c.setCampusAtual(servicoDTO.getServicoDTO().getTipoServicoDTO().getSetorDTO().getCampusDTO());
            setSessionAttribute("contexto", c);
            getContextoBean().setSomenteCampus(servicoDTO.getServicoDTO().getTipoServicoDTO().getSetorDTO().getCampusDTO());
        }
    }

    public List<SelectItem> getSetorSelectItens() throws Throwable {
        return new SelectBoxUtil().retornaListaEmSelectItem(getSetoresUsuario());
    }

    @Override
    public void validaCampo(List<String> erros, T instance) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void removerAtributosSessao(){
        removeSessionAttribute("statusGrafico");
        removeSessionAttribute("setorGrafico");
        removeSessionAttribute("dataInicial");
        removeSessionAttribute("dataFinal");
    }
    
    public SetorDTO getSetorAndamento() {
        return setorAndamento;
    }

    public PatrimonioDTO getPatrimonioAndamento() {
        return patrimonioAndamento;
    }

    public ServicoDTO getServicoAndamento() {
        return servicoAndamento;
    }

    public ContextoBean getContextoBean() {
        return contextoBean;
    }

    public void setContextoBean(ContextoBean contextoBean) {
        this.contextoBean = contextoBean;
    }

    public List<ArquivoDTO> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoDTO> arquivos) {
        this.arquivos = arquivos;
    }
    
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    public void setArquivo(ArquivoDTO arquivo) {
        this.arquivo = arquivo;
    }
    
    public List<LocalizacaoDTO> getListaLocalizacao() {
        return LocalizacaoBO.getInstance().pesquisarPorCampus(getCampusDTO());
    }

    public LocalizacaoDTO getLocalizacaoDTO() {
        return localizacaoDTO;
    }
    
    public void setLocalizacaoDTO(LocalizacaoDTO localizacaoDTO) {
        this.localizacaoDTO = localizacaoDTO;
    }

    @Override
    public List<T> dadosPesquisa() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<PrioridadeDTO> getListaPrioridade(){
        return PrioridadeBO.getInstance().findAll();
    }

    public int getProgresso() {
        return progresso;
    }

    public void setProgresso(int progresso) {
        this.progresso = progresso;
    }
}