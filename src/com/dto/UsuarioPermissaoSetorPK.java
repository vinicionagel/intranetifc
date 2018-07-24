package com.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class UsuarioPermissaoSetorPK implements Serializable{

    private static final long serialVersionUID = 29L;
        
    @Id
    @Column(name = "permissao_codigo")
    private Long permissaoCodigo;
    
    @Id
    @Column(name = "usuario_codigo")
    private Long usuarioCodigo;
    
    @Id
    @Column(name = "setor_codigo")
    private Long setorCodigo;
    
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

    public Long getSetorCodigo() {
        return setorCodigo;
    }

    public void setSetorCodigo(Long setorCodigo) {
        this.setorCodigo = setorCodigo;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.permissaoCodigo);
        hash = 37 * hash + Objects.hashCode(this.usuarioCodigo);
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
        final UsuarioPermissaoSetorPK other = (UsuarioPermissaoSetorPK) obj;
        if (!Objects.equals(this.permissaoCodigo, other.permissaoCodigo)) {
            return false;
        }
        if (!Objects.equals(this.usuarioCodigo, other.usuarioCodigo)) {
            return false;
        }
        return true;
    }
}