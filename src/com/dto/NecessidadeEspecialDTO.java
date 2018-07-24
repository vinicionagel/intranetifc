package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "necessidade_especial")
public class NecessidadeEspecialDTO extends GenericDTOIdDescricao{
    private static final long serialVersionUID = 2005L;   
}
