package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_logradouro")
public class TipoLogradouroDTO extends GenericDTOIdDescricao {
    private static final long serialVersionUID = 2006L;
}