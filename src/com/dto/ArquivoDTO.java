package com.dto;

import com.core.GenericDTO;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "arquivo")
public class ArquivoDTO extends GenericDTO{    
    
    private static final long serialVersionUID = 2L;

    public ArquivoDTO(String nome) {
        this.nome = nome;
    }

    public ArquivoDTO() {
    }
    
    @Lob
    @Basic(fetch= FetchType.LAZY)
    private byte[] arquivo;
    private String nome;
    @ManyToOne()
    @JoinColumn(name = "chamado_codigo")
    private ChamadoDTO chamadoDTO;
    
    @Transient
    private int inicialBuilder;
    
    @Transient
    private int finalBuilder;
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ChamadoDTO getChamadoDTO() {
        return chamadoDTO;
    }

    public void setChamadoDTO(ChamadoDTO chamadoDTO) {
        this.chamadoDTO = chamadoDTO;
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
}