package com.dao;

import com.core.GenericDAO;
import com.dto.BannerDTO;

public class BannerDAO extends GenericDAO<BannerDTO> {
    
    private static final long serialVersionUID = 1003L;
    
    public BannerDAO() {
        super(BannerDTO.class);
    }  
}