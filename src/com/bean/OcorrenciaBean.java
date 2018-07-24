package com.bean;

import com.auxiliar.Contexto;
import com.bo.AcaoDisciplinarBO;
import com.bo.AlunoBO;
import com.bo.AndamentoOcorrenciaBO;
import com.bo.OcorrenciaBO;
import com.bo.PrioridadeBO;
import com.bo.SetorBO;
import com.bo.StatusOcorrenciaBO;
import com.bo.TipoOcorrenciaBO;
import com.core.GenericBean;
import com.dto.AcaoDisciplinarDTO;
import com.dto.AlunoDTO;
import com.dto.AndamentoOcorrenciaDTO;
import com.dto.ArquivoDTO;
import com.dto.ArquivoOcorrenciaDTO;
import com.dto.OcorrenciaDTO;
import com.dto.SetorDTO;
import com.dto.StatusOcorrenciaDTO;
import com.dto.TipoOcorrenciaDTO;
import static com.util.FacesUtil.adicionarMensagemAviso;
import static com.util.FacesUtil.adicionarMensagemAviso;
import static com.util.FacesUtil.adicionarMensagemErro;
import static com.util.FacesUtil.inseridoSucesso;
import static com.util.FacesUtil.setSessionAttribute;
import com.util.ValidadorCampo;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

@Named(value = "ocorrenciaBean")
@ViewScoped
public class OcorrenciaBean extends GenericBean<OcorrenciaDTO> implements Serializable {
    
    private AlunoDTO aluno;
    private OcorrenciaDTO ocorrenciaDTO = new OcorrenciaDTO();
    private List<ArquivoOcorrenciaDTO> arquivos =  new ArrayList();
    protected ArquivoOcorrenciaDTO arquivo;
    private ContextoBean contextoBean;
    private Long codigoPesquisa;
    private TipoOcorrenciaDTO tipoOcorrencia;
    private boolean observador = false;
    private Part file;
    
    public OcorrenciaBean() {
        ocorrenciaDTO.setDataHoraAbertura(new Date());
        tituloLayoutUnit = "Ocorrência";
    }
    
    @Override
    public void validaCampo(List<String> erros, OcorrenciaDTO instance) {
        ValidadorCampo.validarCampoVazio(ocorrenciaDTO.getTitulo(), "TÍTULO", erros);
        ValidadorCampo.validarCampoNulo(ocorrenciaDTO.getAlunoDTO(), "ALUNO", erros);
        ValidadorCampo.validarCampoNulo(ocorrenciaDTO.getSetorDTOAutor(), "SETOR AUTOR", erros);
        ValidadorCampo.validarCampoNulo(ocorrenciaDTO.getSetorDTOAtribuido(), "ENCAMINHAR PARA", erros);
        ValidadorCampo.validarCampoNulo(ocorrenciaDTO.getAcaoDisciplinarDTO(), "TIPO DE OCORRÊNCIA", erros);
        ValidadorCampo.validarCampoVazio(ocorrenciaDTO.getDescricao(), "DESCRIÇÃO", erros);
    }

    @Override
    public String save() {
        try {
            if (super.validacao(objeto)) {
                return null;
            }
            ocorrenciaDTO.setPrioridadeDTO(PrioridadeBO.getInstance().findById(2));
            ocorrenciaDTO.setStatusOcorrenciaDTO(StatusOcorrenciaBO.getInstance().findById(1));
            ocorrenciaDTO.setArquivos(getArquivos());
            ocorrenciaDTO.setStatusOcorrenciaDTO(StatusOcorrenciaBO.getInstance().findById(1));
            OcorrenciaBO.getInstance().save(ocorrenciaDTO);
            criarAndamento();
            inseridoSucesso();
            return novo();
        } catch (Exception e) {
            adicionarMensagemErro(e);
            return null;
        }
    }
    
    public Long getCodigoPesquisa() {
        return codigoPesquisa;
    }
    
    public void setorTroca(AjaxBehaviorEvent event) {
        ocorrenciaDTO.setSetorDTOAtribuido(ocorrenciaDTO.getSetorDTOAutor());
    }
    
    public void setCodigoPesquisa(Long codigoPesquisa) throws Throwable {
        OcorrenciaDTO ocorrenciaDTOAux = OcorrenciaBO.getInstance().findById(codigoPesquisa);
        Contexto c = ContextoBean.getContexto();
        setSessionAttribute("ocorrenciaDTO", ocorrenciaDTOAux);            
        c.setCampusAtual(ocorrenciaDTOAux.getSetorDTOAutor().getCampusDTO());
        setSessionAttribute("contexto", c);
    }
    
    public ContextoBean getContextoBean() {
        return contextoBean;
    }

    public void setContextoBean(ContextoBean contextoBean) {
        this.contextoBean = contextoBean;
    }
        
    @Override
    public String incluirAlterar() {
        return "ocorrenciaIncluirAlterar";
    }

    @Override
    public String pesquisar() {
        return "ocorrenciaPesquisar";
    }

    @Override
    public String novo() {        
        this.setAlterando(false);
        return pesquisar();
    }

