package com.dao;

import com.core.GenericDAO;
import com.dto.ArquivoOcorrenciaDTO;


public class ArquivoOcorrenciaDAO extends GenericDAO<ArquivoOcorrenciaDTO> {
    
    private static final long serialVersionUID = 20015L;
    
    public ArquivoOcorrenciaDAO() {
        super(ArquivoOcorrenciaDTO.class);
    }  
}
