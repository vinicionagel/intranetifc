package com.bo;

import com.core.GenericBO;
import com.dao.TipoSanguineoDAO;
import com.dto.TipoSanguineoDTO;

public class TipoSanguineoBO extends GenericBO<TipoSanguineoDTO>{
    
    private static final long serialVersionUID = 30000L;
    private TipoSanguineoDAO dao = TipoSanguineoDAO.getInstance();

    private volatile static TipoSanguineoBO uniqueInstance;

    public static TipoSanguineoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoSanguineoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoSanguineoBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public TipoSanguineoBO() {
        super(TipoSanguineoDTO.class);
    }

}
