/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dto;

import com.core.GenericDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author vinicio
 */
@Entity
@Table(name = "configuracao_banco")
public class ConfiguracaoBancoDTO extends GenericDTO{
    
    private static final long serialVersionUID = 2016L;
    
    private String endereco;
    @Column(name = "nome_banco")
    private String nomeBanco;
    
    private String porta;

    private String usuario;
    
    private String senha;
    
    @ManyToOne
    @JoinColumn(name="tipo_configuracao_codigo")
    private TipoConfiguracaoDTO tipoConfiguracaoDTO;

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getPorta() {
        return porta;
    }

    public void setPorta(String porta) {
        this.porta = porta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public TipoConfiguracaoDTO getTipoConfiguracaoDTO() {
        return tipoConfiguracaoDTO;
    }

    public void setTipoConfiguracaoDTO(TipoConfiguracaoDTO tipoConfiguracaoDTO) {
        this.tipoConfiguracaoDTO = tipoConfiguracaoDTO;
    }
    /**
     * 
     * @return url da conexao para postgres
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:postgresql://");
        builder.append(endereco);
        builder.append(":");
        builder.append(porta);
        builder.append("/");
        builder.append(nomeBanco);
        return builder.toString();
    }
    
}
