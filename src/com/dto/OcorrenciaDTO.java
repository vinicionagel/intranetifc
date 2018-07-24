package com.dto;

import com.core.GenericDTOIdDescricao;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;


@Entity
@Table(name = "ocorrencia")
public class OcorrenciaDTO extends GenericDTOIdDescricao{
    
    private static final long serialVersionUID = 2013L;
    
    @ManyToMany(cascade = CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinTable(name = "observador_setor",
    joinColumns = { @JoinColumn(name = "ocorrencia_codigo")},
    inverseJoinColumns = { @JoinColumn(name = "setor_codigo")})   
    private List<SetorDTO> setoresObervadores;     
    
    @ManyToOne
    @JoinColumn(name = "setor_codigo_autor")
    private SetorDTO setorDTOAutor;
    
    @ManyToOne
    @JoinColumn(name = "setor_codigo_atribuido")
    private SetorDTO setorDTOAtribuido;
    
    @ManyToOne
    @JoinColumn(name = "aluno_codigo")
    private AlunoDTO alunoDTO;
    
    @ManyToOne
    @JoinColumn(name = "status_ocorrencia_codigo")
    private StatusOcorrenciaDTO statusOcorrenciaDTO;
    
    @ManyToOne
    @JoinColumn(name = "acao_disciplinar_codigo")
    private AcaoDisciplinarDTO acaoDisciplinarDTO;
    
    @Column(name = "data_hora_abertura")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataHoraAbertura;
    
    @Column(name = "data_hora_fechamento")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataHoraFechamento;
    
    @Column(name = "data_prevista")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPrevista;
    
    @Column(name = "percentual")
    private int progresso;
    private String titulo;
    
    @ManyToOne
    @JoinColumn(name = "prioridade_codigo")
    private PrioridadeDTO prioridadeDTO;
    
    @OneToMany(mappedBy="ocorrenciaDTO", fetch = FetchType.LAZY, orphanRemoval=true, cascade= CascadeType.ALL)
    private List<ArquivoOcorrenciaDTO> arquivos;
    
    
    
    public OcorrenciaDTO(){
        
    }

    public OcorrenciaDTO(AlunoDTO alunoDTO,Date dataPrevista, int progresso, String titulo, PrioridadeDTO prioridadeDTO, 
            SetorDTO setorDTOAtribuido, StatusOcorrenciaDTO statusOcorrenciaDTO, 
            Long codigo, String descricao, Date dataHoraAbertura, SetorDTO setorDTOAutor, 
            Date dataHoraFechamento, List<SetorDTO> setoresObervadores, List<ArquivoOcorrenciaDTO> arquivos, 
            SetorDTO setorDTOAtribuido0, AcaoDisciplinarDTO acaoDisciplinarDTO) {
        this.alunoDTO = alunoDTO;
        this.dataPrevista =  dataPrevista;
        this.progresso = progresso;
        this.titulo = titulo;
        this.prioridadeDTO = prioridadeDTO;
        this.setorDTOAtribuido = setorDTOAtribuido;
        this.statusOcorrenciaDTO = statusOcorrenciaDTO;
        this.codigo = codigo;
        this.descricao = descricao;
        this.dataHoraAbertura = dataHoraAbertura;
        this.setorDTOAutor = setorDTOAutor;
        this.dataHoraFechamento = dataHoraFechamento;
        this.setoresObervadores = setoresObervadores;
        this.arquivos = arquivos;
        this.setorDTOAtribuido = setorDTOAtribuido;
        this.acaoDisciplinarDTO = acaoDisciplinarDTO;
    }
    
    public List<SetorDTO> getSetoresObervadores() {
        return setoresObervadores;
    }

    public void setSetoresObervadores(List<SetorDTO> setoresObervadores) {
        this.setoresObervadores = setoresObervadores;
    }

    public SetorDTO getSetorDTOAutor() {
        return setorDTOAutor;
    }

    public void setSetorDTOAutor(SetorDTO setorDTOAutor) {
        this.setorDTOAutor = setorDTOAutor;
    }

    public SetorDTO getSetorDTOAtribuido() {
        return setorDTOAtribuido;
    }

    public void setSetorDTOAtribuido(SetorDTO setorDTOAtribuido) {
        this.setorDTOAtribuido = setorDTOAtribuido;
    }
    
    public StatusOcorrenciaDTO getStatusOcorrenciaDTO() {
        return statusOcorrenciaDTO;
    }

    public void setStatusOcorrenciaDTO(StatusOcorrenciaDTO statusOcorrenciaDTO) {
        this.statusOcorrenciaDTO = statusOcorrenciaDTO;
    }

    public AcaoDisciplinarDTO getAcaoDisciplinarDTO() {
        return acaoDisciplinarDTO;
    }

    public void setAcaoDisciplinarDTO(AcaoDisciplinarDTO acaoDisciplinarDTO) {
        this.acaoDisciplinarDTO = acaoDisciplinarDTO;
    }

    public Date getDataHoraAbertura() {
        return dataHoraAbertura;
    }

    public void setDataHoraAbertura(Date dataHoraAbertura) {
        this.dataHoraAbertura = dataHoraAbertura;
    }

    public Date getDataHoraFechamento() {
        return dataHoraFechamento;
    }

    public void setDataHoraFechamento(Date dataHoraFechamento) {
        this.dataHoraFechamento = dataHoraFechamento;
    }

    public Date getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public int getProgresso() {
        return progresso;
    }

    public void setProgresso(int progresso) {
        this.progresso = progresso;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public PrioridadeDTO getPrioridadeDTO() {
        return prioridadeDTO;
    }

    public void setPrioridadeDTO(PrioridadeDTO prioridadeDTO) {
        this.prioridadeDTO = prioridadeDTO;
    }

    public AlunoDTO getAlunoDTO() {
        return alunoDTO;
    }

    public void setAlunoDTO(AlunoDTO alunoDTO) {
        this.alunoDTO = alunoDTO;
    }
    
    public List<ArquivoOcorrenciaDTO> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoOcorrenciaDTO> arquivos) {
        this.arquivos = arquivos;
    }
}
