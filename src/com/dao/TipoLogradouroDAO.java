package com.dao;

import com.core.GenericDAO;
import com.dto.TipoLogradouroDTO;
import java.util.logging.Logger;

public class TipoLogradouroDAO extends GenericDAO<TipoLogradouroDTO>{
    
    private static final long serialVersionUID = 20008L;   
    private volatile static TipoLogradouroDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(TipoLogradouroDAO.class.getName());
    
    public static TipoLogradouroDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TipoLogradouroDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TipoLogradouroDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public TipoLogradouroDAO() {
        super(TipoLogradouroDTO.class);
    }      
}
