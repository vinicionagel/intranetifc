package com.bo;

import com.core.GenericBO;
import com.dto.ArquivoDTO;

public class ArquivoBO extends GenericBO<ArquivoDTO>{
    
    private static final long serialVersionUID = 102L;
    
    public ArquivoBO() {
        super(ArquivoDTO.class);
    }
}