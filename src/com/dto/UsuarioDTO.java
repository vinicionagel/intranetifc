package com.dto;

import com.core.GenericDTO;
import com.util.MD5;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Cacheable(true)
@Table(name = "usuario")
public class UsuarioDTO extends GenericDTO {

    private static final long serialVersionUID = 27L;
    
    private String login;
    private String senha;
    private String email;
    private String nome;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "data_nascimento")
    private Date dataNascimento;
    private String telefone;
    private int estado;
    private String verificacao;
    //relacionamentos
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_permissao",
    joinColumns = {
        @JoinColumn(name = "usuario_codigo")},
    inverseJoinColumns = {
        @JoinColumn(name = "permissao_codigo")})
    private List<PermissaoDTO> permissoes;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_configuracao",
    joinColumns = {
        @JoinColumn(name = "usuario_codigo")},
    inverseJoinColumns = {
        @JoinColumn(name = "configuracao_codigo")})
    private List<ConfiguracaoDTO> configuracoes;
       
    @ManyToOne()
    @JoinColumn(name = "ultimo_campus_codigo")
    private CampusDTO ultimoCampus;
        
    public UsuarioDTO() {
        permissoes = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento == null ? null : (Date) dataNascimento.clone();
    }

    public void setDataNascimento(Date dataNascimento) {        
        this.dataNascimento = dataNascimento == null ? null : (Date) dataNascimento.clone();        
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = MD5.cripto(senha);
    }

    public void setSenhaMD5(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerificacao() {
        return verificacao;
    }

    public void setVerificacao(String verificacao) {
        this.verificacao = verificacao;
    }

    public List<PermissaoDTO> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(List<PermissaoDTO> permissoes) {
        this.permissoes = permissoes;
    }

    public CampusDTO getUltimoCampus() {
        return ultimoCampus;
    }

    public void setUltimoCampus(CampusDTO ultimoCampus) {
        this.ultimoCampus = ultimoCampus;
    }
    
    public List<ConfiguracaoDTO> getConfiguracoes() {
        return configuracoes;
    }

    public void setConfiguracoes(List<ConfiguracaoDTO> configuracoes) {
        this.configuracoes = configuracoes;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UsuarioDTO [codigoVerificacao=");
        builder.append(", login=");
        builder.append(getLogin());
        builder.append(", senha=");
        builder.append(getSenha());
        builder.append(", email=");
        builder.append(getEmail());
        builder.append(", nome=");
        builder.append(getNome());
        builder.append(", dataNascimento=");
        builder.append(getDataNascimento());
        builder.append(", telefone=");
        builder.append(getTelefone());
        builder.append(", estado=");
        builder.append(getEstado());
        builder.append(", Codigo verificacao=");
        builder.append(", Verificacao=");
        builder.append(getVerificacao());
        builder.append("]");
        return builder.toString();
    }   
}