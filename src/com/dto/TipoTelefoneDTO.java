package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_telefone")
public class TipoTelefoneDTO extends GenericDTOIdDescricao{
    private static final long serialVersionUID = 2000L;   
}
