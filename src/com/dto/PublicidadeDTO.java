package com.dto;

import com.core.GenericDTO;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "publicidade")
@Inheritance(strategy= InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_publicidade",discriminatorType=DiscriminatorType.STRING)
public class PublicidadeDTO extends GenericDTO {

    private static final long serialVersionUID = 17L;
    
    @Column(name="nome_formato",unique = true)
    private String nomeFormato;
    
    @Column(name="data_envio")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataEnvio;
    
    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] arquivo;
    
    @Column(name="data_fim_visualizacao")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFimVisualizacao;

    public String getNomeFormato() {
        return nomeFormato;
    }

    public void setNomeFormato(String nomeFormato) {
        this.nomeFormato = nomeFormato;
    }

    public Date getDataEnvio() {
        return dataEnvio == null ? null : (Date) dataEnvio.clone();
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio == null ? null : (Date) dataEnvio.clone();
    }

    public Date getDataFimVisualizacao() {
        return dataFimVisualizacao == null ? null : (Date) dataFimVisualizacao.clone();
    }

    public void setDataFimVisualizacao(Date dataFimVisualizacao) {
        this.dataFimVisualizacao = dataFimVisualizacao == null ? null : (Date) dataFimVisualizacao.clone();
    }
    
    
    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("teste [nomeFormato=");
        builder.append(nomeFormato);
        builder.append(", dataEnvio=");
        builder.append(dataEnvio);
        builder.append(", dataFimVisualizacao=");
        builder.append(dataFimVisualizacao);
        builder.append("]");
        return builder.toString();
    }   
}