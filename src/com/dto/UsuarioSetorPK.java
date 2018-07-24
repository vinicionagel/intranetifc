package com.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class UsuarioSetorPK implements Serializable {

    private static final long serialVersionUID = 14L;
    
    @Id
    @Column(name = "usuario_codigo",nullable=false,
			insertable=false, updatable=false)
    private Long usuarioCodigo;
    
    @Id
    @Column(name = "setor_codigo",nullable=false,
			insertable=false, updatable=false)
    private Long setorCodigo;

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
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.usuarioCodigo);
        hash = 97 * hash + Objects.hashCode(this.setorCodigo);
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
        final UsuarioSetorPK other = (UsuarioSetorPK) obj;
        if (!Objects.equals(this.usuarioCodigo, other.usuarioCodigo)) {
            return false;
        }
        if (!Objects.equals(this.setorCodigo, other.setorCodigo)) {
            return false;
        }
        return true;
    }
    
}