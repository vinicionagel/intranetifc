package com.bo;

import com.core.GenericBO;
import com.dao.TipoConfiguracaoDAO;
import com.dto.TipoConfiguracaoDTO;

public class TipoConfiguracaoBO extends GenericBO<TipoConfiguracaoDTO>{
    
    private static final long serialVersionUID = 131L;
    private TipoConfiguracaoDAO dao = TipoConfiguracaoDAO.getInstance();

    private volatile static TipoConfiguracaoBO uniqueInstance;

    public static TipoConfiguracaoBO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoConfiguracaoBO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoConfiguracaoBO();
                }
            }
        }
        return uniqueInstance;
    }
    
    public TipoConfiguracaoBO(){
        super(TipoConfiguracaoBO.class);
    }
}