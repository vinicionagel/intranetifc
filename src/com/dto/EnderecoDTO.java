
package com.dto;

import com.core.GenericDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "endereco")
public class EnderecoDTO extends GenericDTO{ 
    
    private static final long serialVersionUID = 2011L;
    
    @ManyToOne()
    @JoinColumn(name = "tipo_logradouro_codigo")
    private TipoLogradouroDTO tipoLogradouroDTO;
    
    @ManyToOne()
    @JoinColumn(name = "municipio_codigo")
    private MunicipioDTO municipioDTO;
    
    @Column(name="nome_logradouro")
    private String nomeLogradouro;
    
    private String complemento;
    
    @Column(name="caixa_postal")
    private String caixaPostal;
    
    private String bairro;
    
    private int cep;
    
    private String numero;

    public TipoLogradouroDTO getTipoLogradouroDTO() {
        return tipoLogradouroDTO;
    }

    public void setTipoLogradouroDTO(TipoLogradouroDTO tipoLogradouroDTO) {
        this.tipoLogradouroDTO = tipoLogradouroDTO;
    }

    public MunicipioDTO getMunicipioDTO() {
        return municipioDTO;
    }

    public void setMunicipioDTO(MunicipioDTO municipioDTO) {
        this.municipioDTO = municipioDTO;
    }

    public String getNomeLogradouro() {
        return nomeLogradouro;
    }

    public void setNomeLogradouro(String nomeLogradouro) {
        this.nomeLogradouro = nomeLogradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCaixaPostal() {
        return caixaPostal;
    }

    public void setCaixaPostal(String caixaPostal) {
        this.caixaPostal = caixaPostal;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    
}
