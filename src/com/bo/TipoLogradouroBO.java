package com.bo;

import com.core.GenericBO;
import com.dao.TipoLogradouroDAO;
import com.dto.TipoLogradouroDTO;

public class TipoLogradouroBO extends GenericBO<TipoLogradouroDTO>{
    
    private static final long serialVersionUID = 30006L;
    private TipoLogradouroDAO dao = TipoLogradouroDAO.getInstance();

    private volatile static TipoLogradouroBO uniqueInstance;

    public static TipoLogradouroBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoLogradouroBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoLogradouroBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public TipoLogradouroBO() {
        super(TipoLogradouroDTO.class);
    }

}
