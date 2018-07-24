package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "status")
@Cacheable
public class StatusDTO extends GenericDTOIdDescricao {

    private static final long serialVersionUID = 23L;
    
    public StatusDTO() {
        
    }
    
    public StatusDTO(Long codigo){
        this.codigo = codigo;
    }    
}