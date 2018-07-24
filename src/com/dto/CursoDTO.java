package com.dto;

import com.core.GenericDTOIdDescricao;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "curso")
public class CursoDTO extends GenericDTOIdDescricao {
    private static final long serialVersionUID = 2014L;
}