    @Override
    public List<OcorrenciaDTO> dadosPesquisa() {
        if(observador){
            if(aluno != null){
                OcorrenciaBO.getInstance().pesquisarObservadasPorAluno(aluno, SetorBO.getInstance().consultarSetoresDoUsuarioPorCampus(ContextoBean.getContexto()));
            }
            return OcorrenciaBO.getInstance().pesquisarObservadas(SetorBO.getInstance().consultarSetoresDoUsuarioPorCampus(ContextoBean.getContexto()));
        }else{
            if(aluno != null){
                return OcorrenciaBO.getInstance().pesquisarPorAlunoEAtribuidosParaSetor(aluno, SetorBO.getInstance().consultarSetoresDoUsuarioPorCampus(ContextoBean.getContexto()));
            }
            return OcorrenciaBO.getInstance().pesquisarAtribuidosParaSetor(SetorBO.getInstance().consultarSetoresDoUsuarioPorCampus(ContextoBean.getContexto()));
        }
    }
    
    public void pesquisarObservador(){
        observador = true;
    }
    
    public void pesquisarOcorrencia(){
        observador = false;
    }
    
    public void criarAndamento(){
        AndamentoOcorrenciaBO.getInstance().save(new AndamentoOcorrenciaDTO(new Date(), "* Ocorrencia aberta por ".concat(ContextoBean.getContexto().getUsuarioLogado().getNome()), this.ocorrenciaDTO, ContextoBean.getContexto().getUsuarioLogado()));
    }
    
    
    public void criarArquivo() {
        try {
            if (arquivos.contains(new ArquivoOcorrenciaDTO(file.getSubmittedFileName()))) {
                adicionarMensagemErro("Já existe um arquivo adicionado com esse nome ");
            } else {
                ArquivoOcorrenciaDTO arquivoOcorrenciaDTO = new ArquivoOcorrenciaDTO();
                arquivoOcorrenciaDTO.setArquivo(IOUtils.toByteArray(file.getInputStream()));
                arquivoOcorrenciaDTO.setNome(file.getSubmittedFileName());
                arquivoOcorrenciaDTO.setOcorrenciaDTO(ocorrenciaDTO);
                arquivos.add(arquivoOcorrenciaDTO);
                file = null;
                adicionarMensagemAviso((arquivoOcorrenciaDTO.getNome()));
            }
        } catch (IOException t) {
            Logger.getLogger(AndamentoBean.class.getName()).log(Level.SEVERE, null, t);
        }
    }
    
    public void removerArq(ArquivoOcorrenciaDTO arquivoOcorrenciaDTO){
        arquivos.remove(arquivoOcorrenciaDTO);
    }
    
    public List<AlunoDTO> alunos(String texto){
        return AlunoBO.getInstance().pesquisarAlunoPorNome(texto.toUpperCase(),10);
    }
    
    public AlunoDTO getAluno() {
        return aluno;
    }

    public void setAluno(AlunoDTO aluno) {
        this.aluno = aluno;
    }

    public OcorrenciaDTO getOcorrenciaDTO() {
        return ocorrenciaDTO;
    }

    public void setOcorrenciaDTO(OcorrenciaDTO ocorrenciaDTO) {
        this.ocorrenciaDTO = ocorrenciaDTO;
    }
    
    public List<SetorDTO> getListaSetoresUsuario(){
        List<SetorDTO> list = SetorBO.getInstance().consultarSetoresDoUsuarioOcorrenciaPorCampus(ContextoBean.getContexto());
        ocorrenciaDTO.setSetorDTOAtribuido(list.get(0));
        return list;
    }
    
    public List<SetorDTO> getListaSetores(){
        return SetorBO.getInstance().pesquisarPorCampusOcorrencia(ContextoBean.getContexto().getCampusAtual());
    }
    
    public List<TipoOcorrenciaDTO> getListaTipoOcorrencia(){
        return TipoOcorrenciaBO.getInstance().findAll();
    }
    
    public List<StatusOcorrenciaDTO> getListaStatusOcorrencia(){
        return StatusOcorrenciaBO.getInstance().findAll();
    }

    public List<AcaoDisciplinarDTO> getListaAcaoDisciplinar(){
        return AcaoDisciplinarBO.getInstance().pesquisarAcaoDisciplinarPorTipoOcorrencia(new TipoOcorrenciaDTO(1l));
    }
    
    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public List<ArquivoOcorrenciaDTO> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoOcorrenciaDTO> arquivos) {
        this.arquivos = arquivos;
    }

    public ArquivoOcorrenciaDTO getArquivo() {
        return arquivo;
    }

    public void setArquivo(ArquivoOcorrenciaDTO arquivo) {
        this.arquivo = arquivo;
    }

    public TipoOcorrenciaDTO getTipoOcorrencia() {
        return tipoOcorrencia;
    }

    public void setTipoOcorrencia(TipoOcorrenciaDTO tipoOcorrencia) {
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public boolean isObservador() {
        return observador;
    }

    public void setObservador(boolean observador) {
        this.observador = observador;
    }
    
}
