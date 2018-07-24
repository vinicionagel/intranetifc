package com.dao;

import com.core.GenericDAO;
import com.dto.PrioridadeDTO;
import java.util.logging.Logger;
    
public class PrioridadeDAO extends GenericDAO<PrioridadeDTO>{
    
    private static final long serialVersionUID = 8001L;   
    private volatile static PrioridadeDAO uniqueInstance;
    static final Logger logger = Logger.getLogger(PrioridadeDAO.class.getName());
    
    public static PrioridadeDAO getInstance(){
        if (uniqueInstance == null){
            synchronized (PrioridadeDAO.class){
                if (uniqueInstance == null){
                    uniqueInstance = new PrioridadeDAO();
                }
            }
        }
        return uniqueInstance;
    } 
    
    public PrioridadeDAO() {
        super(PrioridadeDTO.class);
    }      
}
