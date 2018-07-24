package com.dao;

import com.core.GenericDAO;
import com.dto.TipoSanguineoDTO;
import java.util.logging.Logger;

public class TipoSanguineoDAO extends GenericDAO<TipoSanguineoDTO>{
    private static final long serialVersionUID = 20004L;   
    private volatile static TipoSanguineoDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(TipoSanguineoDAO.class.getName());
    
    public static TipoSanguineoDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (TipoSanguineoDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new TipoSanguineoDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public TipoSanguineoDAO() {
        super(TipoSanguineoDTO.class);
    }      
}

