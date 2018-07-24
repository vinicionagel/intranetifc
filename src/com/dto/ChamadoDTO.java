package com.dto;

import com.core.GenericDTOIdDescricao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "chamado")
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_chamado",discriminatorType=DiscriminatorType.STRING)
public class ChamadoDTO extends GenericDTOIdDescricao {

    private static final long serialVersionUID = 5L;
    
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
    //relacionamentos
    @ManyToMany(cascade = CascadeType.PERSIST ,fetch= FetchType.LAZY)
    @JoinTable(name = "observador",
    joinColumns = { @JoinColumn(name = "chamado_codigo")},
    inverseJoinColumns = { @JoinColumn(name = "usuario_codigo")})
    private List<UsuarioDTO> observadores;            
    
    @ManyToOne
    @JoinColumn(name = "prioridade_codigo")
    private PrioridadeDTO prioridadeDTO; 
    
    @ManyToOne
    @JoinColumn(name = "usuario_codigo_autor")
    private UsuarioDTO usuarioAutorDTO;
    
    @ManyToOne
    @JoinColumn(name = "usuario_codigo_atribuido")
    private UsuarioDTO usuarioAtribuidoDTO;
    
    @ManyToOne
    @JoinColumn(name = "status_codigo")
    private StatusDTO statusDTO;   
    
    @OneToMany(mappedBy="chamadoDTO", fetch = FetchType.LAZY, orphanRemoval=true, cascade= CascadeType.PERSIST)
    private List<ArquivoDTO> arquivos;
    
    public ChamadoDTO( Date dataPrevista, int progresso, String titulo, PrioridadeDTO prioridadeDTO, UsuarioDTO usuarioAtribuidoDTO, StatusDTO statusDTO, 
            Long codigo, String descricao, Date dataAbertura, UsuarioDTO autor, Date dataFechamento, List<UsuarioDTO> observadores, List<ArquivoDTO> arquivoDTOs, UsuarioDTO atribuido) {
        this.dataPrevista = dataPrevista == null ? null : (Date) dataPrevista.clone();
        this.progresso = progresso;
        this.titulo = titulo;
        this.prioridadeDTO = prioridadeDTO;
        this.usuarioAtribuidoDTO = usuarioAtribuidoDTO;
        this.statusDTO = statusDTO;
        this.codigo = codigo;
        this.descricao = descricao;
        this.dataHoraAbertura = dataAbertura == null ? null : (Date) dataAbertura.clone();
        this.usuarioAutorDTO = autor;
        this.dataHoraFechamento = dataFechamento == null ? null : (Date) dataFechamento.clone();
        this.observadores = observadores;
        this.arquivos = arquivoDTOs;
        this.usuarioAtribuidoDTO = atribuido;
    }
    
    public ChamadoDTO() {
        observadores = new ArrayList<>();
        arquivos = new ArrayList<>();
    }

    public Date getDataHoraAbertura() {
        return dataHoraAbertura == null ? null : (Date) dataHoraAbertura.clone();
    }

    public void setDataHoraAbertura(Date dataHoraAbertura) {
        this.dataHoraAbertura = dataHoraAbertura == null ? null : (Date) dataHoraAbertura.clone();
    }

    public Date getDataHoraFechamento() {
        return dataHoraFechamento == null ? null : (Date) dataHoraFechamento.clone();
    }

    public void setDataHoraFechamento(Date dataHoraFechamento) {
        this.dataHoraFechamento = dataHoraFechamento == null ? null : (Date) dataHoraFechamento.clone();
    }

    public PrioridadeDTO getPrioridadeDTO() {
        return prioridadeDTO;
    }

    public void setPrioridadeDTO(PrioridadeDTO prioridade) {
        this.prioridadeDTO = prioridade;
    }
    
    public Date getDataPrevista() {
        return dataPrevista == null ? null : (Date) dataPrevista.clone();
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista == null ? null : (Date) dataPrevista.clone();
    }

    public int getProgresso() {
        return progresso;
    }

    public void setProgresso(int progresso) {
        this.progresso = progresso;
    }

    public StatusDTO getStatusDTO() {
        return statusDTO;
    }

    public void setStatusDTO(StatusDTO statusDTO) {
        this.statusDTO = statusDTO;
    }

    public List<UsuarioDTO> getObservadores() {
        return observadores;
    }

    public void setObservadores(List<UsuarioDTO> observadores) {
        this.observadores = observadores;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UsuarioDTO getUsuarioAutorDTO() {
        return usuarioAutorDTO;
    }

    public void setUsuarioAutorDTO(UsuarioDTO usuarioAutorDTO) {
        this.usuarioAutorDTO = usuarioAutorDTO;
    }

    public UsuarioDTO getUsuarioAtribuidoDTO() {
        return usuarioAtribuidoDTO;
    }

    public void setUsuarioAtribuidoDTO(UsuarioDTO usuarioAtribuidoDTO) {
        this.usuarioAtribuidoDTO = usuarioAtribuidoDTO;
    }
        
    public List<ArquivoDTO> getArquivos() {
        return arquivos;
    }

    public void setArquivos(List<ArquivoDTO> arquivos) {
        this.arquivos = arquivos;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ChamadoDTO [dataHoraAbertura=");
        builder.append(dataHoraAbertura);
        builder.append(", dataHoraPrevista=");
        builder.append(getDataPrevista());
        builder.append(", dataHoraFechamento=");
        builder.append(dataHoraFechamento);  
        builder.append(", codigo=");
        builder.append(codigo);
        builder.append(", descricao=");
        builder.append(descricao);
        builder.append("]");
        return builder.toString();
    }
}