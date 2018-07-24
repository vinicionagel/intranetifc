package com.dao;

import com.core.GenericDAO;
import com.dto.TipoConfiguracaoDTO;
import java.util.logging.Logger;

public class TipoConfiguracaoDAO extends GenericDAO<TipoConfiguracaoDTO>{
        
    private static final long serialVersionUID = 1009L;
    static final Logger logger = Logger.getLogger(TipoConfiguracaoDAO.class.getName());
    private volatile static TipoConfiguracaoDAO uniqueInstance;
    
    public TipoConfiguracaoDAO() {
        super(TipoConfiguracaoDAO.class);
    }
        
    public static TipoConfiguracaoDAO getInstance() {
        if (uniqueInstance == null) {
            synchronized (TipoConfiguracaoDAO.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new TipoConfiguracaoDAO();
                }
            }
        }
        return uniqueInstance;
    }
}