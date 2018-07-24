package com.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "usuario_setor")
@IdClass(UsuarioSetorPK.class)
public class UsuarioSetorDTO implements Serializable {

    private static final long serialVersionUID = 30L;
    
    @Id
    @Column(name = "usuario_codigo", nullable=false,
			insertable=false, updatable=false)
    private Long usuarioCodigo;
    
    @Id
    @Column(name = "setor_codigo", nullable=false,
			insertable=false, updatable=false)
    private Long setorCodigo;
    
    @ManyToOne()
    @JoinColumn(name = "usuario_codigo")
    private UsuarioDTO usuarioDTO;
    
    @ManyToOne()
    @JoinColumn(name = "setor_codigo")
    private SetorDTO setorDTO;
    
    @ManyToOne()
    @JoinColumn(name = "funcao_codigo")
    private FuncaoDTO funcaoDTO;
    
    @Column(name = "responsavel_setor")
    private int responsavel;
    
    @Transient
    private boolean isSelecionado;
    
    @Transient
    private Long funcao = 0l;
    
    @Transient
    private boolean responsavelBool;
    
    public FuncaoDTO getFuncaoDTO() {
        return funcaoDTO;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public SetorDTO getSetorDTO() {
        return setorDTO;
    }

    public void setSetorDTO(SetorDTO setorDTO) {
        this.setorDTO = setorDTO;
    }

    public void setFuncaoDTO(FuncaoDTO funcaoDTO) {
        this.funcaoDTO = funcaoDTO;
    }

    public boolean isIsSelecionado() {
        return isSelecionado;
    }

    public void setIsSelecionado(boolean isSelecionado) {
        this.isSelecionado = isSelecionado;
    }

    public Long getFuncao() {
        return funcao;
    }

    public void setFuncao(Long funcao) {
        this.funcao = funcao;
    }

    public int getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(int responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.usuarioCodigo);
        hash = 79 * hash + Objects.hashCode(this.setorCodigo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsuarioSetorDTO other = (UsuarioSetorDTO) obj;
        if (!Objects.equals(this.usuarioCodigo, other.usuarioCodigo)) {
            return false;
        }
        if (!Objects.equals(this.setorCodigo, other.setorCodigo)) {
            return false;
        }
        return true;
    }

    public boolean isResponsavelBool() {
        return getResponsavel() == 1;
    }
    
    public boolean getResponsavelBool(){
        return responsavelBool;
    }
    
    public void setResponsavelBool(boolean responsavelBool) {
        this.responsavelBool = responsavelBool;
    }
    
}