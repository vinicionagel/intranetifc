package com.dao;

import com.core.GenericDAO;
import com.dto.PublicidadeDTO;

public class PublicidadeDAO extends GenericDAO<PublicidadeDTO> {
    
    private static final long serialVersionUID = 1015L;
    
    public PublicidadeDAO() {
        super(PublicidadeDTO.class);
    }
}
