package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_ocorrencia")
public class TipoOcorrenciaDTO extends GenericDTOIdDescricao {

    public TipoOcorrenciaDTO() {
    }

    public TipoOcorrenciaDTO(Long codigo) {
        this.codigo = codigo;
    }
    
    
    
    private static final long serialVersionUID = 7000L;  
}