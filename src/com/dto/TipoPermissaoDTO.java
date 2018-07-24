package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_permissao")
@Cacheable
public class TipoPermissaoDTO extends GenericDTOIdDescricao{

    private static final long serialVersionUID = 25L;
    
}