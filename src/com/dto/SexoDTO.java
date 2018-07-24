/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author vinicio
 */
@Entity
@Table(name = "sexo")
public class SexoDTO extends GenericDTOIdDescricao{
    
    private static final long serialVersionUID = 2003L;
        
    private String sigla;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}

