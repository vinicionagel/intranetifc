package com.bo;

import com.core.GenericBO;
import com.dto.BannerDTO;

public class BannerBO extends GenericBO<BannerDTO>{
    
    private static final long serialVersionUID = 103L;
    
    public BannerBO(){
        super(BannerDTO.class);
    }
}
