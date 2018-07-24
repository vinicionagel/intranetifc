package com.bo;

import com.core.GenericBO;
import com.dao.TelefoneDAO;
import com.dto.TelefoneDTO;

public class TelefoneBO extends GenericBO<TelefoneDTO>{
    
    private static final long serialVersionUID = 30008L;
    private TelefoneDAO dao = TelefoneDAO.getInstance();

    private volatile static TelefoneBO uniqueInstance;

    public static TelefoneBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TelefoneBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TelefoneBO();
                }
            }
        }
        return uniqueInstance;
    }

    
    public TelefoneBO() {
        super(TelefoneDTO.class);
    }

}
