package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dia_semana")
public class DiaSemanaDTO extends GenericDTOIdDescricao{
    
    private static final long serialVersionUID = 2017L;
    
    private String sigla;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
