package com.core;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class GenericDTOIdDescricao extends GenericDTO {
    
    @Column(name = "descricao", length = 255)
    protected String descricao;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
  
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof GenericDTOIdDescricao)) {
            return false;
        }
        GenericDTOIdDescricao other = (GenericDTOIdDescricao) object;
        if (((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo)))
                || ((this.descricao == null && other.descricao != null) || (this.descricao != null && !this.descricao.equals(other.descricao)))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GenericDTOIdDescricao [codigo=");
        builder.append(codigo);
        builder.append(", descricao=");
        builder.append(descricao);
        builder.append("]");
        return builder.toString();
    }
}