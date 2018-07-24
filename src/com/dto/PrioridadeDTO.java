package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "prioridade")
public class PrioridadeDTO extends GenericDTOIdDescricao{
    private static final long serialVersionUID = 8000L;   
}
