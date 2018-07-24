package com.dao;

import com.core.GenericDAO;
import com.dto.TipoTelefoneDTO;
import java.util.logging.Logger;

public class TipoTelefoneDAO extends GenericDAO<TipoTelefoneDTO>{
    
    private static final long serialVersionUID = 20000L;   
    private volatile static TipoTelefoneDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(TipoTelefoneDAO.class.getName());
    
    public static TipoTelefoneDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TipoTelefoneDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TipoTelefoneDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public TipoTelefoneDAO() {
        super(TipoTelefoneDTO.class);
    }      
}
