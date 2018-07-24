package com.bo;

import com.core.GenericBO;
import com.dao.TipoTelefoneDAO;
import com.dto.TipoTelefoneDTO;

public class TipoTelefoneBO extends GenericBO<TipoTelefoneDTO>{
    
    private static final long serialVersionUID = 30000L;
    private TipoTelefoneDAO dao = TipoTelefoneDAO.getInstance();

    private volatile static TipoTelefoneBO uniqueInstance;

    public static TipoTelefoneBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoTelefoneBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoTelefoneBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public TipoTelefoneBO() {
        super(TipoTelefoneDTO.class);
    }

}
