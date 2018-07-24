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

@Entity
@Table(name = "usuario_permissao_setor")
@IdClass(UsuarioPermissaoSetorPK.class)
public class UsuarioPermissaoSetorDTO implements Serializable {

    private static final long serialVersionUID = 28L;
    
    @Id
    @Column(name = "permissao_codigo", nullable=false,
			insertable=false, updatable=false)
    private Long permissaoCodigo;
    
    @Id
    @Column(name = "usuario_codigo", nullable=false,
			insertable=false, updatable=false)
    private Long usuarioCodigo;
    
    @Id
    @Column(name = "setor_codigo", nullable=false,
			insertable=false, updatable=false)
    private Long setorCodigo;
    
    @ManyToOne
    @JoinColumn(name="usuario_codigo")
    private UsuarioDTO usuarioDTO;
    
    @ManyToOne
    @JoinColumn(name="permissao_codigo")
    private PermissaoDTO permissaoDTO;
    
    @ManyToOne
    @JoinColumn(name="setor_codigo")
    private SetorDTO setorDTO;

    public Long getPermissaoCodigo() {
        return permissaoCodigo;
    }

    public void setPermissaoCodigo(Long permissaoCodigo) {
        this.permissaoCodigo = permissaoCodigo;
    }

    public Long getUsuarioCodigo() {
        return usuarioCodigo;
    }

    public void setUsuarioCodigo(Long usuarioCodigo) {
        this.usuarioCodigo = usuarioCodigo;
    }

    public UsuarioDTO getUsuarioDTO() {
        return usuarioDTO;
    }

    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
        this.usuarioDTO = usuarioDTO;
    }

    public PermissaoDTO getPermissaoDTO() {
        return permissaoDTO;
    }

    public void setPermissaoDTO(PermissaoDTO permissaoDTO) {
        this.permissaoDTO = permissaoDTO;
    }

    public SetorDTO getSetorDTO() {
        return setorDTO;
    }

    public void setSetorDTO(SetorDTO setorDTO) {
        this.setorDTO = setorDTO;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.permissaoCodigo);
        hash = 59 * hash + Objects.hashCode(this.usuarioCodigo);
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
        final UsuarioPermissaoSetorDTO other = (UsuarioPermissaoSetorDTO) obj;
        if (!Objects.equals(this.permissaoCodigo, other.permissaoCodigo)) {
            return false;
        }
        if (!Objects.equals(this.usuarioCodigo, other.usuarioCodigo)) {
            return false;
        }
        return true;
    }
    
    
    
}