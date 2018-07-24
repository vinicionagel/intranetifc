package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fabricante")
public class FabricanteDTO extends GenericDTOIdDescricao {
 
    private static final long serialVersionUID = 8L;
    
}