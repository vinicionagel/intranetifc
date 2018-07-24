package com.dto;

import com.core.GenericDTO;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "arquivo_ocorrencia")
public class ArquivoOcorrenciaDTO extends GenericDTO {

    private static final long serialVersionUID = 2015L;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "arquivo_ocorrencia")
    private byte[] arquivo;
    private String nome;
    @ManyToOne()
    @JoinColumn(name = "ocorrencia_codigo")
    private OcorrenciaDTO ocorrenciaDTO;

    @Transient
    private int inicialBuilder;

    @Transient
    private int finalBuilder;
    
    public ArquivoOcorrenciaDTO() {
    }

    public ArquivoOcorrenciaDTO(String nome) {
        this.nome = nome;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public OcorrenciaDTO getOcorrenciaDTO() {
        return ocorrenciaDTO;
    }

    public void setOcorrenciaDTO(OcorrenciaDTO ocorrenciaDTO) {
        this.ocorrenciaDTO = ocorrenciaDTO;
    }

    public byte[] getArquivo() {
        return arquivo == null ? null : arquivo.clone();
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo == null ? null : arquivo.clone();
    }

    public int getInicialBuilder() {
        return inicialBuilder;
    }

    public void setInicialBuilder(int inicialBuilder) {
        this.inicialBuilder = inicialBuilder;
    }

    public int getFinalBuilder() {
        return finalBuilder;
    }

    public void setFinalBuilder(int finalBuilder) {
        this.finalBuilder = finalBuilder;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ArquivoOcorrenciaDTO other = (ArquivoOcorrenciaDTO) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return true;
    }
}
