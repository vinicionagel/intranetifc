package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status_ocorrencia")
public class StatusOcorrenciaDTO  extends GenericDTOIdDescricao{
    
    private static final long serialVersionUID = 2001L;
        
}
