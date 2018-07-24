package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_patrimonio")
public class TipoPatrimonioDTO extends GenericDTOIdDescricao {

    private static final long serialVersionUID = 24L;
    
}