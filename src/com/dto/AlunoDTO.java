package com.dto;

import com.core.GenericDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.List;
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
@Table(name = "aluno")
public class AlunoDTO extends GenericDTO{
    
    private static final long serialVersionUID = 2010L;
       
    @Column(name = "id_siga")
    private int idSiga;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "data_nascimento")
    private Date dataNascimento;
        
    @Column(name = "nome_pai")
    private String nomePai;
    
    @Column(name = "nome_mae")
    private String nomeMae;
    
    private String nome;
    
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "sexo_codigo")
    private SexoDTO sexoDTO;
    
    @ManyToOne
    @JoinColumn(name = "tipo_sanguineo_codigo")
    private TipoSanguineoDTO tipoSanguineoDTO;
    
    @ManyToOne
    @JoinColumn(name = "endereco_codigo")
    private EnderecoDTO enderecoDTO;
    
    @OneToMany(mappedBy="alunoDTO", fetch = FetchType.LAZY, orphanRemoval=true, cascade= CascadeType.ALL)
    private List<TelefoneDTO> telefones;
    
    @ManyToMany(cascade = CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinTable(name = "aluno_necessidade_especial",
    joinColumns = { @JoinColumn(name = "aluno_codigo")},
    inverseJoinColumns = { @JoinColumn(name = "necessidade_especial_codigo")})   
    private List<NecessidadeEspecialDTO> necessidadeEspecial;  
    
    @ManyToMany(cascade = CascadeType.ALL,fetch= FetchType.LAZY)
    @JoinTable(name = "aluno_curso",
    joinColumns = { @JoinColumn(name = "aluno_codigo")},
    inverseJoinColumns = { @JoinColumn(name = "curso_codigo")})   
    private List<CursoDTO> cursos;  
    
    public AlunoDTO() {
        telefones = new ArrayList<>();
        cursos = new ArrayList<>();
        necessidadeEspecial = new ArrayList<>();
    }
    
    public TipoSanguineoDTO getTipoSanguineoDTO() {
        return tipoSanguineoDTO;
    }

    public void setTipoSanguineoDTO(TipoSanguineoDTO tipoSanguineoDTO) {
        this.tipoSanguineoDTO = tipoSanguineoDTO;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String NomePai) {
        this.nomePai = NomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String NomeMae) {
        this.nomeMae = NomeMae;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SexoDTO getSexoDTO() {
        return sexoDTO;
    }

    public void setSexoDTO(SexoDTO sexoDTO) {
        this.sexoDTO = sexoDTO;
    }

    public EnderecoDTO getEnderecoDTO() {
        return enderecoDTO;
    }

    public void setEnderecoDTO(EnderecoDTO enderecoDTO) {
        this.enderecoDTO = enderecoDTO;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public List<TelefoneDTO> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<TelefoneDTO> telefones) {
        this.telefones = telefones;
    }

    public List<NecessidadeEspecialDTO> getNecessidadeEspecial() {
        return necessidadeEspecial;
    }

    public void setNecessidadeEspecial(List<NecessidadeEspecialDTO> necessidadeEspecial) {
        this.necessidadeEspecial = necessidadeEspecial;
    }

    public List<CursoDTO> getCursos() {
        return cursos;
    }

    public void setCursos(List<CursoDTO> cursos) {
        this.cursos = cursos;
    }

    public int getIdSiga() {
        return idSiga;
    }

    public void setIdSiga(int idSiga) {
        this.idSiga = idSiga;
    }
}
